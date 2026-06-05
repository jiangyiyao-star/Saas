package com.en.springbootinit.model.dto.productinfo;

import com.en.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 商品编码 (模糊查询)
     */
    private String productCode;

    /**
     * 商品名称 (模糊查询)
     */
    private String productName;

    /**
     * 一级分类 (精确筛选)
     */
    private String primaryCategory;

    /**
     * 二级分类 (精确筛选)
     */
    private String subCategory;

    /**
     * 全局上下架状态
     */
    private Integer globalStatus;

    private static final long serialVersionUID = 1L;
}