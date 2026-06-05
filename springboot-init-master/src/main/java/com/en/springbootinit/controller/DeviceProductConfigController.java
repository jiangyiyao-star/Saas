package com.en.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.en.springbootinit.common.BaseResponse;
import com.en.springbootinit.common.DeleteRequest;
import com.en.springbootinit.common.ErrorCode;
import com.en.springbootinit.common.ResultUtils;
import com.en.springbootinit.exception.BusinessException;
import com.en.springbootinit.model.dto.deviceproductconfig.DeviceProductConfigAddRequest;
import com.en.springbootinit.model.dto.deviceproductconfig.DeviceProductConfigQueryRequest;
import com.en.springbootinit.model.dto.deviceproductconfig.DeviceProductConfigUpdateRequest;
import com.en.springbootinit.model.entity.DeviceInfo;
import com.en.springbootinit.model.entity.DeviceProductConfig;
import com.en.springbootinit.model.entity.ProductSku;
import com.en.springbootinit.service.DeviceInfoService;
import com.en.springbootinit.service.DeviceProductConfigService;
import com.en.springbootinit.service.ProductSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "设备酒头/通道商品挂载配置接口")
@RestController
@RequestMapping("/deviceProductConfig")
public class DeviceProductConfigController {

    @Resource
    private DeviceProductConfigService deviceProductConfigService;

    // 联动校验所需 Service
    @Resource
    private DeviceInfoService deviceInfoService;
    @Resource
    private ProductSkuService productSkuService;

    @ApiOperation("设备酒头挂载商品(上酒)")
    @PostMapping("/add")
    public BaseResponse<Long> addDeviceProductConfig(@RequestBody DeviceProductConfigAddRequest addRequest) {
        if (addRequest == null || !StringUtils.hasText(addRequest.getDeviceSn())
                || addRequest.getChannelNo() == null || addRequest.getSkuId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备SN、通道号、SKUID均不能为空");
        }

        // 1. 校验设备物理机是否存在
        long deviceCount = deviceInfoService.count(new QueryWrapper<DeviceInfo>().eq("device_sn", addRequest.getDeviceSn()));
        if (deviceCount == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "指定的设备不存在，无法挂载商品");
        }

        // 2. 校验售卖的 SKU 是否存在
        ProductSku sku = productSkuService.getById(addRequest.getSkuId());
        if (sku == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "指定的商品规格不存在");
        }

        // 3. 核心校验：同一个设备的同一个酒头，只能挂载一条配置信息 (对应唯一索引 uk_device_channel)
        long existCount = deviceProductConfigService.count(new QueryWrapper<DeviceProductConfig>()
                .eq("device_sn", addRequest.getDeviceSn())
                .eq("channel_no", addRequest.getChannelNo()));
        if (existCount > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,
                    "该设备的 " + addRequest.getChannelNo() + " 号通道已经挂载了商品，请使用更新接口换酒");
        }

        DeviceProductConfig config = new DeviceProductConfig();
        BeanUtils.copyProperties(addRequest, config);

        boolean result = deviceProductConfigService.save(config);
        return ResultUtils.success(config.getId());
    }

    @ApiOperation("修改设备酒头配置(换酒、调价、加库存)")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateDeviceProductConfig(@RequestBody DeviceProductConfigUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "配置ID不能为空");
        }

        // 校验换酒操作时，新 SKU 是否存在
        if (updateRequest.getSkuId() != null) {
            ProductSku sku = productSkuService.getById(updateRequest.getSkuId());
            if (sku == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "更换的新商品规格不存在");
            }
        }

        DeviceProductConfig config = new DeviceProductConfig();
        BeanUtils.copyProperties(updateRequest, config);

        boolean result = deviceProductConfigService.updateById(config);
        return ResultUtils.success(result);
    }

    @ApiOperation("根据ID获取挂载详情")
    @GetMapping("/get")
    public BaseResponse<DeviceProductConfig> getDeviceProductConfigById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DeviceProductConfig config = deviceProductConfigService.getById(id);
        return ResultUtils.success(config);
    }

    @ApiOperation("删除酒头挂载配置(清空通道)")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDeviceProductConfig(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = deviceProductConfigService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @ApiOperation("分页获取设备通道商品挂载列表(常用于查看某台机器卖啥酒)")
    @PostMapping("/list/page")
    public BaseResponse<Page<DeviceProductConfig>> listDeviceProductConfigByPage(@RequestBody DeviceProductConfigQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();

        QueryWrapper<DeviceProductConfig> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(StringUtils.hasText(queryRequest.getDeviceSn()), "device_sn", queryRequest.getDeviceSn());
        queryWrapper.eq(queryRequest.getChannelNo() != null, "channel_no", queryRequest.getChannelNo());
        queryWrapper.eq(queryRequest.getSkuId() != null, "sku_id", queryRequest.getSkuId());

        // 默认按照通道号正序排列，符合物理机器视觉习惯（1号酒头、2号酒头...）
        queryWrapper.orderByAsc("channel_no");

        Page<DeviceProductConfig> page = deviceProductConfigService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(page);
    }
}