package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.mapper.DeviceInfoMapper;
import com.en.springbootinit.model.entity.DeviceInfo;
import com.en.springbootinit.service.DeviceInfoService;

import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【device_info(设备基础信息档案表)】的数据库操作Service实现
* @createDate 2026-06-04 10:30:25
*/
@Service
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo>
    implements DeviceInfoService{

}




