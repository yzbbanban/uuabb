package com.uuabb.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by brander on 2018/1/9
 */
public class Test {

    public static void main(String[] args) {
        String s = "\"\\\"/uploads/2018-01-09/20180109232338_073334ea4f002402d6c922c2e9c78dbc.jpg\\";
        String s1="\"/uploads/2018-01-09/20180109232338_dd1f990e3d4de0d797ec14d420e2117e.jpg\\\"\"";
        String s2="\"/uploads/2018-01-10/20180110091419_b6c730e68ec40ec974668008ccc00fc9.jpg\\";

        String s3="\"\\\"/uploads/2018-01-09/20180109232338_073334ea4f002402d6c922c2e9c78dbc.jpg";
        String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
//        String imgp= "Redocn_2012100818523401.png";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(s3.substring(0, s.length() - 1));
        if ("\\".equals(s.substring(s3.length() - 1, s3.length())) && matcher.find()){
            System.out.println("========");
        }else if ("\\".equals(s3.substring(1, 2))) {
            System.out.println("++++++");
        }else if (s3.lastIndexOf("\\") > 1 && "/".equals(s3.substring(1, 2))) {
            System.out.println("-----");
        }


        if (s1.lastIndexOf("\\") > 1 && "/".equals(s1.substring(1,2))) {
            System.out.println("++11: " + s1.lastIndexOf("\\"));
        }
        System.out.println("---"+s.charAt(1));

        if ("\\".equals(s.substring(1,2))){
            System.out.println("---");
        }


        System.out.println(matcher.find());
    }
}
