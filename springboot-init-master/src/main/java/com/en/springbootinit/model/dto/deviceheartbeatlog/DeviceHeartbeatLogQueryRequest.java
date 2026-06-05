package com.en.springbootinit.model.dto.deviceheartbeatlog;

import com.en.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceHeartbeatLogQueryRequest extends PageRequest implements Serializable {
    /**
     * 设备序列号 (精准查询)
     */
    private String deviceSn;

    /**
     * IP地址 (模糊或精确均可)
     */
    private String ipAddress;

    /**
     * 查询起始时间 (如：2026-06-04 00:00:00)
     */
    private Date startTime;

    /**
     * 查询结束时间 (如：2026-06-04 23:59:59)
     */
    private Date endTime;

    private static final long serialVersionUID = 1L;
}