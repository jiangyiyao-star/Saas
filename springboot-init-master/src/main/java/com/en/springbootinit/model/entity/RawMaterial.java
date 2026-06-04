package com.en.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 原料基础信息表
 * @TableName raw_material
 */
@TableName(value ="raw_material")
@Data
public class RawMaterial {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 原料唯一编码
     */
    private String materialCode;

    /**
     * 原料名称(如：青岛原浆生啤液)
     */
    private String materialName;

    /**
     * 原料分类(如：酒水液体、辅助气体)
     */
    private String category;

    /**
     * 归属客户ID (0代表全局; 有值为专供)
     */
    private Long customerId;

    /**
     * 状态(1-正常可用，0-停用下架)
     */
    private Integer status;

    /**
     * 基础计量单位(如：L, ML, KG)
     */
    private String baseUnit;

    /**
     * 原料描述/备注
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