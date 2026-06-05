package com.en.springbootinit.model.dto.devicetype;

import lombok.Data;
import java.io.Serializable;

@Data
public class DeviceTypeUpdateRequest implements Serializable {
    private Long id;
    private String typeName;
    private String configInfo;
    private String keyParameters;
    private String category;
    private String deviceCode;
    private static final long serialVersionUID = 1L;
}
