package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.mapper.OwnerProductPriceMapper;
import com.en.springbootinit.model.entity.OwnerProductPrice;
import com.en.springbootinit.service.OwnerProductPriceService;

import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【owner_product_price(商户商品定价表)】的数据库操作Service实现
* @createDate 2026-06-04 10:30:25
*/
@Service
public class OwnerProductPriceServiceImpl extends ServiceImpl<OwnerProductPriceMapper, OwnerProductPrice>
    implements OwnerProductPriceService{

}




