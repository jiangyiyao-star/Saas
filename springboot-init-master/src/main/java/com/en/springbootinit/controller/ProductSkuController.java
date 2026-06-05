package com.en.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.en.springbootinit.common.BaseResponse;
import com.en.springbootinit.common.DeleteRequest;
import com.en.springbootinit.common.ErrorCode;
import com.en.springbootinit.common.ResultUtils;
import com.en.springbootinit.exception.BusinessException;
import com.en.springbootinit.model.dto.productsku.ProductSkuAddRequest;
import com.en.springbootinit.model.dto.productsku.ProductSkuQueryRequest;
import com.en.springbootinit.model.dto.productsku.ProductSkuUpdateRequest;
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

@Api(tags = "商品规格明细(SKU)管理接口")
@RestController
@RequestMapping("/productSku")
public class ProductSkuController {

    @Resource
    private ProductSkuService productSkuService;

    // 注入 SPU 服务用于校验
    @Resource
    private ProductInfoService productInfoService;

    @ApiOperation("新增商品规格(SKU)")
    @PostMapping("/add")
    public BaseResponse<Long> addProductSku(@RequestBody ProductSkuAddRequest addRequest) {
        if (addRequest == null || addRequest.getProductId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "关联的商品主表ID不能为空");
        }

        // 校验关联的 SPU 是否存在
        ProductInfo productInfo = productInfoService.getById(addRequest.getProductId());
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "关联的商品主体(SPU)不存在");
        }

        // 校验 SKU 编码唯一性 (如果填了的话)
        if (StringUtils.hasText(addRequest.getSkuCode())) {
            long count = productSkuService.count(new QueryWrapper<ProductSku>().eq("sku_code", addRequest.getSkuCode()));
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "该SKU编码已存在");
            }
        }

        ProductSku productSku = new ProductSku();
        BeanUtils.copyProperties(addRequest, productSku);

        boolean result = productSkuService.save(productSku);
        return ResultUtils.success(productSku.getId());
    }

    @ApiOperation("修改商品规格(SKU)")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateProductSku(@RequestBody ProductSkuUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "规格ID不能为空");
        }

        // 校验 SKU 编码唯一性排除自身
        if (StringUtils.hasText(updateRequest.getSkuCode())) {
            long count = productSkuService.count(new QueryWrapper<ProductSku>()
                    .eq("sku_code", updateRequest.getSkuCode())
                    .ne("id", updateRequest.getId()));
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "SKU编码与其他规格冲突");
            }
        }

        ProductSku productSku = new ProductSku();
        BeanUtils.copyProperties(updateRequest, productSku);
        boolean result = productSkuService.updateById(productSku);
        return ResultUtils.success(result);
    }

    @ApiOperation("根据ID获取规格详情")
    @GetMapping("/get")
    public BaseResponse<ProductSku> getProductSkuById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ProductSku productSku = productSkuService.getById(id);
        if (productSku == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(productSku);
    }

    @ApiOperation("删除规格(逻辑删除)")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteProductSku(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // ⚠️ 进阶业务提示：真实生产环境中，如果这个 SKU 正在被 `device_product_config` (设备通道) 或者 `owner_product_price` 引用，需要拦截删除。
        boolean b = productSkuService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @ApiOperation("分页查询规格列表(常用于查某个SPU下的所有规格)")
    @PostMapping("/list/page")
    public BaseResponse<Page<ProductSku>> listProductSkuByPage(@RequestBody ProductSkuQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();

        QueryWrapper<ProductSku> queryWrapper = new QueryWrapper<>();
        // 核心：按商品主表 ID 筛选
        queryWrapper.eq(queryRequest.getProductId() != null, "product_id", queryRequest.getProductId());

        queryWrapper.like(StringUtils.hasText(queryRequest.getSkuName()), "sku_name", queryRequest.getSkuName());
        queryWrapper.like(StringUtils.hasText(queryRequest.getSkuCode()), "sku_code", queryRequest.getSkuCode());

        // 按创建时间倒序
        queryWrapper.orderByDesc("created_at");

        Page<ProductSku> productSkuPage = productSkuService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(productSkuPage);
    }
}