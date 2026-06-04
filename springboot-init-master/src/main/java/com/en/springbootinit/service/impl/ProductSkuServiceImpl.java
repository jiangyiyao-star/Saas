package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.model.entity.ProductSku;
import com.en.springbootinit.service.ProductSkuService;
import generator.mapper.ProductSkuMapper;
import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【product_sku(商品规格表(SKU))】的数据库操作Service实现
* @createDate 2026-06-04 10:30:25
*/
@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku>
    implements ProductSkuService{

}




