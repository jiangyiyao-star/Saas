package com.en.springbootinit.model.dto.devicetype;

import com.en.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceTypeQueryRequest extends PageRequest implements Serializable {
    /**
     * 机型名称 (支持模糊查询)
     */
    private String typeName;

    /**
     * 设备分类 (精确查询，用于下拉框筛选)
     */
    private String category;

    /**
     * 设备编码 (支持模糊查询)
     */
    private String deviceCode;

    private static final long serialVersionUID = 1L;
}
