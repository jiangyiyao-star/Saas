package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.mapper.DeviceProductConfigMapper;
import com.en.springbootinit.model.entity.DeviceProductConfig;
import com.en.springbootinit.service.DeviceProductConfigService;

import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【device_product_config(设备通道与商品SKU挂载配置表)】的数据库操作Service实现
* @createDate 2026-06-04 10:30:25
*/
@Service
public class DeviceProductConfigServiceImpl extends ServiceImpl<DeviceProductConfigMapper, DeviceProductConfig>
    implements DeviceProductConfigService{

}




