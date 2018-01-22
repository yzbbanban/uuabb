package com.uuabb.pojo;

import java.io.Serializable;

/**
 * Created by brander on 2017/12/19
 */
public class Img implements Serializable{
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Img{" +
                "imgUrl='" + imgUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Img img = (Img) o;

        return imgUrl != null ? imgUrl.equals(img.imgUrl) : img.imgUrl == null;
    }

    @Override
    public int hashCode() {
        return imgUrl != null ? imgUrl.hashCode() : 0;
    }
}
