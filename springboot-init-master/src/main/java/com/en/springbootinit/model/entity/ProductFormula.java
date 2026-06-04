package com.en.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商品配方明细表
 * @TableName product_formula
 */
@TableName(value ="product_formula")
@Data
public class ProductFormula {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 售卖商品SKU ID
     */
    private Long skuId;

    /**
     * 消耗原料 ID
     */
    private Long rawMaterialId;

    /**
     * 消耗数量/比例(如一杯500ml值为500)
     */
    private BigDecimal consumeQty;

    /**
     * 消耗单位(建议与基础计量单位一致)
     */
    private String consumeUnit;

    /**
     * 是否为核心原料(1-是，0-否)
     */
    private Integer isCore;

    /**
     * 软删除标记(0-正常，1-已删除)
     */
    private Integer isDeleted;

    /**
     * 配置创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 配置更新时间
     */
    private LocalDateTime updatedAt;
}