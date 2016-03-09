package com.fiona.tiaozao.net;

import com.fiona.tiaozao.bean.User;

import java.io.File;
import java.util.Map;

/**
 * 数据上传接口
 * Created by fiona on 16-3-6.
 */
public interface Upload {

    /**
     * 出售物品/求购物品
     */
    void addGoods(File file,Map<String,String> map,boolean isSale);

    /**
     * 删除一件物品
     * @param goodsID
     */
    void deleteGoods(int goodsID);

    /**
     * 更新一件物品
     * @param goodsID
     */
    void updateGoods(int goodsID);

    /**
     * 添加一个收藏
     * @param
     */
    void addCollection(final String userID, final String user_goods_id);

    /**
     * 删除一个收藏
     * @param
     */
    void deleteCollection(final String userID, final String user_goods_id);

    /**
     * 添加一个用户
     * @param user
     */
    void insertUser(User user);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(String desceibe,String user_id);
}
