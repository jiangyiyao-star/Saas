package com.en.springbootinit.model.dto.deviceinfo;

import com.en.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 设备序列号 (模糊查询)
     */
    private String deviceSn;

    /**
     * 设备名称 (模糊查询)
     */
    private String deviceName;

    /**
     * 设备状态
     */
    private Integer status;

    /**
     * 归属商户ID
     */
    private Long ownerId;

    /**
     * IP 地址 (精确查询)
     */
    private String ipAddress;

    /**
     * MAC 地址 (精确查询)
     */
    private String macAddress;

    /**
     * 设备类型 ID (原型图顶部的“类型”下拉框)
     */
    private Long typeId;

    // ================= 地图范围筛选参数 =================
    /**
     * 最小经度 (地图左下角)
     */
    private Double minLng;

    /**
     * 最大经度 (地图右上角)
     */
    private Double maxLng;

    /**
     * 最小纬度 (地图左下角)
     */
    private Double minLat;

    /**
     * 最大纬度 (地图右上角)
     */
    private Double maxLat;

    private static final long serialVersionUID = 1L;
}