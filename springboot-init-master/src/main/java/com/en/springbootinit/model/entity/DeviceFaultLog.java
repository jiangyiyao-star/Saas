package com.en.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 设备故障表
 * @TableName device_fault_log
 */
@TableName(value ="device_fault_log")
@Data
public class DeviceFaultLog {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 设备序列号
     */
    private String deviceSn;

    /**
     * 硬件故障代码
     */
    private String faultCode;

    /**
     * 故障详细描述
     */
    private String faultDesc;

    /**
     * 严重等级(1-提示, 2-一般, 3-严重)
     */
    private Integer severity;

    /**
     * 处理状态(0-待处理, 1-处理中, 2-已解决)
     */
    private Integer status;

    /**
     * 故障发生时间
     */
    private LocalDateTime occurTime;

    /**
     * 故障解决时间
     */
    private LocalDateTime resolveTime;

    /**
     * 处理人员ID
     */
    private Long handlerId;

    /**
     * 处理备注
     */
    private String resolveRemark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}