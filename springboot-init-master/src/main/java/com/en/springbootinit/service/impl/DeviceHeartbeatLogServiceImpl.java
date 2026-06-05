package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.mapper.DeviceHeartbeatLogMapper;
import com.en.springbootinit.model.entity.DeviceHeartbeatLog;
import com.en.springbootinit.service.DeviceHeartbeatLogService;

import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【device_heartbeat_log(设备心跳表)】的数据库操作Service实现
* @createDate 2026-06-04 10:30:13
*/
@Service
public class DeviceHeartbeatLogServiceImpl extends ServiceImpl<DeviceHeartbeatLogMapper, DeviceHeartbeatLog>
    implements DeviceHeartbeatLogService{

}




