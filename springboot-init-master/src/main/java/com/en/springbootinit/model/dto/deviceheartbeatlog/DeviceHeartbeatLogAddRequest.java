package com.en.springbootinit.model.dto.deviceheartbeatlog;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DeviceHeartbeatLogAddRequest implements Serializable {
    /**
     * 上报心跳的设备序列号
     */
    private String deviceSn;

    /**
     * 网络信号强度(0-100)
     */
    private Integer signalStrength;

    /**
     * 设备核心温度
     */
    private BigDecimal temperature;

    /**
     * 上报时的IP地址
     */
    private String ipAddress;

    private static final long serialVersionUID = 1L;
}