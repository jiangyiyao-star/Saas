package com.en.springbootinit.model.dto.devicefaultlog;

import com.en.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceFaultLogQueryRequest extends PageRequest implements Serializable {
    /**
     * 设备序列号 (支持模糊查询)
     */
    private String deviceSn;

    /**
     * 故障代码
     */
    private String faultCode;

    /**
     * 严重等级
     */
    private Integer severity;

    /**
     * 处理状态
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}