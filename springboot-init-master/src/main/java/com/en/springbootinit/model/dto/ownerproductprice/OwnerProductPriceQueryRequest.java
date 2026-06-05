package com.en.springbootinit.model.dto.ownerproductprice;

import com.en.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class OwnerProductPriceQueryRequest extends PageRequest implements Serializable {
    /**
     * 所属商户ID (精准查询)
     */
    private Long ownerId;

    /**
     * 关联的商品(SKU) ID (精准查询)
     */
    private Long skuId;

    /**
     * 上下架状态
     */
    private Integer ownerStatus;

    private static final long serialVersionUID = 1L;
}