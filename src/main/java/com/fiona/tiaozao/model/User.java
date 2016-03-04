package com.fiona.tiaozao.model;

import java.util.ArrayList;

/**
 * 用户实体类
 */
public class User {

    private String id;            //id
    private String icon;    //名字
    private String name;    //����
    private String account;    //账户
    private String contact;    //联系方式
    private int flag;        //1：qq		0:微博

    private ArrayList<Goods> listSale;            //用户出售的物品


    /**
     * @param id
     * @param icon
     * @param name
     * @param account
     * @param contact
     * @param list
     */
    public User(String id, String icon, String name, String account, String contact, ArrayList<Goods> list) {
        super();
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.account = account;
        this.contact = contact;
        this.listSale = list;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", contact='" + contact + '\'' +
                ", flag=" + flag +
                ", listSale=" + listSale +
                '}';
    }
}
