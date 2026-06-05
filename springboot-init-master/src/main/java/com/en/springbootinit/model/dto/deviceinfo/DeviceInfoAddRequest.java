package com.en.springbootinit.model.dto.deviceinfo;

import lombok.Data;
import java.io.Serializable;

@Data
public class DeviceInfoAddRequest implements Serializable {
    /**
     * 设备唯一序列号/出厂ID
     */
    private String deviceSn;

    /**
     * 设备自定义名称
     */
    private String deviceName;

    /**
     * 关联的设备类型ID
     */
    private Long typeId;

    /**
     * 归属店主/商户ID (可以后续绑定，新增时可为空)
     */
    private Long ownerId;

    /**
     * 详细部署物理地址
     */
    private String address;

    /**
     * 设备参数(JSON字符串)
     */
    private String customParameters;

    private static final long serialVersionUID = 1L;
}