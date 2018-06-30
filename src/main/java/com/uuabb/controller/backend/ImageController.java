package com.uuabb.controller.backend;

import com.google.common.collect.Lists;
import com.uuabb.common.Const;
import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.Image;
import com.uuabb.pojo.Img;
import com.uuabb.pojo.User;
import com.uuabb.pojo.vo.ImageVo;
import com.uuabb.service.IFileService;
import com.uuabb.service.IImageService;
import com.uuabb.service.IProductService;
import com.uuabb.service.IUserService;
import com.uuabb.util.Funny;
import com.uuabb.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
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
import java.util.List;
import java.util.Map;

/**
 * Created by brander on 2017/12/19
 */
@Controller
@RequestMapping("/img/")
public class ImageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IFileService iFileService;

    @Autowired
    private IImageService iImageService;


    @RequestMapping(value = "get_img.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<ImageVo>> getImages() {
        return iImageService.getImages();
    }


    @RequestMapping(value = "fun_img.do", method = RequestMethod.POST)
    @ResponseBody
    public String saveImage(HttpSession session,
                            @RequestParam(value = "upload_file", required = false) MultipartFile file,
                            @RequestParam(value = "width", required = false, defaultValue = "4") int width,
                            @RequestParam(value = "height", required = false, defaultValue = "4") int height,
                            HttpServletRequest request, HttpServletResponse response) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if (user == null) {
//            return "用户错误";
//        }
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        String url = "";
        String imageUrl = "";
//        if (iUserService.checkAdminRole(user).isSuccess()) {
        //上传保存图片
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)) {
            return "上传失败";

        }
        imageUrl = targetFileName;
        url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");

//        }

        //保存到数据库中
        iImageService.addImage(imageUrl);


        String result = "";

        try {
            result = Funny.createAsciiPic(url, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            return "哎呀图片出错啦";
        }

//        System.out.println("==========>"+result);

        return "<pre style='width:100%;height:100%;'>" + result + "</pre>";

    }


}
