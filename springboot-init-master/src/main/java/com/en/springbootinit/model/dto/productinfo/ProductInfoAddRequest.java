package com.en.springbootinit.model.dto.productinfo;

import lombok.Data;
import java.io.Serializable;

@Data
public class ProductInfoAddRequest implements Serializable {
    /**
     * 全局商品编码(系统内部唯一识别)
     */
    private String productCode;

    /**
     * 商品名称(如：青岛经典生啤)
     */
    private String productName;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 一级分类(如：酒水饮料)
     */
    private String primaryCategory;

    /**
     * 二级分类(如：黄啤、黑啤)
     */
    private String subCategory;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 产地(如：山东青岛)
     */
    private String originPlace;

    /**
     * 保质期(天)
     */
    private Integer shelfLifeDays;

    /**
     * SPU主图URL
     */
    private String imageUrl;

    /**
     * 商品描述/卖点介绍
     */
    private String description;

    private static final long serialVersionUID = 1L;
}