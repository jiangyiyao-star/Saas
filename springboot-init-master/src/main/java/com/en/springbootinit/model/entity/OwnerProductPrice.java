package com.en.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商户商品定价表
 * @TableName owner_product_price
 */
@TableName(value ="owner_product_price")
@Data
public class OwnerProductPrice {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 厂商/商户ID
     */
    private Long ownerId;

    /**
     * 关联商品(SKU) ID
     */
    private Long skuId;

    /**
     * 实际售卖单价
     */
    private BigDecimal retailPrice;

    /**
     * 独立上下架状态(1-上架，0-下架)
     */
    private Integer ownerStatus;

    /**
     * 定价创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 价格更新时间
     */
    private LocalDateTime updatedAt;
}