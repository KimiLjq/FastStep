package com.example.viewpagafragment.JavaBean;

public class CouponMessageBean {
    private String image;
    private String name;
    private int saleNumebr;
    private double afterMoney;
    private String endTime;
    private int id;
    private double beforeMoney;
    private String url;

    public CouponMessageBean() {
    }

    public CouponMessageBean(String image, String name, int saleNumebr, double afterMoney, String endTime, int id, double beforeMoney, String url) {
        this.image = image;
        this.name = name;
        this.saleNumebr = saleNumebr;
        this.afterMoney = afterMoney;
        this.endTime = endTime;
        this.id = id;
        this.beforeMoney = beforeMoney;
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSaleNumebr() {
        return saleNumebr;
    }

    public void setSaleNumebr(int saleNumebr) {
        this.saleNumebr = saleNumebr;
    }

    public double getAfterMoney() {
        return afterMoney;
    }

    public void setAfterMoney(double afterMoney) {
        this.afterMoney = afterMoney;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBeforeMoney() {
        return beforeMoney;
    }

    public void setBeforeMoney(double beforeMoney) {
        this.beforeMoney = beforeMoney;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
