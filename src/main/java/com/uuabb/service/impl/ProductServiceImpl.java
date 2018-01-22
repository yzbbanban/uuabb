package com.uuabb.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.uuabb.common.Const;
import com.uuabb.common.ResponseCode;
import com.uuabb.common.ServerResponse;
import com.uuabb.dao.CategoryMapper;
import com.uuabb.dao.ProductMapper;
import com.uuabb.pojo.Category;
import com.uuabb.pojo.Product;
import com.uuabb.pojo.User;
import com.uuabb.pojo.vo.ProductDetailVo;
import com.uuabb.pojo.vo.ProductListVo;
import com.uuabb.service.ICategoryService;
import com.uuabb.service.IProductService;

import com.uuabb.util.DateTimeUtil;
import com.uuabb.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brander on 2017/12/19
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product == null) {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] imgs = product.getSubImages().split(",");
            if (imgs.length > 0) {
                String mainImage = imgs[0];
                product.setMainImage(mainImage);
            }
        }
        if (product.getId() == null) {//insert
            int resultCount = productMapper.insert(product);
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMsg("新建产品成功");
            } else {
                return ServerResponse.createByErrorMsg("新建产品失败");
            }
        } else {//更新
            int resultCount = productMapper.updateByPrimaryKey(product);
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMsg("新建产品成功");
            } else {
                return ServerResponse.createByErrorMsg("新建产品失败");
            }
        }

    }

    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMsg("更新成功");
        }
        return ServerResponse.createByErrorMsg("更新失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILEGAL_ARGUMENT.getCode(), ResponseCode.ILEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("商品已下架");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);


        return ServerResponse.createBySuccess(productDetailVo);
    }


    @Override
    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
        //start page
        PageHelper.startPage(pageNum, pageSize);
        //填充查询逻辑
        List<Product> products = productMapper.selectProductList();
        List<ProductListVo> productListVos = Lists.newArrayList();
        for (Product p : products) {
            ProductListVo productListVo = assembleProductListVo(p);
            productListVos.add(productListVo);
        }
        //pageHelper 收尾
        //使用 products 进行分页
        PageInfo pageInfo = new PageInfo(products);
        //使用 vos 来展示
        pageInfo.setList(productListVos);

        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {//模糊搜索
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        //搜索产品
        List<Product> products = productMapper.selectByNameAndProductId(productId, productName);
        //界面显示类
        List<ProductListVo> productListVos = Lists.newArrayList();
        for (Product p : products) {
            ProductListVo productListVo = assembleProductListVo(p);
            productListVos.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVos);

        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailV = new ProductDetailVo();

        productDetailV.setId(product.getId());
        productDetailV.setCategoryId(product.getCategoryId());
        productDetailV.setDetail(product.getDetail());
        productDetailV.setMainImage(product.getMainImage());
        productDetailV.setPrice(product.getPrice());
        productDetailV.setStatus(product.getStatus());
        productDetailV.setStock(product.getStock());
        productDetailV.setSubImages(product.getSubImages());
        productDetailV.setName(product.getName());
        productDetailV.setSubtitle(product.getSubtitle());

        productDetailV.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix",
                "http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());

        if (category != null) {
            productDetailV.setParentCategoryId(category.getParentId());
        } else {
            productDetailV.setParentCategoryId(0);
        }


        productDetailV.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailV.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailV;
    }


    /**
     * 转换为界面显示类
     *
     * @param product
     * @return
     */
    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();

        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setDetail(product.getDetail());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());

        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix",
                "http://img.happymmall.com/"));

        return productListVo;
    }


    /**
     * 前台根据产品 id获取信息
     *
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        //判断 productId 是否为 null
        if (productId == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILEGAL_ARGUMENT.getCode(), ResponseCode.ILEGAL_ARGUMENT.getDesc());
        }
        //根据产品 id获取产品
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {//有此产品
            return ServerResponse.createByErrorMsg("商品已下架");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMsg("商品已下架或删除");
        }

        //用于显示的产品类
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);

        return ServerResponse.createBySuccess(productDetailVo);
    }

    /**
     * 根据关键字类搜索
     *
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServerResponse<PageInfo> getProductKeywordCategory(String keyword,
                                                              Integer categoryId,
                                                              int pageNum,
                                                              int pageSize,
                                                              String orderBy) {
        //判断字段
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILEGAL_ARGUMENT.getCode(), ResponseCode.ILEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = new ArrayList<>();

        //查询分类是否存在
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            //判断分类和关键字是否存在
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有该分类，并且还没有该关键字，返回空，不能报异常情况
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = new ArrayList<>();
                //获取 list pageinfo
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);

            }
            //获取分类以及子分类
            categoryIdList = iCategoryService.selectChildrenParallelCategory(category.getId()).getData();

        }


        //判断 keyword
        if (StringUtils.isNotBlank(keyword)) {
            //拼接模糊查询字段
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        //初始化 PageHelper
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                //排序处理
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }

        }
//        System.out.printf("keyword: " + keyword);
        //查询所有分类结果
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword,
                categoryIdList.size() == 0 ? null : categoryIdList);
        List<ProductListVo> productListVoList = new ArrayList<>();
        for (Product p : productList) {
            ProductListVo productListVo = assembleProductListVo(p);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);

    }

}