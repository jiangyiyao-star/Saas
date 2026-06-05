package com.en.springbootinit.model.dto.deviceinfo;

import lombok.Data;
import java.io.Serializable;

@Data
public class DeviceInfoUpdateRequest implements Serializable {
    /**
     * 主键ID (必传)
     */
    private Long id;

    /**
     * 设备自定义名称
     */
    private String deviceName;

    /**
     * 归属店主/商户ID (用于设备换绑)
     */
    private Long ownerId;

    /**
     * 设备状态(0-禁用, 1-在线, 2-离线, 3-故障)
     */
    private Integer status;

    /**
     * 详细部署物理地址
     */
    private String address;

    private static final long serialVersionUID = 1L;
}