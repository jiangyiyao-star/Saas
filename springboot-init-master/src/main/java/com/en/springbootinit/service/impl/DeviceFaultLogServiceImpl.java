package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.model.entity.DeviceFaultLog;
import com.en.springbootinit.service.DeviceFaultLogService;
import generator.mapper.DeviceFaultLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【device_fault_log(设备故障表)】的数据库操作Service实现
* @createDate 2026-06-04 10:30:05
*/
@Service
public class DeviceFaultLogServiceImpl extends ServiceImpl<DeviceFaultLogMapper, DeviceFaultLog>
    implements DeviceFaultLogService{

}




