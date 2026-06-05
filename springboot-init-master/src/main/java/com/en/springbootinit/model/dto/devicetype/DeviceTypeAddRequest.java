package com.en.springbootinit.model.dto.devicetype;

import lombok.Data;
import java.io.Serializable;

@Data
public class DeviceTypeAddRequest implements Serializable {
    /**
     * 机型名称（如：双头智能售酒机）
     */
    private String typeName;

    /**
     * 设备标准配置说明
     */
    private String configInfo;

    /**
     * 关键参数模板(JSON格式字符串)
     */
    private String keyParameters;

    /**
     * 设备分类(如：啤酒机、咖啡机)
     */
    private String category;

    /**
     * 设备编码(如：BEER-M-V1)
     */
    private String deviceCode;

    private static final long serialVersionUID = 1L;
}