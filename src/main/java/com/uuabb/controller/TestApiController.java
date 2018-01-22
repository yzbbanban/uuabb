package com.uuabb.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by brander on 2017/12/22
 */
@Controller
@RequestMapping("/test")
public class TestApiController {
    @ApiOperation(value="一个测试API",notes = "第一个测试api")
    @ResponseBody
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello()
    {
        return "hello";
    }
}
