package com.fiona.tiaozao.bean;

/**
 * 收藏实体类
 */
public class Collection {

    private int id;     //id
    private int userID; //用户id
    private Object obj; //收藏对象
    private int flag;   //1:goods 	0：user

    public Collection(int id, int userID, Object obj, int flag) {
        super();
        this.id = id;
        this.userID = userID;
        this.obj = obj;
        this.flag = flag;
    }


    public Collection(int id, int userID, Object obj) {
        super();
        this.id = id;
        this.userID = userID;
        this.obj = obj;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


}
