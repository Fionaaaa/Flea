package com.fiona.tiaozao.net;


import android.content.Context;
import android.os.Handler;

/**
 * <p>定义网络请求和数据解析的接口</p>
 * Created by fiona on 16-3-4.
 */
public interface NetQuery {

    /**
     * 获得所有出售物品
     *
     * @return
     */
    void getSaleGoods();

    /**
     * 获得所有求购物品
     *
     * @return
     */
    void getEmptionGoods();

    /**
     * 获得所有用户
     *
     * @return
     */
    void getUsers();

    /**
     * 获得单个用户
     *
     * @param userID
     * @return
     */
    void getUser(String userID, Handler handler);

    /**
     * 获得用户的求购
     *
     * @param userID
     * @return
     */
    void getUserEmption(String userID, Handler handler);

    /**
     * 获得用户的物品收藏
     *
     * @param userID
     * @return
     */
    void getCollectGoods(String userID);

    /**
     * 获得用户的关注收藏
     *
     * @param userID
     * @return
     */
    void getCollectUser(String userID);

    //获得单个用户出售的物品
    void getUserGoods(String user_id);

    //获取物品通知
    void getNotifyGoods(String user_id, Context context);

    //获取摊位通知
    void getNotifyStall(String user_id, Context context);

}
