package com.fiona.tiaozao.net;

import com.fiona.tiaozao.model.Goods;

/**
 * 数据上传接口
 * Created by fiona on 16-3-6.
 */
public interface Upload {

    /**
     * 出售物品/求购物品
     */
    void addGoods(Goods goods);

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
     * @param goods_user_id
     */
    void addCollection(int goods_user_id);

    /**
     * 删除一个收藏
     * @param goods_user_id
     */
    void deleteCollection(int goods_user_id);
}
