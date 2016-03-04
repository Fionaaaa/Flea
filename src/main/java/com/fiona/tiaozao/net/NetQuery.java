package com.fiona.tiaozao.net;


import com.fiona.tiaozao.model.Goods;
import com.fiona.tiaozao.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by fiona on 16-3-4.
 */
public interface NetQuery {

    /**
     * 获得所有出售物品
     *
     * @return
     */
    public void getSaleGoods();

    /**
     * 获得所有求购物品
     *
     * @return
     */
    public void getEmptionGoods();

    /**
     * 获得分类物品
     *
     * @param classify
     * @return
     */
    public void getClassifyGoods(String classify);

    /**
     * 获得所有用户
     *
     * @return
     */
    public void getUsers();

    /**
     * 获得单个用户
     *
     * @param userID
     * @return
     */
    public void getUser(String userID);

    /**
     * 获得用户的求购
     *
     * @param userID
     * @return
     */
    public void getUserEmption(int userID);

    /**
     * 获得用户的物品收藏
     *
     * @param userID
     * @return
     */
    public void getCollectGoods(int userID);

    /**
     * 获得用户的关注收藏
     *
     * @param userID
     * @return
     */
    public void getCollectUser(int userID);

}
