package com.en.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 设备心跳表
 * @TableName device_heartbeat_log
 */
@TableName(value ="device_heartbeat_log")
@Data
public class DeviceHeartbeatLog {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 设备序列号
     */
    private String deviceSn;

    /**
     * 上报时间
     */
    private LocalDateTime reportTime;

    /**
     * 网络信号强度
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

    /**
     * 记录生成时间
     */
    private LocalDateTime createdAt;
}