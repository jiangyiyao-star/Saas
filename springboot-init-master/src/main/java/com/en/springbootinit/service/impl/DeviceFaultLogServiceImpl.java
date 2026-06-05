package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.model.entity.DeviceFaultLog;
import com.en.springbootinit.mapper.DeviceFaultLogMapper; // 【重点修正】导入刚才改好的 Mapper
import com.en.springbootinit.service.DeviceFaultLogService;
import org.springframework.stereotype.Service;

@Service
public class DeviceFaultLogServiceImpl extends ServiceImpl<DeviceFaultLogMapper, DeviceFaultLog>
        implements DeviceFaultLogService {

}