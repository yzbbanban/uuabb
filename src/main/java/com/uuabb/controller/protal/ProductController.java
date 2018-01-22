package com.uuabb.controller.protal;

import com.github.pagehelper.PageInfo;
import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.vo.ProductDetailVo;
import com.uuabb.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by brander on 2018/1/20
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;


    /**
     * 获取产品详情
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    /**
     * 用户端产品搜索
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", required = false) String orderBy) {

        return iProductService.getProductKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);

    }

}
