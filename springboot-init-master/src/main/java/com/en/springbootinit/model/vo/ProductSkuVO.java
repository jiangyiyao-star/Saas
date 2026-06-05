package com.en.springbootinit.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.en.springbootinit.model.entity.DeviceInfo;
import com.en.springbootinit.model.entity.ProductSku;
import lombok.Data;


@Data
public class ProductSkuVO extends ProductSku implements Serializable {
    private String productName; // 冗余展示商品主名
}