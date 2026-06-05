package com.en.springbootinit.model.dto.devicefaultlog;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class DeviceFaultLogAddRequest implements Serializable {
    /**
     * 发生告警的设备序列号
     */
    private String deviceSn;

    /**
     * 系统/硬件故障代码
     */
    private String faultCode;

    /**
     * 告警/故障详细描述
     */
    private String faultDesc;

    /**
     * 严重等级(1-提示, 2-一般, 3-严重)
     */
    private Integer severity;

    /**
     * 故障发生时间 (前端可传，不传则后端默认当前时间)
     */
    private Date occurTime;

    private static final long serialVersionUID = 1L;
}