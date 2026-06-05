package com.en.springbootinit.model.dto.devicefaultlog;

import lombok.Data;
import java.io.Serializable;

@Data
public class DeviceFaultLogUpdateRequest implements Serializable {
    /**
     * 主键ID (必传)
     */
    private Long id;

    /**
     * 处理状态(0-待处理, 1-处理中, 2-已解决)
     */
    private Integer status;

    /**
     * 处理人员(运营或商户)ID
     */
    private Long handlerId;

    /**
     * 处理备注(如：已现场换桶)
     */
    private String resolveRemark;

    private static final long serialVersionUID = 1L;
}