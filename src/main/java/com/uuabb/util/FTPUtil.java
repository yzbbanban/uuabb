package com.uuabb.util;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by brander on 2017/12/24
 */
public class FTPUtil {

    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    //访问参数
    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");


    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    private String ip;
    private int port;

    private String user;
    private String pwd;

    //ftp 客户端
    private FTPClient ftpClient;

    //上传多文件
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        System.out.printf("开始上传===========================");
        Boolean result = ftpUtil.uploadfile(fileList, "img");
        System.out.printf("上传完成===========================");
        return result;
    }

    /**
     * 上传图片
     *
     * @param fileList
     * @param remotePath
     * @return
     */
    private boolean uploadfile(List<File> fileList, String remotePath) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;//文件流
        System.out.printf("ftp上传完成路径---"+remotePath);
        if (connectServer(ip, port, user, pwd)) {

            try {
                ftpClient.changeWorkingDirectory(remotePath);//上传路径
                ftpClient.setBufferSize(1024);//每次多少
                ftpClient.setControlEncoding("utf-8");//字符编码
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//上传的类型 二进制
                ftpClient.enterLocalActiveMode();//打开本地被动模式
                for (File file : fileList) {//遍历存储
                    fis = new FileInputStream(file);//创建文件流
                    ftpClient.storeFile(file.getName(), fis);//存储文件
                    System.out.printf("ftp上传完成-----");
                    System.out.printf("ftp上传完成路径xx---"+remotePath);
                }
            } catch (IOException e) {
                logger.info("上传失败", e);
                System.out.printf("ftp上传失败-----"+e.getMessage());
                uploaded = false;
                e.printStackTrace();
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        } else {
            uploaded = false;
        }


        return uploaded;
    }

    /**
     * 连接 ftp 服务器
     *
     * @return
     */
    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = true;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);//连接
            isSuccess = ftpClient.login(user, pwd);//登陆
        } catch (IOException e) {
            isSuccess = false;
            logger.info("连接失败", e);
        }
        return isSuccess;

    }


    public static String getFtpIp() {
        return ftpIp;
    }

    public static void setFtpIp(String ftpIp) {
        FTPUtil.ftpIp = ftpIp;
    }

    public static String getFtpUser() {
        return ftpUser;
    }

    public static void setFtpUser(String ftpUser) {
        FTPUtil.ftpUser = ftpUser;
    }

    public static String getFtpPass() {
        return ftpPass;
    }

    public static void setFtpPass(String ftpPass) {
        FTPUtil.ftpPass = ftpPass;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
