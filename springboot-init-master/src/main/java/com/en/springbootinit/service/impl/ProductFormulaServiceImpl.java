package com.en.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.en.springbootinit.model.entity.ProductFormula;
import com.en.springbootinit.service.ProductFormulaService;
import generator.mapper.ProductFormulaMapper;
import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【product_formula(商品配方明细表)】的数据库操作Service实现
* @createDate 2026-06-04 10:30:25
*/
@Service
public class ProductFormulaServiceImpl extends ServiceImpl<ProductFormulaMapper, ProductFormula>
    implements ProductFormulaService{

}




