package com.fiona.tiaozao.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
 * mark：用户里已存在其所出售的物品
 * Created by fiona on 16-3-4.
 */
public class NetQueryImpl implements NetQuery {

    RequestQueue queue;     //请求队列
    Handler handler;

    public NetQueryImpl(Context context, Handler handler) {
        this.queue = Volley.newRequestQueue(context);
        this.handler = handler;
    }

    /**
     * 获得出售的物品
     *
     * @return
     */
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
        });

        queue.add(request);
        queue.start();
    }

    /**
     * 获得求购的物品
     *
     * @return
     */
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
        queue.start();
    }

    /**
     * 获得分类的物品
     *
     * @param classify
     * @return
     */
    @Override
    public void getClassifyGoods(String classify) {
        final String[] code = new String[1];
        switch (classify) {
            case "数码":
                code[0]="0";
                break;
            case "电器":
                code[0]="1";
                break;
            case "日常用品":
                code[0]="2";
                break;
            case "书籍":
                code[0]="3";
                break;
            case "服饰":
                code[0]="4";
                break;
            case "体育用品":
                code[0]="5";
                break;
            case "其他":
                code[0]="6";
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap();
                map.put("type",code[0]);
                return map;
            }
        };

        queue.add(request);
        queue.start();
    }

    /**
     * 获得所有用户
     *
     * @return
     */
    @Override
    public void getUsers() {
    }

    /**
     * 获得单个用户
     *
     * @param userID
     * @return
     */
    @Override
    public void getUser(final String userID) {
        StringRequest request = new StringRequest(StringRequest.Method.POST, App.URL + App.USER_SERVLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //解析返回数据
                Gson gson = new Gson();
                User user = gson.fromJson(s, User.class);
                Message message = new Message();
                message.obj = user;
                message.what=App.USER;
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
        queue.start();
    }

    /**
     * 获得用户求购的物品
     *
     * @param userID
     * @return
     */
    @Override
    public void getUserEmption(int userID) {
    }

    /**
     * 获得收藏的物品
     *
     * @param userID
     * @return
     */
    @Override
    public void getCollectGoods(int userID) {
    }

    /**
     * 获得收藏用户
     *
     * @param userID
     * @return
     */
    @Override
    public void getCollectUser(int userID) {
    }
}
