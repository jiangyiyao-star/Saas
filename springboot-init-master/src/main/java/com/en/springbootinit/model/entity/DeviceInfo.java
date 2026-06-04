package com.en.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 设备基础信息档案表
 * @TableName device_info
 */
@TableName(value ="device_info")
@Data
public class DeviceInfo {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 设备唯一序列号/出厂ID
     */
    private String deviceSn;

    /**
     * 设备自定义名称
     */
    private String deviceName;

    /**
     * 关联的设备类型ID (device_type.id)
     */
    private Long typeId;

    /**
     * 归属店主/商户ID
     */
    private Long ownerId;

    /**
     * 设备状态(0-禁用, 1-在线, 2-离线, 3-故障)
     */
    private Integer status;

    /**
     * 经度 (地图分布展示用)
     */
    private BigDecimal longitude;

    /**
     * 纬度 (地图分布展示用)
     */
    private BigDecimal latitude;

    /**
     * 详细部署物理地址(可由经纬度或IP解析获取)
     */
    private String address;

    /**
     * IP地址 (通过心跳上报获取)
     */
    private String ipAddress;

    /**
     * MAC地址 (设备物理网卡地址)
     */
    private String macAddress;

    /**
     * 设备参数(JSON)
     */
    private Object customParameters;

    /**
     * 最后一次心跳时间
     */
    private LocalDateTime lastHeartbeat;

    /**
     * 软删除标记(0-正常，1-已删除)
     */
    private Integer isDeleted;

    /**
     * 录入时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}