package com.en.springbootinit.model.dto.ownerproductprice;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OwnerProductPriceAddRequest implements Serializable {
    /**
     * 所属厂商/商户ID (必传)
     */
    private Long ownerId;

    /**
     * 关联的商品规格(SKU) ID (必传)
     */
    private Long skuId;

    /**
     * 该商户设定的实际售卖单价
     */
    private BigDecimal retailPrice;

    /**
     * 独立上下架状态(1-上架，0-下架)
     */
    private Integer ownerStatus;

    private static final long serialVersionUID = 1L;
}