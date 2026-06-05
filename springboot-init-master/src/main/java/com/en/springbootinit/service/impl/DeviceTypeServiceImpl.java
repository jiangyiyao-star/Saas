package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.mapper.DeviceTypeMapper;
import com.en.springbootinit.model.entity.DeviceType;
import com.en.springbootinit.service.DeviceTypeService;

import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【device_type(设备类型基础表)】的数据库操作Service实现
* @createDate 2026-06-04 10:30:25
*/
@Service
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType>
    implements DeviceTypeService{

}




