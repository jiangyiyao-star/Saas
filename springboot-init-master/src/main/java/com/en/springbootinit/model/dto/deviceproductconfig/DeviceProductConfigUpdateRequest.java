package com.en.springbootinit.model.dto.deviceproductconfig;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DeviceProductConfigUpdateRequest implements Serializable {
    /**
     * 主键ID (必传)
     */
    private Long id;

    /**
     * 换酒：关联的商品(SKU) ID
     */
    private Long skuId;

    /**
     * 调价：该设备当前的最终售卖价格
     */
    private BigDecimal devicePrice;

    /**
     * 补货：当前剩余库存(ml)
     */
    private BigDecimal currentStock;

    private static final long serialVersionUID = 1L;
}
