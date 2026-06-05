package com.en.springbootinit.model.dto.productsku;

import com.en.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductSkuQueryRequest extends PageRequest implements Serializable {
    /**
     * 关联的商品SPU ID (非常核心！通常前端会先查出SPU，然后点“查看规格”传入这个ID查列表)
     */
    private Long productId;

    /**
     * 规格名称 (模糊)
     */
    private String skuName;

    /**
     * SKU 编码 (模糊)
     */
    private String skuCode;

    private static final long serialVersionUID = 1L;
}