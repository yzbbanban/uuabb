package com.uuabb.controller.backend;

import com.sun.deploy.net.HttpResponse;
import com.uuabb.common.Const;
import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.Product;
import com.uuabb.pojo.User;
import com.uuabb.service.IFileService;
import com.uuabb.service.IProductService;
import com.uuabb.service.IUserService;
import com.uuabb.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brander on 2017/12/19
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     * 保存产品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "save_product.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("未登录,请重新登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);

        } else {
            return ServerResponse.createByErrorMsg("非管理员");
        }
    }

    /**
     * 设置产品状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("未登录,请重新登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.setSaleStatus(productId, status);

        } else {
            return ServerResponse.createByErrorMsg("非管理员");
        }
    }

    /**
     * 获取产品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //获取善品的详细信息

            return iProductService.manageProductDetail(productId);
        }
        return ServerResponse.createByErrorMsg("用户不是管理员");

    }

    /**
     * 页数获取产品
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpSession session,
                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //获取善品的详细信息

            return iProductService.getProductList(pageNum, pageSize);
        }
        return ServerResponse.createByErrorMsg("用户不是管理员");

    }

    /**
     * 搜索产品
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse searchList(HttpSession session, String productName,
                                     Integer productId,
                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //获取善品的详细信息
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        }
        return ServerResponse.createByErrorMsg("用户不是管理员");

    }

    @RequestMapping(value = "img_upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                 HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
//            String path = request.getSession().getServletContext().getRealPath("upload");
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //上传保存图片
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                return ServerResponse.createByErrorMsg("上传失败");
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            Map fileMap = new HashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);

        }
        return ServerResponse.createByErrorMsg("用户不是管理员");


    }


    @RequestMapping(value = "richText_img_upload.do", method = RequestMethod.POST)
    @ResponseBody
    public Map richTextUpload(HttpSession session,
                              @RequestParam(value = "upload_file", required = false) MultipartFile file,
                              HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = new HashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "用户未登录");
            return resultMap;
        }
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //上传保存图片
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;

        }
        resultMap.put("success", false);
        resultMap.put("msg", "用户不是管理员");
        return resultMap;


    }

}
