package com.en.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.en.springbootinit.model.entity.DeviceType;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 设备类型视图
 *
 * // 修改1
 * //修改二
 */
@Data
public class DeviceTypeVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param deviceTypeVO
     * @return
     */
    public static DeviceType voToObj(DeviceTypeVO deviceTypeVO) {
        if (deviceTypeVO == null) {
            return null;
        }
        DeviceType deviceType = new DeviceType();
        BeanUtils.copyProperties(deviceTypeVO, deviceType);
        List<String> tagList = deviceTypeVO.getTagList();
        deviceType.setTags(JSONUtil.toJsonStr(tagList));
        return deviceType;
    }

    /**
     * 对象转封装类
     *
     * @param deviceType
     * @return
     */
    public static DeviceTypeVO objToVo(DeviceType deviceType) {
        if (deviceType == null) {
            return null;
        }
        DeviceTypeVO deviceTypeVO = new DeviceTypeVO();
        BeanUtils.copyProperties(deviceType, deviceTypeVO);
        deviceTypeVO.setTagList(JSONUtil.toList(deviceType.getTags(), String.class));
        return deviceTypeVO;
    }
}
