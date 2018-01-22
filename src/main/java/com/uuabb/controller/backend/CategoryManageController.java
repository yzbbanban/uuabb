package com.uuabb.controller.backend;

import com.uuabb.common.Const;
import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.User;
import com.uuabb.service.ICategoryService;
import com.uuabb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by brander on 2017/12/18
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {


    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session,
                                              String categoryName,
                                              @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {//未登录
            return ServerResponse.createByErrorMsg("用户未登录");

        }
        //用户已登陆
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMsg("无操作权限，非管理员");
        }
    }

    @RequestMapping(value = "set_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updateCategory(HttpSession session,String categoryName, Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {//未登录
            return ServerResponse.createByErrorMsg("用户未登录");

        }
        //用户已登陆
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员

            return iCategoryService.updateCategory(categoryName,categoryId);
        } else {
            return ServerResponse.createByErrorMsg("无操作权限，非管理员");
        }


    }

    @RequestMapping(value = "get_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,Integer parentId){

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {//未登录
            return ServerResponse.createByErrorMsg("用户未登录");

        }
        //用户已登陆
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            //查询子节点的 category 信息，不递归，保持平级
            return iCategoryService.getChildrenParallelCategory(parentId);

        } else {
            return ServerResponse.createByErrorMsg("无操作权限，非管理员");
        }
    }

    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session ,Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {//未登录
            return ServerResponse.createByErrorMsg("用户未登录");

        }
        //用户已登陆
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            //查询递归子节点的id，查询左右的节点数据
            return iCategoryService.selectChildrenParallelCategory(categoryId);

        } else {
            return ServerResponse.createByErrorMsg("无操作权限，非管理员");
        }

    }

}
