package com.en.springbootinit.model.vo;

import com.en.springbootinit.model.entity.DeviceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 设备基础信息视图对象 (专门用于返回给前端展示)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceInfoVO extends DeviceInfo implements Serializable {

    /**
     * 设备类型名称 (关联 device_type 表查出)
     */
    private String typeName;

    /**
     * 商户名称 (预留字段：如果后续有 user/merchant 表，可以将 ownerId 转为 ownerName)
     */
    private String ownerName;

    private static final long serialVersionUID = 1L;
}