package com.en.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.en.springbootinit.common.BaseResponse;
import com.en.springbootinit.common.DeleteRequest;
import com.en.springbootinit.common.ErrorCode;
import com.en.springbootinit.common.ResultUtils;
import com.en.springbootinit.exception.BusinessException;
import com.en.springbootinit.model.dto.deviceinfo.DeviceInfoAddRequest;
import com.en.springbootinit.model.dto.deviceinfo.DeviceInfoQueryRequest;
import com.en.springbootinit.model.dto.deviceinfo.DeviceInfoUpdateRequest;
import com.en.springbootinit.model.entity.DeviceInfo;
import com.en.springbootinit.model.entity.DeviceType;
import com.en.springbootinit.model.vo.DeviceInfoVO;
import com.en.springbootinit.service.DeviceInfoService;
import com.en.springbootinit.service.DeviceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "设备基础信息管理接口")
@RestController
@RequestMapping("/deviceInfo")
public class DeviceInfoController {

    @Resource
    private DeviceInfoService deviceInfoService;

    // 【新增注入】需要用到设备类型服务来查名称
    @Resource
    private DeviceTypeService deviceTypeService;

    @ApiOperation("录入新设备")
    @PostMapping("/add")
    public BaseResponse<Long> addDeviceInfo(@RequestBody DeviceInfoAddRequest addRequest) {
        // ... (保持和上一版完全一致)
        if (addRequest == null || !StringUtils.hasText(addRequest.getDeviceSn())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备序列号不能为空");
        }

        long count = deviceInfoService.count(new QueryWrapper<DeviceInfo>().eq("device_sn", addRequest.getDeviceSn()));
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该设备序列号已存在");
        }

        DeviceInfo deviceInfo = new DeviceInfo();
        BeanUtils.copyProperties(addRequest, deviceInfo);
        deviceInfo.setStatus(0);

        boolean result = deviceInfoService.save(deviceInfo);
        return ResultUtils.success(deviceInfo.getId());
    }

    @ApiOperation("更新设备基础信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateDeviceInfo(@RequestBody DeviceInfoUpdateRequest updateRequest) {
        // ... (保持和上一版完全一致)
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        BeanUtils.copyProperties(updateRequest, deviceInfo);
        boolean result = deviceInfoService.updateById(deviceInfo);
        return ResultUtils.success(result);
    }

    @ApiOperation("根据ID获取设备详情")
    @GetMapping("/get")
    public BaseResponse<DeviceInfoVO> getDeviceInfoById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DeviceInfo deviceInfo = deviceInfoService.getById(id);
        if (deviceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 单条记录也包装成 VO 返回
        DeviceInfoVO vo = new DeviceInfoVO();
        BeanUtils.copyProperties(deviceInfo, vo);
        if (deviceInfo.getTypeId() != null) {
            DeviceType type = deviceTypeService.getById(deviceInfo.getTypeId());
            if (type != null) {
                vo.setTypeName(type.getTypeName());
            }
        }
        return ResultUtils.success(vo);
    }

    @ApiOperation("根据 IP 或 MAC 地址精准查找设备")
    @GetMapping("/getByNetwork")
    public BaseResponse<DeviceInfo> getDeviceByNetwork(
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) String macAddress) {
        // ... (保持和上一版完全一致)
        if (!StringUtils.hasText(ipAddress) && !StringUtils.hasText(macAddress)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "IP地址和MAC地址不能同时为空");
        }
        QueryWrapper<DeviceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(ipAddress), "ip_address", ipAddress);
        queryWrapper.eq(StringUtils.hasText(macAddress), "mac_address", macAddress);
        queryWrapper.orderByDesc("last_heartbeat").last("limit 1");

        DeviceInfo deviceInfo = deviceInfoService.getOne(queryWrapper);
        return ResultUtils.success(deviceInfo);
    }

    @ApiOperation("删除设备(逻辑删除)")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDeviceInfo(@RequestBody DeleteRequest deleteRequest) {
        // ... (保持和上一版完全一致)
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = deviceInfoService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @ApiOperation("分页获取设备列表(含名称组装和地图筛选)")
    @PostMapping("/list/page")
    public BaseResponse<Page<DeviceInfoVO>> listDeviceInfoByPage(@RequestBody DeviceInfoQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        QueryWrapper<DeviceInfo> queryWrapper = new QueryWrapper<>();

        // 1. 基础条件筛选 (新增了 typeId 筛选)
        queryWrapper.like(StringUtils.hasText(queryRequest.getDeviceSn()), "device_sn", queryRequest.getDeviceSn());
        queryWrapper.like(StringUtils.hasText(queryRequest.getDeviceName()), "device_name", queryRequest.getDeviceName());
        queryWrapper.eq(queryRequest.getStatus() != null, "status", queryRequest.getStatus());
        queryWrapper.eq(queryRequest.getOwnerId() != null, "owner_id", queryRequest.getOwnerId());
        queryWrapper.eq(queryRequest.getTypeId() != null, "type_id", queryRequest.getTypeId());

        // 地图相关筛选...
        if (queryRequest.getMinLng() != null && queryRequest.getMaxLng() != null &&
                queryRequest.getMinLat() != null && queryRequest.getMaxLat() != null) {
            queryWrapper.ge("longitude", queryRequest.getMinLng())
                    .le("longitude", queryRequest.getMaxLng())
                    .ge("latitude", queryRequest.getMinLat())
                    .le("latitude", queryRequest.getMaxLat());
        }
        queryWrapper.orderByDesc("created_at");

        // 2. 查出实体类分页数据
        Page<DeviceInfo> deviceInfoPage = deviceInfoService.page(new Page<>(current, size), queryWrapper);

        // 3. 【核心组装逻辑】将 Entity 转换为 VO，并填充 typeName
        Page<DeviceInfoVO> voPage = new Page<>(current, size, deviceInfoPage.getTotal());
        List<DeviceInfo> records = deviceInfoPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return ResultUtils.success(voPage);
        }

        // a. 提取这一页中所有不重复的 typeId
        Set<Long> typeIdSet = records.stream()
                .map(DeviceInfo::getTypeId)
                .filter(id -> id != null) // 过滤掉空值
                .collect(Collectors.toSet());

        // b. 批量查询字典表，生成 map: {1: "单头机", 2: "双头机"} (此处简化，实际项目中可能需要考虑缓存优化)
        Map<Long, String> typeNameMap = new HashMap<>();
        if (!typeIdSet.isEmpty()) {
            List<DeviceType> deviceTypes = deviceTypeService.listByIds(typeIdSet);
            typeNameMap = deviceTypes.stream().collect(Collectors.toMap(DeviceType::getId, DeviceType::getTypeName));
        }

        // c. 将 Entity 转换为 VO，并赋值 typeName
        Map<Long, String> finalTypeNameMap = typeNameMap;
        List<DeviceInfoVO> voList = records.stream().map(deviceInfo -> {
            DeviceInfoVO vo = new DeviceInfoVO();
            BeanUtils.copyProperties(deviceInfo, vo);
            // 从 map 中获取名称
            vo.setTypeName(finalTypeNameMap.get(deviceInfo.getTypeId()));
            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return ResultUtils.success(voPage);
    }
}
