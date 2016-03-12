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
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.interactor.Interactor;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private static Context mContext;

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
        mContext = context;
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
            queue.start();
        }
        return Inner.netQuery;
    }

    private static class Inner {
        static NetQueryImpl netQuery = new NetQueryImpl();
    }


    //从网络获得出售的物品(缓存)    1
    @Override
    public void getSaleGoods() {

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
                    goods.setGoods_id(String.valueOf(goods.getId()));
                    data.add(goods);
                }

                //更新缓存
                Goods.deleteAll(Goods.class, "flag=?", "1");
                Goods.saveInTx(data);

                //发送通知到总线
                EventBus.getDefault().post(App.QUERY_SALE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("debug", "请求物品列表：error");
            }
        });

        queue.add(request);
    }


    //获得求购的物品   2
    @Override
    public void getEmptionGoods() {
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
                    goods.setGoods_id(String.valueOf(goods.getId()));
                    data.add(goods);
                }

                //本地缓存
                Goods.deleteAll(Goods.class, "flag=?", "0");
                Goods.saveInTx(data);

                //发通知到总线
                EventBus.getDefault().post(App.QUERY_EMPTION);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("debug", "error");
            }
        });

        queue.add(request);
    }


    //获得所有用户    3
    @Override
    public void getUsers() {

        StringRequest request = new StringRequest(App.URL + App.USER_SERVLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ArrayList<User> data = new ArrayList<>();
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(s);
                JsonArray jsonArray = element.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    element = jsonArray.get(i);
                    User user = gson.fromJson(element, User.class);
                    user.setUser_id(String.valueOf(user.getId()));
                    data.add(user);
                }

                //更新缓存
                User.deleteAll(User.class);
                User.saveInTx(data);
                //发送通知（总线）
                EventBus.getDefault().post(App.QUERY_USER);
            }
        }, null);
        queue.add(request);
    }

    //获得单个用户出售的物品
    @Override
    public void getUserGoods(String user_id){
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
                    goods.setGoods_id(String.valueOf(goods.getId()));
                    data.add(goods);
                }

                //本地缓存
                Goods.deleteAll(Goods.class, "flag=?", "0");
                Goods.saveInTx(data);

                //发通知到总线
                EventBus.getDefault().post(App.QUERY_EMPTION);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("debug", "error");
            }
        });

        queue.add(request);
    }

    //获得单个用户
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


    //获得单个用户求购的物品
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


    //获得收藏的物品   4
    @Override
    public void getCollectGoods(final String userID) {
        final ArrayList<Goods> listGoods = new ArrayList<>();
        final Set<String> ids = new HashSet<>();
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
                    if (goods != null) {
                        ids.add(String.valueOf(goods.getId()));
                    }
                }

                //将收藏记录写到本地
                mContext.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putStringSet("collect_goods", ids).commit();
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


    //获得收藏的用户    5
    @Override
    public void getCollectUser(final String userID) {
        final ArrayList<User> listUser = new ArrayList<>();
        final Set<String> ids = new HashSet<>();
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
                    ids.add(String.valueOf(user.getId()));

                }

                //将收藏记录写到本地
                mContext.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putStringSet("collect_user", ids).commit();
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

    //获得物品通知
    @Override
    public void getNotifyGoods(final String user_id, final Context context) {
        final ArrayList<Goods> listGoods = new ArrayList<>();
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_SERVLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("debug", "s:" + s);
                if (s.length() > 0) {
                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(s);
                    JsonArray jsonArray = element.getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        element = jsonArray.get(i);
                        Goods goods = gson.fromJson(element, Goods.class);
                        listGoods.add(goods);
                    }

                    //发送通知
                    Interactor.sendNotifyGoods(listGoods, context);
                }
            }
        }, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("type", "notify");
                map.put("user_id", user_id);
                return map;
            }
        };
        queue.add(request);
    }

    //获得摊位通知
    @Override
    public void getNotifyStall(final String user_id, final Context context) {
        final ArrayList<User> listUser = new ArrayList<>();
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_SERVLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("debug", "s:" + s);
                if (s.length() > 0) {
                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(s);
                    JsonArray jsonArray = element.getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        element = jsonArray.get(i);
                        User user = gson.fromJson(element, User.class);
                        listUser.add(user);
                    }

                    //发送通知
                    Interactor.sendNotifyStall(listUser, context);
                }
            }
        }, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("type", "stall");
                map.put("user_id", user_id);
                return map;
            }
        };
        queue.add(request);
    }
}
