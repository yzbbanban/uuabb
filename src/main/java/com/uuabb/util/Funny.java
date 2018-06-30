package com.uuabb.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ban on 2018/6/27.
 */
public class Funny {
    /**
     * @param path 图片路径
     */
    public static String createAsciiPic(final String path, int width, int height) throws Exception {
        final String base = "@#&$%*o!;.";// 字符串由复杂到简单
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            BufferedImage image = ImageIO.read(inStream);
            image = zoomImage(image, image.getWidth() / 10, image.getHeight() / 10, width, height);

            String str = "";
            for (int y = 0; y < image.getHeight(); y += 2) {
                if (y == 0) {
//                    sb.append("<span>/**</span>");
                    sb.append("/**");
//                    System.out.println("/**");
                } else if (!"".equals(str.trim())) {
//                    System.out.print(" *" + str);
//                    sb.append("<span>&nbsp;*"+str+"</span>");
                    sb.append(" *"  +str);
//                    sb.append("<p></p>");
                    sb.append("\n");
//                    System.out.println();
                }
                str = "";
                for (int x = 0; x < image.getWidth(); x++) {
                    final int pixel = image.getRGB(x, y);
                    final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                    final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                    final int index = Math.round(gray * (base.length() + 1) / 255);
                    str += index >= base.length() ? " " : String.valueOf(base.charAt(index));
//                    System.out.print(str);
//                    sb.append(str);
                }
            }
            sb.append(" */");
//            sb.append("<span>&nbsp;*/</span>");
//            System.out.println(" */");
            return sb.toString();
        } catch (final IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /*
     * 图片缩放,w，h为缩放的目标宽度和高度
     * src为源文件目录，dest为缩放后保存目录
     */
    public static BufferedImage zoomImage(BufferedImage image, int w, int h, int width, int height) throws Exception {

        double wr = 0, hr = 0;

        BufferedImage bufImg = image; //读取图片
        Image item = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//设置缩放目标图片模板

        wr = w * width * 1.0 / bufImg.getWidth();     //获取缩放比例
        hr = h * height * 1.0 / bufImg.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        item = ato.filter(bufImg, null);

        return toBufferedImage(item);
    }

    /**
     * 图片转换
     *
     * @param image
     * @return
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // 加载所有像素
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            // 创建buffer图像
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            e.printStackTrace();
        }
        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        // 复制
        Graphics g = bimage.createGraphics();
        // 赋值
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    /**
     * test
     *
     * @param args
     */
//    public static void main(final String[] args) {
//        try {
//            String result = Funny.createAsciiPic("http://www.uuabb.cn:81/5129f70a-1ef4-495f-8559-477a829ad8cd.png",10,9);
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}