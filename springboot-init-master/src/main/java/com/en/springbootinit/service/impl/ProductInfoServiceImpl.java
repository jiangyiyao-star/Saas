package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.mapper.ProductInfoMapper;
import com.en.springbootinit.model.entity.ProductInfo;
import com.en.springbootinit.service.ProductInfoService;

import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【product_info(全局商品主表(SPU))】的数据库操作Service实现
* @createDate 2026-06-04 10:30:25
*/
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo>
    implements ProductInfoService{

}




