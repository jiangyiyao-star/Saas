package com.en.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商品规格表(SKU)
 * @TableName product_sku
 */
@TableName(value ="product_sku")
@Data
public class ProductSku {
    /**
     * SKU主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联的商品主表SPU ID
     */
    private Long productId;

    /**
     * 规格名称(如：500ml 10°P 瓶装)
     */
    private String skuName;

    /**
     * SKU编码(具体条形码或内部码)
     */
    private String skuCode;

    /**
     * 酒精度/麦汁浓度(如：10°P / 4.0%vol)
     */
    private String alcoholAbv;

    /**
     * 容量/规格(单位：ml)
     */
    private Integer capacity;

    /**
     * 平台统一指导价
     */
    private BigDecimal basePrice;

    /**
     * 具体规格图片(可选)
     */
    private String skuImageUrl;

    /**
     * 软删除标记(0-正常，1-已删除)
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}