package com.en.springbootinit.model.dto.productinfo;

import lombok.Data;
import java.io.Serializable;

@Data
public class ProductInfoUpdateRequest implements Serializable {
    private Long id; // 必传
    private String productCode;
    private String productName;
    private Long brandId;
    private String primaryCategory;
    private String subCategory;
    private Long supplierId;
    private String originPlace;
    private Integer shelfLifeDays;
    private Integer globalStatus; // 1-上架，0-下架
    private String imageUrl;
    private String description;

    private static final long serialVersionUID = 1L;
}