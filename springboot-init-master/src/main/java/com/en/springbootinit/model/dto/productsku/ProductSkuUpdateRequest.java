package com.en.springbootinit.model.dto.productsku;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductSkuUpdateRequest implements Serializable {
    private Long id; // 必传
    private String skuName;
    private String skuCode;
    private String alcoholAbv;
    private Integer capacity;
    private BigDecimal basePrice;
    private String skuImageUrl;
    private static final long serialVersionUID = 1L;
}