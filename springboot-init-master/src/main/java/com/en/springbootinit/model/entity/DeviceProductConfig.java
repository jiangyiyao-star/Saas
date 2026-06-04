package com.en.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 设备通道与商品SKU挂载配置表
 * @TableName device_product_config
 */
@TableName(value ="device_product_config")
@Data
public class DeviceProductConfig {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联设备序列号
     */
    private String deviceSn;

    /**
     * 物理通道号/酒头号(如：1, 2)
     */
    private Integer channelNo;

    /**
     * 关联商品SKU表 ID
     */
    private Long skuId;

    /**
     * 该设备当前的最终售卖价格
     */
    private BigDecimal devicePrice;

    /**
     * 剩余容量/库存(ml，关联配方联动扣减)
     */
    private BigDecimal currentStock;

    /**
     * 配置创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 配置更新时间
     */
    private LocalDateTime updatedAt;
}