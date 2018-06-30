package com.uuabb.pojo.vo;

import java.util.Date;

/**
 * Created by brander on 2018/6/29
 */
public class ImageVo {
    private Integer id;
    private String imageUrl;
    private Date createTime;
    private String ip;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl.trim();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", createTime=" + createTime +
                ", ip='" + ip + '\'' +
                '}';
    }
}
