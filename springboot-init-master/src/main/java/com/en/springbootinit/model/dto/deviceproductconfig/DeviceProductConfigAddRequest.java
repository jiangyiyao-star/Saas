package com.en.springbootinit.model.dto.deviceproductconfig;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DeviceProductConfigAddRequest implements Serializable {
    /**
     * 关联设备序列号 (必传)
     */
    private String deviceSn;

    /**
     * 物理通道号/酒头号(如：1, 2) (必传)
     */
    private Integer channelNo;

    /**
     * 关联的商品(SKU) ID (必传)
     */
    private Long skuId;

    /**
     * 该设备当前的最终售卖价格
     */
    private BigDecimal devicePrice;

    /**
     * 当前剩余容量/库存(ml)
     */
    private BigDecimal currentStock;

    private static final long serialVersionUID = 1L;
}