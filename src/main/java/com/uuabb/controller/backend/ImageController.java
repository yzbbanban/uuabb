package com.uuabb.controller.backend;

import com.google.common.collect.Lists;
import com.uuabb.common.ServerResponse;
import com.uuabb.pojo.Img;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by brander on 2017/12/19
 */
@Controller
@RequestMapping("/img/")
public class ImageController {
    @RequestMapping(value = "get_img.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Img>> getImages(String get) {
        List<Img> imgs = Lists.newArrayList();

        for (int i = 0; i < 10; i++) {
            Img img = new Img();
            img.setImgUrl("" + (i + 1) + ".jpg");
            imgs.add(img);
        }
        return ServerResponse.createBySuccess(imgs);

    }


}
