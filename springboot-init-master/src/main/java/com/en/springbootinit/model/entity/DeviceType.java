package com.en.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 设备类型基础表
 * @TableName device_type
 */
@TableName(value ="device_type")
@Data
public class DeviceType {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 机型名称（如：双头智能售酒机）
     */
    private String typeName;

    /**
     * 设备标准配置说明
     */
    private String configInfo;

    /**
     * 关键参数模板(JSON格式)
     */
    private Object keyParameters;

    /**
     * 设备分类(如：啤酒机、咖啡机)
     */
    private String category;

    /**
     * 设备编码(用于唯一标识该具体型号)
     */
    private String deviceCode;

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