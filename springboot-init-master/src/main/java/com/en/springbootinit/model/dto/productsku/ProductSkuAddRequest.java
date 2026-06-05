package com.en.springbootinit.model.dto.productsku;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductSkuAddRequest implements Serializable {
    /**
     * 关联的商品主表SPU ID (必传)
     */
    private Long productId;

    /**
     * 规格名称(如：500ml 10°P 瓶装)
     */
    private String skuName;

    /**
     * SKU 编码(条形码等)
     */
    private String skuCode;

    /**
     * 酒精度/麦汁浓度
     */
    private String alcoholAbv;

    /**
     * 容量/规格(单位：ml)
     */
    private Integer capacity;

    /**
     * 平台指导价
     */
    private BigDecimal basePrice;

    /**
     * 规格图片
     */
    private String skuImageUrl;

    private static final long serialVersionUID = 1L;
}