package com.fiona.tiaozao.net;

import com.android.volley.RequestQueue;
import com.fiona.tiaozao.model.Goods;

/**
 * 上传实现类
 * Created by fiona on 16-3-6.
 */
public class UploadImpl implements Upload {

    private static RequestQueue queue;

    private UploadImpl() {
        queue = NetQueryImpl.queue;
    }

    private static class Inner {
        static UploadImpl upload = new UploadImpl();
    }

    public static UploadImpl getInstance() {
        return Inner.upload;
    }

    //添加物品
    @Override
    public void addGoods(Goods goods) {

    }

    //删除物品
    @Override
    public void deleteGoods(int goodsID) {

    }

    //更新物品
    @Override
    public void updateGoods(int goodsID) {

    }

    //添加收藏
    @Override
    public void addCollection(int goods_user_id) {

    }

    //删除收藏
    @Override
    public void deleteCollection(int goods_user_id) {

    }
}
