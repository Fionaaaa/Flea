package com.fiona.tiaozao.bean;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户实体类
 */
public class User extends SugarRecord implements Serializable {

    @Expose
    private String icon;    //头像
    @Expose
    private String name;    //名字
    @Expose
    private String account;    //账户
    @Expose
    private String contact;    //联系方式
    @Expose
    private int flag;        //1：qq		0:微博
    @Ignore
    private ArrayList<Goods> listSale;            //用户出售的物品
    @Expose
    private String describe;        //摊位描述  mark：服务器暂时没添加


    public User() {
    }

    public User(String icon, String name, String account, int flag) {
        this.icon = icon;
        this.name = name;
        this.account = account;
        this.flag = flag;
    }

    /**
     * @param icon
     * @param name
     * @param account
     * @param contact
     * @param list
     */
    public User(String icon, String name, String account, String contact, ArrayList<Goods> list) {
        super();
        this.icon = icon;
        this.name = name;
        this.account = account;
        this.contact = contact;
        this.listSale = list;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }


    public ArrayList<Goods> getListSale() {
        return listSale;
    }

    public void setListSale(ArrayList<Goods> listSale) {
        this.listSale = listSale;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "User{" +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", contact='" + contact + '\'' +
                ", flag=" + flag +
                ", listSale=" + listSale +
                '}';
    }
}
