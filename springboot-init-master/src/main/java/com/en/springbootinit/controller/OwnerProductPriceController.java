package com.en.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.en.springbootinit.common.BaseResponse;
import com.en.springbootinit.common.DeleteRequest;
import com.en.springbootinit.common.ErrorCode;
import com.en.springbootinit.common.ResultUtils;
import com.en.springbootinit.exception.BusinessException;
import com.en.springbootinit.model.dto.ownerproductprice.OwnerProductPriceAddRequest;
import com.en.springbootinit.model.dto.ownerproductprice.OwnerProductPriceQueryRequest;
import com.en.springbootinit.model.dto.ownerproductprice.OwnerProductPriceUpdateRequest;
import com.en.springbootinit.model.entity.OwnerProductPrice;
import com.en.springbootinit.model.entity.ProductSku;
import com.en.springbootinit.service.OwnerProductPriceService;
import com.en.springbootinit.service.ProductSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "商户商品独立定价管理接口")
@RestController
@RequestMapping("/ownerProductPrice")
public class OwnerProductPriceController {

    @Resource
    private OwnerProductPriceService ownerProductPriceService;

    // 注入 SKU 服务用于校验关联商品是否存在
    @Resource
    private ProductSkuService productSkuService;

    @ApiOperation("商户新增商品定价配置")
    @PostMapping("/add")
    public BaseResponse<Long> addOwnerProductPrice(@RequestBody OwnerProductPriceAddRequest addRequest) {
        if (addRequest == null || addRequest.getOwnerId() == null || addRequest.getSkuId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "商户ID和SKUID不能为空");
        }
        if (addRequest.getRetailPrice() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "必须设定售卖单价");
        }

        // 1. 校验SKU是否存在
        ProductSku sku = productSkuService.getById(addRequest.getSkuId());
        if (sku == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该商品规格(SKU)不存在，无法定价");
        }

        // 2. 校验唯一性：同一个商户对同一个SKU只能有一条定价记录 (对应数据库的 uk_owner_sku)
        long count = ownerProductPriceService.count(new QueryWrapper<OwnerProductPrice>()
                .eq("owner_id", addRequest.getOwnerId())
                .eq("sku_id", addRequest.getSkuId()));
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该商户已对该商品进行了定价，请直接修改原有价格");
        }

        OwnerProductPrice ownerProductPrice = new OwnerProductPrice();
        BeanUtils.copyProperties(addRequest, ownerProductPrice);

        // 如果未传状态，默认上架
        if (ownerProductPrice.getOwnerStatus() == null) {
            ownerProductPrice.setOwnerStatus(1);
        }

        boolean result = ownerProductPriceService.save(ownerProductPrice);
        return ResultUtils.success(ownerProductPrice.getId());
    }

    @ApiOperation("商户修改商品定价或上下架状态")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateOwnerProductPrice(@RequestBody OwnerProductPriceUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "定价记录ID不能为空");
        }

        OwnerProductPrice ownerProductPrice = new OwnerProductPrice();
        BeanUtils.copyProperties(updateRequest, ownerProductPrice);

        boolean result = ownerProductPriceService.updateById(ownerProductPrice);
        return ResultUtils.success(result);
    }

    @ApiOperation("根据ID获取商户定价详情")
    @GetMapping("/get")
    public BaseResponse<OwnerProductPrice> getOwnerProductPriceById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OwnerProductPrice ownerProductPrice = ownerProductPriceService.getById(id);
        return ResultUtils.success(ownerProductPrice);
    }

    @ApiOperation("删除商户定价配置")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOwnerProductPrice(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = ownerProductPriceService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @ApiOperation("分页获取商户定价列表(常用于商户后台展示商品库)")
    @PostMapping("/list/page")
    public BaseResponse<Page<OwnerProductPrice>> listOwnerProductPriceByPage(@RequestBody OwnerProductPriceQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();

        QueryWrapper<OwnerProductPrice> queryWrapper = new QueryWrapper<>();
        // 全是精确查找，因为针对关联关系
        queryWrapper.eq(queryRequest.getOwnerId() != null, "owner_id", queryRequest.getOwnerId());
        queryWrapper.eq(queryRequest.getSkuId() != null, "sku_id", queryRequest.getSkuId());
        queryWrapper.eq(queryRequest.getOwnerStatus() != null, "owner_status", queryRequest.getOwnerStatus());

        // 按更新时间倒序排列，优先展示最近调价的商品
        queryWrapper.orderByDesc("updated_at");

        Page<OwnerProductPrice> page = ownerProductPriceService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(page);
    }
}