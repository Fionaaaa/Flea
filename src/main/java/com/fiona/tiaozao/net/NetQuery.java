package com.fiona.tiaozao.net;


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
    public void getSaleGoods(Handler handler);

    /**
     * 获得所有求购物品
     *
     * @return
     */
    public void getEmptionGoods(Handler handler);

    /**
     * 获得分类物品
     *
     * @param classify
     * @return
     */
    public void getClassifyGoods(String classify, Handler handler);

    /**
     * 获得所有用户
     *
     * @return
     */
    public void getUsers(Handler handler);

    /**
     * 获得单个用户
     *
     * @param userID
     * @return
     */
    public void getUser(String userID, Handler handler);

    /**
     * 获得用户的求购
     *
     * @param userID
     * @return
     */
    public void getUserEmption(String userID, Handler handler);

    /**
     * 获得用户的物品收藏
     *
     * @param userID
     * @return
     */
    public void getCollectGoods(String userID, Handler handler);

    /**
     * 获得用户的关注收藏
     *
     * @param userID
     * @return
     */
    public void getCollectUser(String userID, Handler handler);

}
