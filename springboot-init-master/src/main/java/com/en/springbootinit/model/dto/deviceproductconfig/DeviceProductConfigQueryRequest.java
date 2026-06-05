package com.en.springbootinit.model.dto.deviceproductconfig;

import com.en.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceProductConfigQueryRequest extends PageRequest implements Serializable {
    /**
     * 关联设备序列号 (精确查询某台设备的所有酒头)
     */
    private String deviceSn;

    /**
     * 物理通道号/酒头号
     */
    private Integer channelNo;

    /**
     * 关联商品SKU ID (查询哪些机器在卖这款酒)
     */
    private Long skuId;

    private static final long serialVersionUID = 1L;
}