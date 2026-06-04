package com.en.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 全局商品主表(SPU)
 * @TableName product_info
 */
@TableName(value ="product_info")
@Data
public class ProductInfo {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 全局商品编码(系统内部唯一识别)
     */
    private String productCode;

    /**
     * 商品名称(如：青岛经典生啤)
     */
    private String productName;

    /**
     * 品牌ID (这里简写，实际可关联brand表)
     */
    private Long brandId;

    /**
     * 一级分类(如：酒水饮料、生鲜)
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
     * 全局上下架状态(1-上架，0-下架)
     */
    private Integer globalStatus;

    /**
     * 商品SPU主图URL
     */
    private String imageUrl;

    /**
     * 商品描述/卖点介绍
     */
    private String description;

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