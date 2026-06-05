package com.en.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.en.springbootinit.common.BaseResponse;
import com.en.springbootinit.common.ErrorCode;
import com.en.springbootinit.common.ResultUtils;
import com.en.springbootinit.exception.BusinessException;
import com.en.springbootinit.model.dto.deviceheartbeatlog.DeviceHeartbeatLogAddRequest;
import com.en.springbootinit.model.dto.deviceheartbeatlog.DeviceHeartbeatLogQueryRequest;
import com.en.springbootinit.model.entity.DeviceHeartbeatLog;
import com.en.springbootinit.service.DeviceHeartbeatLogService;
// import com.en.springbootinit.service.DeviceInfoService; // 进阶联动时使用
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

@Api(tags = "设备心跳与工况流水接口")
@RestController
@RequestMapping("/deviceHeartbeatLog")
public class DeviceHeartbeatLogController {

    @Resource
    private DeviceHeartbeatLogService deviceHeartbeatLogService;

    // @Resource
    // private DeviceInfoService deviceInfoService;

    @ApiOperation("接收设备心跳上报(通常由网关调用)")
    @PostMapping("/add")
    public BaseResponse<Long> addHeartbeatLog(@RequestBody DeviceHeartbeatLogAddRequest addRequest) {
        if (addRequest == null || !StringUtils.hasText(addRequest.getDeviceSn())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备SN不能为空");
        }

        DeviceHeartbeatLog heartbeatLog = new DeviceHeartbeatLog();
        BeanUtils.copyProperties(addRequest, heartbeatLog);
        // 系统自动注入上报时间，防止硬件时钟不准
        heartbeatLog.setReportTime(LocalDateTime.now());

        boolean result = deviceHeartbeatLogService.save(heartbeatLog);

        // ⚠️【企业级联动规范】：硬件心跳上报后，通常需要同步更新 DeviceInfo 表中的 last_heartbeat 和 status(变在线)
        // 此处逻辑可写在 Service 层，由消息队列(MQ)异步处理最佳。为了 Controller 干净，这里仅做提示。
        /*
        if (result) {
            DeviceInfo info = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>().eq("device_sn", addRequest.getDeviceSn()));
            if (info != null) {
                info.setLastHeartbeat(new Date());
                info.setIpAddress(addRequest.getIpAddress());
                info.setStatus(1); // 1-在线
                deviceInfoService.updateById(info);
            }
        }
        */

        return ResultUtils.success(heartbeatLog.getId());
    }

    @ApiOperation("根据ID查询心跳记录明细")
    @GetMapping("/get")
    public BaseResponse<DeviceHeartbeatLog> getHeartbeatLogById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DeviceHeartbeatLog log = deviceHeartbeatLogService.getById(id);
        return ResultUtils.success(log);
    }

    // 注意：心跳流水表一般不允许修改(Update)和手动删除(Delete)，只能定期通过定时任务清理过期数据，以保证日志审计的真实性。
    // 因此这里特意去掉了 /update 和 /delete 接口。

    @ApiOperation("分页获取心跳流水(支持时间段与设备SN检索)")
    @PostMapping("/list/page")
    public BaseResponse<Page<DeviceHeartbeatLog>> listHeartbeatLogByPage(@RequestBody DeviceHeartbeatLogQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();

        QueryWrapper<DeviceHeartbeatLog> queryWrapper = new QueryWrapper<>();

        // 1. 设备SN：针对流水表，一般是精准查询某一台设备的历史工况
        queryWrapper.eq(StringUtils.hasText(queryRequest.getDeviceSn()), "device_sn", queryRequest.getDeviceSn());

        // 2. IP地址：模糊查询
        queryWrapper.like(StringUtils.hasText(queryRequest.getIpAddress()), "ip_address", queryRequest.getIpAddress());

        // 3. 【核心】时间段检索 (大于等于 startTime，小于等于 endTime)
        if (queryRequest.getStartTime() != null) {
            queryWrapper.ge("report_time", queryRequest.getStartTime());
        }
        if (queryRequest.getEndTime() != null) {
            queryWrapper.le("report_time", queryRequest.getEndTime());
        }

        // 4. 流水表必须按时间倒序排列，优先看最新心跳
        queryWrapper.orderByDesc("report_time");

        Page<DeviceHeartbeatLog> heartbeatLogPage = deviceHeartbeatLogService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(heartbeatLogPage);
    }
}