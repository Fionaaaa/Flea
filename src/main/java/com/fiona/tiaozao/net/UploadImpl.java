package com.fiona.tiaozao.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.interactor.ImageOprator;
import com.fiona.tiaozao.model.Goods;
import com.fiona.tiaozao.model.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 上传实现类
 * Created by fiona on 16-3-6.
 */
public class UploadImpl implements Upload {

    private static RequestQueue queue;

    private static Context context;

    private UploadImpl() {
        if (NetQueryImpl.queue != null) {
            queue = NetQueryImpl.queue;
        } else {
            queue = Volley.newRequestQueue(context);
        }
        queue.start();
    }

    private static class Inner {
        static UploadImpl upload = new UploadImpl();
    }

    public static UploadImpl getInstance(Context ct) {
        context = ct;
        return Inner.upload;
    }

    //添加物品
    @Override
    public void addGoods(File file, Map<String, String> map, boolean isSale) {
        if (isSale) {
            map.put("isSale", "1");
            postFile(file, map);
        } else {
            map.put("isSale", "0");
            postEmption(map);
        }

    }

    //删除物品
    @Override
    public void deleteGoods(final int goodsID) {
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.GOODS_OPERATE_SERVLET, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap();
                map.put("type","delete");
                map.put("goods_id", String.valueOf(goodsID));
                return map;
            }
        };
        queue.add(request);
    }

    //更新物品
    @Override
    public void updateGoods(int goodsID) {

    }

    //添加收藏
    @Override
    public void addCollection(final String userID, final String user_goods_id) {
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_OPERATE_SERVLET, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap();
                map.put("type","1");
                map.put("user_id", String.valueOf(userID));
                map.put("user_goods_id", String.valueOf(user_goods_id));
                return map;
            }
        };
        queue.add(request);
    }

    //删除收藏
    @Override
    public void deleteCollection(final String userID, final String user_goods_id) {
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_OPERATE_SERVLET, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap();
                map.put("type","2");
                map.put("user_id", String.valueOf(userID));
                map.put("user_goods_id", String.valueOf(user_goods_id));
                return map;
            }
        };
        queue.add(request);
    }

    //添加用户
    @Override
    public void insertUser(User user) {

    }

    //更新用户
    @Override
    public void updateUser(User user) {

    }

    /**
     * @param file 需要上传的文件
     * @param map  需要上传的表单数据
     */
    private void postFile(File file, Map<String, String> map) {
        try {
            ImageOprator.saveFile(ImageOprator.getimage(file.getPath()), file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //文件
        RequestBody bodyFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        //表单（包含文件）
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", file.getName(), bodyFile);
        for (String key : map.keySet()) {
            builder.addFormDataPart(key, map.get(key));
        }
        builder.addFormDataPart("type","insert");
        RequestBody body = builder.build();

        //post请求
        Request request = new Request.Builder()
                .url(App.URL + App.GOODS_OPERATE_SERVLET)
                .post(body)
                .build();

        //加入请求队列
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
                Log.d("debug", "添加物品失败:"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功
                String code=response.toString();
                Log.d("debug",code);
            }
        });
    }

    /**
     * 添加求购
     *
     * @param map
     */
    private void postEmption(final Map<String, String> map) {

        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.GOODS_OPERATE_SERVLET, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.put("type", "emption");
                return map;
            }
        };
        queue.add(request);
    }
}