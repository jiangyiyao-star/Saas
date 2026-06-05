package com.en.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.en.springbootinit.common.BaseResponse;
import com.en.springbootinit.common.DeleteRequest;
import com.en.springbootinit.common.ErrorCode;
import com.en.springbootinit.common.ResultUtils;
import com.en.springbootinit.exception.BusinessException;
import com.en.springbootinit.model.dto.devicetype.DeviceTypeAddRequest;
import com.en.springbootinit.model.dto.devicetype.DeviceTypeQueryRequest;
import com.en.springbootinit.model.dto.devicetype.DeviceTypeUpdateRequest;
import com.en.springbootinit.model.entity.DeviceType;
import com.en.springbootinit.service.DeviceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "设备类型/字典管理接口")
@RestController
@RequestMapping("/deviceType")
public class DeviceTypeController {

    @Resource
    private DeviceTypeService deviceTypeService;

    @ApiOperation("新增设备类型")
    @PostMapping("/add")
    public BaseResponse<Long> addDeviceType(@RequestBody DeviceTypeAddRequest addRequest) {
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 核心校验：设备编码必须唯一
        if (!StringUtils.hasText(addRequest.getDeviceCode())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备编码不能为空");
        }
        long count = deviceTypeService.count(new QueryWrapper<DeviceType>().eq("device_code", addRequest.getDeviceCode()));
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该设备编码已存在，请更换");
        }

        DeviceType deviceType = new DeviceType();
        BeanUtils.copyProperties(addRequest, deviceType);
        boolean result = deviceTypeService.save(deviceType);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "新增失败");
        }
        return ResultUtils.success(deviceType.getId());
    }

    @ApiOperation("修改设备类型")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateDeviceType(@RequestBody DeviceTypeUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ID不能为空");
        }
        // 若修改了编码，需再次查重排除自身
        if (StringUtils.hasText(updateRequest.getDeviceCode())) {
            long count = deviceTypeService.count(new QueryWrapper<DeviceType>()
                    .eq("device_code", updateRequest.getDeviceCode())
                    .ne("id", updateRequest.getId()));
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "修改后的设备编码与其他类型冲突");
            }
        }

        DeviceType deviceType = new DeviceType();
        BeanUtils.copyProperties(updateRequest, deviceType);
        boolean result = deviceTypeService.updateById(deviceType);
        return ResultUtils.success(result);
    }

    @ApiOperation("根据ID获取设备类型详情")
    @GetMapping("/get")
    public BaseResponse<DeviceType> getDeviceTypeById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DeviceType deviceType = deviceTypeService.getById(id);
        if (deviceType == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(deviceType);
    }

    @ApiOperation("删除设备类型")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDeviceType(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // ⚠️ 进阶逻辑(可扩展)：删除前应检查 DeviceInfo 表中是否还有设备在使用该 TypeId，如果有则拒绝删除。
        boolean b = deviceTypeService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @ApiOperation("分页查询设备类型列表")
    @PostMapping("/list/page")
    public BaseResponse<Page<DeviceType>> listDeviceTypeByPage(@RequestBody DeviceTypeQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();

        QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
        // 模糊查询
        queryWrapper.like(StringUtils.hasText(queryRequest.getTypeName()), "type_name", queryRequest.getTypeName());
        queryWrapper.like(StringUtils.hasText(queryRequest.getDeviceCode()), "device_code", queryRequest.getDeviceCode());
        // 精确查询
        queryWrapper.eq(StringUtils.hasText(queryRequest.getCategory()), "category", queryRequest.getCategory());

        // 默认按ID倒序
        queryWrapper.orderByDesc("id");

        Page<DeviceType> deviceTypePage = deviceTypeService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(deviceTypePage);
    }
}