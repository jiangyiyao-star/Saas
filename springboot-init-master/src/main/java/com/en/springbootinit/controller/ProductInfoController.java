package com.en.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.en.springbootinit.common.BaseResponse;
import com.en.springbootinit.common.DeleteRequest;
import com.en.springbootinit.common.ErrorCode;
import com.en.springbootinit.common.ResultUtils;
import com.en.springbootinit.exception.BusinessException;
import com.en.springbootinit.model.dto.productinfo.ProductInfoAddRequest;
import com.en.springbootinit.model.dto.productinfo.ProductInfoQueryRequest;
import com.en.springbootinit.model.dto.productinfo.ProductInfoUpdateRequest;
import com.en.springbootinit.model.entity.ProductInfo;
import com.en.springbootinit.model.entity.ProductSku;
import com.en.springbootinit.service.ProductInfoService;
import com.en.springbootinit.service.ProductSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Api(tags = "商品主表(SPU)管理接口")
@RestController
@RequestMapping("/productInfo")
public class ProductInfoController {

    @Resource
    private ProductInfoService productInfoService;

    // 注入 SKU Service 用于联动校验
    @Resource
    private ProductSkuService productSkuService;

    @ApiOperation("新增商品主表(SPU)")
    @PostMapping("/add")
    public BaseResponse<Long> addProductInfo(@RequestBody ProductInfoAddRequest addRequest) {
        if (addRequest == null || !StringUtils.hasText(addRequest.getProductCode())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品编码不能为空");
        }

        // 校验全局商品编码是否唯一
        long count = productInfoService.count(new QueryWrapper<ProductInfo>().eq("product_code", addRequest.getProductCode()));
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该商品编码已存在");
        }

        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(addRequest, productInfo);
        productInfo.setGlobalStatus(0); // 默认新增后处于下架状态，需手动上架

        boolean result = productInfoService.save(productInfo);
        return ResultUtils.success(productInfo.getId());
    }

    @ApiOperation("修改商品主表信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateProductInfo(@RequestBody ProductInfoUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ID不能为空");
        }

        // 如果修改了编码，排除自身查重
        if (StringUtils.hasText(updateRequest.getProductCode())) {
            long count = productInfoService.count(new QueryWrapper<ProductInfo>()
                    .eq("product_code", updateRequest.getProductCode())
                    .ne("id", updateRequest.getId()));
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "修改后的商品编码与其他商品冲突");
            }
        }

        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(updateRequest, productInfo);

        // 规范执行：如果业务中有自定义时间字段需要更新，严格使用 LocalDateTime.now()
        // productInfo.setPublishTime(LocalDateTime.now());

        boolean result = productInfoService.updateById(productInfo);
        return ResultUtils.success(result);
    }

    @ApiOperation("根据ID获取商品主表详情")
    @GetMapping("/get")
    public BaseResponse<ProductInfo> getProductInfoById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ProductInfo productInfo = productInfoService.getById(id);
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(productInfo);
    }

    @ApiOperation("删除商品主表(SPU)")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteProductInfo(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // ⚠️【企业级联动拦截】：如果该商品下还有 SKU，绝对不允许直接删除 SPU
        long skuCount = productSkuService.count(new QueryWrapper<ProductSku>().eq("product_id", deleteRequest.getId()));
        if (skuCount > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该商品下存在关联的规格(SKU)，请先删除或转移SKU");
        }

        boolean b = productInfoService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @ApiOperation("分页查询商品主表(SPU)")
    @PostMapping("/list/page")
    public BaseResponse<Page<ProductInfo>> listProductInfoByPage(@RequestBody ProductInfoQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();

        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        // 严格使用 Spring 的 StringUtils.hasText
        queryWrapper.like(StringUtils.hasText(queryRequest.getProductCode()), "product_code", queryRequest.getProductCode());
        queryWrapper.like(StringUtils.hasText(queryRequest.getProductName()), "product_name", queryRequest.getProductName());
        queryWrapper.eq(StringUtils.hasText(queryRequest.getPrimaryCategory()), "primary_category", queryRequest.getPrimaryCategory());
        queryWrapper.eq(StringUtils.hasText(queryRequest.getSubCategory()), "sub_category", queryRequest.getSubCategory());
        queryWrapper.eq(queryRequest.getGlobalStatus() != null, "global_status", queryRequest.getGlobalStatus());

        queryWrapper.orderByDesc("created_at");

        Page<ProductInfo> productInfoPage = productInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(productInfoPage);
    }
}