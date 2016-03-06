package com.fiona.tiaozao.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.model.Goods;
import com.fiona.tiaozao.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * 网络请求和数据解析的实现类
 * <p/>
 * <p/>
 * 用户里已存在其所出售的物品
 * <p/>
 * Created by fiona on 16-3-4.
 */
public class NetQueryImpl implements NetQuery {

    public static RequestQueue queue;     //请求队列，唯一的

    /**
     * 单例模式
     */
    private NetQueryImpl() {
    }

    /**
     * 获得单例
     *
     * @param context
     * @return
     */
    public static NetQueryImpl getInstance(Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
            queue.start();
        }
        return Inner.netQuery;
    }

    private static class Inner {
        static NetQueryImpl netQuery = new NetQueryImpl();
    }

    /**
     * 获得出售的物品
     *
     * @return
     */
    @Override
    public void getSaleGoods(final Handler handler) {

        StringRequest request = new StringRequest(App.URL + App.GOODS_SERVLET + "?type=sale", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ArrayList<Goods> data = new ArrayList<>();

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(s);
                JsonArray jsonArray = element.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    element = jsonArray.get(i);
                    Goods goods = gson.fromJson(element, Goods.class);
                    data.add(goods);
                }

                Message message = new Message();
                message.what = App.GOODS_SALE;
                message.obj = data;

                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("debug", "请求物品列表：error");
            }
        });

        queue.add(request);
    }

    /**
     * 获得求购的物品
     *
     * @return
     */
    @Override
    public void getEmptionGoods(final Handler handler) {
        StringRequest request = new StringRequest(App.URL + App.GOODS_SERVLET + "?type=emption", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ArrayList<Goods> data = new ArrayList<>();

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(s);
                JsonArray jsonArray = element.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    element = jsonArray.get(i);
                    Goods goods = gson.fromJson(element, Goods.class);
                    data.add(goods);
                }

                Message message = new Message();
                message.what = App.GOODS_EMPTION;
                message.obj = data;
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("debug", "error");
            }
        });

        queue.add(request);
    }

    /**
     * 获得分类的物品
     *
     * @param classify
     * @return
     */
    @Override
    public void getClassifyGoods(String classify, final Handler handler) {
        final String[] code = new String[1];
        switch (classify) {
            case "数码":
                code[0] = "0";
                break;
            case "电器":
                code[0] = "1";
                break;
            case "日常用品":
                code[0] = "2";
                break;
            case "书籍":
                code[0] = "3";
                break;
            case "服饰":
                code[0] = "4";
                break;
            case "体育用品":
                code[0] = "5";
                break;
            case "其他":
                code[0] = "6";
                break;
        }
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.CLASSIFY_SERVLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ArrayList<Goods> data = new ArrayList<>();

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(s);
                JsonArray jsonArray = element.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    element = jsonArray.get(i);
                    Goods goods = gson.fromJson(element, Goods.class);
                    data.add(goods);
                }

                Message message = new Message();
                message.what = App.GOODS_SALE;
                message.obj = data;
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("debug", "error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("type", code[0]);
                return map;
            }
        };

        queue.add(request);
    }

    /**
     * 获得所有用户
     *
     * @return
     */
    @Override
    public void getUsers(final Handler handler) {
        final ArrayList<User> data = new ArrayList<>();
        StringRequest request = new StringRequest(App.URL + App.USER_SERVLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(s);
                JsonArray jsonArray = element.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    element = jsonArray.get(i);
                    User user = gson.fromJson(element, User.class);
                    data.add(user);
                }
                Message message = new Message();
                message.obj = data;
                handler.sendMessage(message);
            }
        }, null);
        queue.add(request);
    }

    /**
     * 获得单个用户
     *
     * @param userID
     * @return
     */
    @Override
    public void getUser(final String userID, final Handler handler) {
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_SERVLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //解析返回数据
                Gson gson = new Gson();
                User user = gson.fromJson(s, User.class);
                Message message = new Message();
                message.obj = user;
                message.what = App.USER;
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("debug", volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("type", "sale");
                map.put("id", userID);
                return map;
            }
        };
        queue.add(request);
    }

    /**
     * 获得用户求购的物品
     *
     * @param userID
     * @return
     */
    @Override
    public void getUserEmption(final String userID, final Handler handler) {
        final ArrayList<Goods> listGoods = new ArrayList<>();
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_SERVLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(s);
                JsonArray jsonArray = element.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    element = jsonArray.get(i);
                    Goods goods = gson.fromJson(element, Goods.class);
                    listGoods.add(goods);
                }
                Message message = new Message();
                message.obj = listGoods;
                handler.sendMessage(message);
            }
        }, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("type", "emption");
                map.put("id", userID);
                return map;
            }
        };
        queue.add(request);
    }

    /**
     * 获得收藏的物品
     *
     * @param userID
     * @return
     */
    @Override
    public void getCollectGoods(final String userID, final Handler handler) {
        final ArrayList<Goods> listGoods = new ArrayList<>();
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_SERVLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(s);
                JsonArray jsonArray = element.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    element = jsonArray.get(i);
                    Goods goods = gson.fromJson(element, Goods.class);
                    listGoods.add(goods);
                }
                Message message = new Message();
                message.obj = listGoods;
                handler.sendMessage(message);
            }
        }, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("type", "goods");
                map.put("id", userID);
                return map;
            }
        };
        queue.add(request);
    }

    /**
     * 获得收藏用户
     *
     * @param userID
     * @return
     */
    @Override
    public void getCollectUser(final String userID, final Handler handler) {
        final ArrayList<User> listUser = new ArrayList<>();
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_SERVLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(s);
                JsonArray jsonArray = element.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    element = jsonArray.get(i);
                    User user = gson.fromJson(element, User.class);
                    listUser.add(user);
                }
                Message message = new Message();
                message.obj = listUser;
                handler.sendMessage(message);
            }
        }, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("type", "user");
                map.put("id", userID);
                return map;
            }
        };
        queue.add(request);
    }
}
