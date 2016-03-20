package com.fiona.tiaozao.net;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.util.ImageOprator;
import com.fiona.tiaozao.util.Util;

import java.io.File;
import java.io.IOException;
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
    public void deleteGoods(final String goodsID) {
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
                Map<String, String> map = new HashMap();
                map.put("type", "delete");
                map.put("goods_id", goodsID);
                return map;
            }
        };
        queue.add(request);
    }

    //更新物品
    @Override
    public void updateGoods(final int goodsID, final int price) {
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.GOODS_OPERATE_SERVLET, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("debug", "物品更新成功");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("debug", "物品更新失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("type", "update");
                map.put("price", String.valueOf(price));
                map.put("goods_id", String.valueOf(goodsID));
                return map;
            }
        };
        queue.add(request);
    }

    //添加收藏
    @Override
    public void addCollection(final String userID, final String user_goods_id, final String flag) {
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
                Map<String, String> map = new HashMap();
                map.put("type", "1");
                map.put("user_id", userID);
                map.put("obj_id", user_goods_id);
                map.put("flag", flag);
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
                Map<String, String> map = new HashMap();
                map.put("type", "2");
                map.put("user_id", String.valueOf(userID));
                map.put("obj_id", String.valueOf(user_goods_id));
                return map;
            }
        };
        queue.add(request);
    }

    //添加用户
    @Override
    public void insertUser(final User user) {
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_OPERATE_SERVLET, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("debug", "添加用户成功");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("debug", "添加用户失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("type", "3");
                map.put("icon", user.getIcon());
                map.put("name", user.getName());
                map.put("account", user.getAccount());
                map.put("flag", String.valueOf(user.getFlag()));
                map.put("describe", user.getDescribe());

                return map;
            }
        };
        queue.add(request);
    }

    //更新用户
    @Override
    public void updateUser(final String desceibe, final String user_id) {
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_OPERATE_SERVLET, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("debug", "更新用户成功");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("debug", "更新用户失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("type", "4");
                map.put("describe", desceibe);
                map.put("user_id", user_id);

                return map;
            }
        };
        queue.add(request);
    }

    /**
     * @param file 需要上传的文件
     * @param map  需要上传的表单数据
     */
    private void postFile(File file, Map<String, String> map) {

        File fileSmall = null;
        File fileLarge=null;
        //先进行压缩
        try {
            ImageOprator oprator=new ImageOprator();
            Util util=new Util();
            fileSmall = util.copyFile(file,true);
            fileLarge=util.copyFile(file,false);
            oprator.saveFile(oprator.getimage(file.getPath(), null), fileSmall);
            oprator.saveFile(oprator.getimage(file.getPath()), fileLarge);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody bodyFile = RequestBody.create(MediaType.parse("application/octet-stream"), fileLarge);
        RequestBody bodySmallFile = RequestBody.create(MediaType.parse("application/octet-stream"), fileSmall);

        //表单（包含文件）
        MultipartBody.Builder builder = new MultipartBody.Builder();

        builder.addFormDataPart("file", file.getName(), bodyFile);
        builder.addFormDataPart("small", file.getName(), bodySmallFile);

        for (String key : map.keySet()) {
            Log.d("key-values:", key + ":" + map.get(key));
            builder.addFormDataPart(key, map.get(key));
        }
        builder.addFormDataPart("type", "insert");
        RequestBody body = builder.build();

        //post请求
        Request request = new Request.Builder()
                .url(App.URL + App.GOODS_OPERATE_SERVLET)
                .post(body)
                .build();

        //加入请求队列
        final File finalFileLarge = fileLarge;
        final File finalFileSmall = fileSmall;
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
                Log.d("debug", "添加物品失败:" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功
                String code = response.toString();
                Log.d("debug", code);

                //把文件删了
                finalFileLarge.delete();
                finalFileSmall.delete();
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
