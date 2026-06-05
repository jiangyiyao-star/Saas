package com.en.springbootinit;

import com.en.springbootinit.common.BaseResponse;
import com.en.springbootinit.controller.DeviceFaultLogController;
import com.en.springbootinit.model.entity.DeviceFaultLog;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 主类测试
 *
 * // 修改1
 * // 修改2
 */
@SpringBootTest
class MainApplicationTests {

    // 让 Spring 自动注入 Controller，而不是自己 new
    @Resource
    private DeviceFaultLogController deviceFaultLogController;


}
