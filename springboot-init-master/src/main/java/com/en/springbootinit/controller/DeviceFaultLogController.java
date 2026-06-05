package com.en.springbootinit.controller;

import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.en.springbootinit.common.BaseResponse;
import com.en.springbootinit.common.DeleteRequest;
import com.en.springbootinit.common.ErrorCode;
import com.en.springbootinit.common.ResultUtils;
import com.en.springbootinit.exception.BusinessException;
import com.en.springbootinit.model.dto.devicefaultlog.DeviceFaultLogAddRequest;
import com.en.springbootinit.model.dto.devicefaultlog.DeviceFaultLogQueryRequest;
import com.en.springbootinit.model.dto.devicefaultlog.DeviceFaultLogUpdateRequest;
import com.en.springbootinit.model.entity.DeviceFaultLog;
import com.en.springbootinit.service.DeviceFaultLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

@Api(tags = "设备故障日志接口")
@RestController
@RequestMapping("/deviceFaultLog")
public class DeviceFaultLogController {

    @Resource
    private DeviceFaultLogService deviceFaultLogService;



    // region 设备故障的增删改查 分页


    @ApiOperation("设备或系统上报故障")
    @PostMapping("/add")
    public BaseResponse<Long> addDeviceFaultLog(@RequestBody DeviceFaultLogAddRequest addRequest) {
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DeviceFaultLog deviceFaultLog = new DeviceFaultLog();
        // 将 DTO 属性拷贝到实体类中
        BeanUtils.copyProperties(addRequest, deviceFaultLog);

        // 如果没有传发生时间，默认当前时间
        if (deviceFaultLog.getOccurTime() == null) {
            deviceFaultLog.setOccurTime(LocalDateTime.now());

        }
        // 默认状态为待处理
        deviceFaultLog.setStatus(0);

        boolean result = deviceFaultLogService.save(deviceFaultLog);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "上报故障失败");
        }
        return ResultUtils.success(deviceFaultLog.getId());
    }

    @ApiOperation("运维处理故障(更新)")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateDeviceFaultLog(@RequestBody DeviceFaultLogUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ID不能为空");
        }
        DeviceFaultLog deviceFaultLog = new DeviceFaultLog();
        BeanUtils.copyProperties(updateRequest, deviceFaultLog);

        // 如果状态变更为 "已解决(2)"，自动记录解决时间
        if (Integer.valueOf(2).equals(updateRequest.getStatus())) {
            deviceFaultLog.setResolveTime(LocalDateTime.now());
        }

        boolean result = deviceFaultLogService.updateById(deviceFaultLog);
        return ResultUtils.success(result);
    }

    @ApiOperation("根据ID查询单条故障详情")
    @GetMapping("/get")
    public BaseResponse<DeviceFaultLog> getDeviceFaultLogById(@RequestParam(value = "id", required = true) Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DeviceFaultLog deviceFaultLog = deviceFaultLogService.getById(id);
        if (deviceFaultLog == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(deviceFaultLog);
    }

    @ApiOperation("删除故障记录(逻辑删除)")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDeviceFaultLog(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = deviceFaultLogService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @ApiOperation("分页获取故障列表(支持多条件筛选)")
    @PostMapping("/list/page")
    public BaseResponse<Page<DeviceFaultLog>> listDeviceFaultLogByPage(@RequestBody DeviceFaultLogQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1. 获取分页参数
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();

        // 2. 构造查询条件
        QueryWrapper<DeviceFaultLog> queryWrapper = new QueryWrapper<>();

        // 设备SN：模糊查询 (替换为 hasText)
        queryWrapper.like(StringUtils.hasText(queryRequest.getDeviceSn()), "device_sn", queryRequest.getDeviceSn());
        // 故障代码：精确查询 (替换为 hasText)
        queryWrapper.eq(StringUtils.hasText(queryRequest.getFaultCode()), "fault_code", queryRequest.getFaultCode());
        // 严重等级：精确查询
        queryWrapper.eq(queryRequest.getSeverity() != null, "severity", queryRequest.getSeverity());
        // 处理状态：精确查询
        queryWrapper.eq(queryRequest.getStatus() != null, "status", queryRequest.getStatus());

        // 按照发生时间倒序排列 (最新的告警在最前面)
        queryWrapper.orderByDesc("occur_time");

        // 3. 执行分页查询
        Page<DeviceFaultLog> deviceFaultLogPage = deviceFaultLogService.page(new Page<>(current, size), queryWrapper);

        return ResultUtils.success(deviceFaultLogPage);
    }

    // endregion


    //  region







    //endregion
}