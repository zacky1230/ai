package com.chineseall.entity;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:49.
 */
public class ImageBaseInfo {
    private double width;
    private double heigth;
    private String coordinate;

    public ImageBaseInfo() {
    }

    public ImageBaseInfo(double width, double heigth, String coordinate) {
        this.width = width;
        this.heigth = heigth;
        this.coordinate = coordinate;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeigth() {
        return heigth;
    }

    public void setHeigth(double heigth) {
        this.heigth = heigth;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }
}
