package com.en.springbootinit.model.dto.ownerproductprice;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OwnerProductPriceUpdateRequest implements Serializable {
    /**
     * 定价记录主键ID (必传)
     */
    private Long id;

    /**
     * 实际售卖单价 (更新价格)
     */
    private BigDecimal retailPrice;

    /**
     * 独立上下架状态(1-上架，0-下架)
     */
    private Integer ownerStatus;

    private static final long serialVersionUID = 1L;
}