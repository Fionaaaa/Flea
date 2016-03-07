package com.fiona.tiaozao.bean;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * 物品实体类
 */
public class Goods extends SugarRecord implements Serializable {

    @Expose
    private String title;        //名字
    @Expose
    private float price;        //价格
    @Expose
    private String describe;    //描述
    @Expose
    private String userId;      //用户id
    @Expose
    private String classify;    //分类
    @Expose
    private String contact;     //描述
    @Expose
    private String time;           //时间
    @Expose
    private int flag;            //1：出售		0：求购
    @Expose
    private String userName;     //用户名

    @Expose
    private String pic_location;    //图片地址

    public Goods() {

    }

    /**
     * @param title
     * @param price
     * @param describe
     * @param userId
     * @param flag
     * @param classify
     */
    public Goods(String title, float price, String describe, String userId, String classify, String time, int flag, String pic) {
        super();
        this.title = title;
        this.price = price;
        this.describe = describe;
        this.userId = userId;
        this.classify = classify;
        this.time = time;
        this.flag = flag;
        pic_location = pic;
    }


    public Goods(String title, float price, String describe, String userId, String classify, String time,
                 String pic_location) {
        super();
        this.title = title;
        this.price = price;
        this.describe = describe;
        this.userId = userId;
        this.classify = classify;
        this.time = time;
        this.pic_location = pic_location;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the price
     */
    public float getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * @return the describe
     */
    public String getDescribe() {
        return describe;
    }

    /**
     * @param describe the describe to set
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the classify
     */
    public String getClassify() {
        return classify;
    }

    /**
     * @param classify the classify to set
     */
    public void setClassify(String classify) {
        this.classify = classify;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getPic_location() {
        return pic_location;
    }

    public void setPic_location(String pic_location) {
        this.pic_location = pic_location;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Goods{" +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", describe='" + describe + '\'' +
                ", userId=" + userId +
                ", classify='" + classify + '\'' +
                ", time=" + time +
                ", flag=" + flag +
                ", pic_location='" + pic_location + '\'' +
                '}';
    }
}
