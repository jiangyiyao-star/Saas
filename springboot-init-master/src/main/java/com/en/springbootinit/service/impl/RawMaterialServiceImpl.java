package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.mapper.RawMaterialMapper;
import com.en.springbootinit.model.entity.RawMaterial;
import com.en.springbootinit.service.RawMaterialService;

import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【raw_material(原料基础信息表)】的数据库操作Service实现
* @createDate 2026-06-04 10:30:25
*/
@Service
public class RawMaterialServiceImpl extends ServiceImpl<RawMaterialMapper, RawMaterial>
    implements RawMaterialService{

}




