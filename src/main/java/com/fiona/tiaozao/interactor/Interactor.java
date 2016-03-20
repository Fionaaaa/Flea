package com.fiona.tiaozao.interactor;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.util.Log;

import com.fiona.tiaozao.App;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.SaleActivity;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.fragment.myself.MyCollectionActivity;
import com.fiona.tiaozao.net.NetQuery;
import com.fiona.tiaozao.net.NetQueryImpl;
import com.fiona.tiaozao.net.UploadImpl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 中间人：（只有一个代理类，干了所有的事。。。累死你。）
 * Created by fiona on 16-3-6.
 */
public class Interactor {

    //从本地取一张照片
    public void pickPicture(Context context) {

        Intent intent = new Intent();
        //设置动作
        intent.setAction(Intent.ACTION_PICK);

        //设置数据
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Uri uri = Uri.fromFile(path);
//        Uri uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        intent.setDataAndType(uri, "image/*");

        //带结果的intent 请求码
        SaleActivity saleActivity = (SaleActivity) context;
        saleActivity.startActivityForResult(intent, App.REQUEST_PICK_PICTURE);
    }

    //拍照
    public void doCapture(Context context) {
        SaleActivity saleActivity = (SaleActivity) context;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //格式化照片文件名字
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String fileName = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        //照片文件位置
        saleActivity.file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "flea_" + fileName + ".jpg");

        Uri imageUri = Uri.fromFile(saleActivity.file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        saleActivity.startActivityForResult(intent, App.REQUEST_CAPTURE);
    }


    //开始所有网络请求
    public  void startAllNetTask(Context context) {
        NetQuery query = NetQueryImpl.getInstance(context);

        query.getSaleGoods();                //请求出售的物品

        query.getUsers();                    //获得所有用户

        query.getEmptionGoods();             //请求求购的物品

        query.getCollectUser(getId(context));//请求本人收藏的摊主

        query.getCollectGoods(getId(context));//请求本人收藏的物品

        getNotify(query, context);           //请求通知

    }

    //请求出售的物品
    public  void getSales(Context context) {
        NetQuery query = NetQueryImpl.getInstance(context);
        query.getSaleGoods();
    }

    //请求求购的物品
    public  void getEmption(Context context) {
        NetQueryImpl query = NetQueryImpl.getInstance(context);
        query.getEmptionGoods();
    }

    //请求用户列表
    public  void getUsers(Context context) {
        NetQueryImpl.getInstance(context).getUsers();
    }

    //请求一个用户出售的物品
    public  void getUserGoods(Context context, String user_id) {
        NetQueryImpl.getInstance(context).getUserGoods(user_id);
    }

    //添加一个用户
    public  void insertUser(final Context context, final User user) {
        //从本地数据判断是否存在此用户，不存在则上传服务器
        new AsyncTask<User, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(User... params) {
                User user = params[0];
                ArrayList<User> userList = (ArrayList<User>) User.find(User.class, "account=?", user.getAccount());
                if (userList.size() > 0) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                if (!b) {
                    //上传服务器
                    UploadImpl.getInstance(context).insertUser(user);
                } else {
                    Log.d("debug", "本地已存在此用户");
                }
            }
        }.execute(user);

    }

    //根据账户获得用户id
    public  String getId(Context context) {
        String account = context.getSharedPreferences("user", context.MODE_PRIVATE).getString("account", "000");
        List<User> list = User.find(User.class, "account=?", account);
        if (list.size() > 0) {
            return String.valueOf(list.get(0).getId());
        }
        return "000";
    }

    //从本地获得收藏的摊位
    public  ArrayList<User> getCollectUser(Context context) {
        ArrayList<User> list = new ArrayList<>();
        HashSet<String> set = (HashSet<String>) context.getSharedPreferences("user", context.MODE_PRIVATE).getStringSet("collect_user", null);

        if (set != null && set.size() > 0) {
            for (String s : set) {
                list.add(User.findById(User.class, Long.valueOf(s)));
            }
        }
        return list;
    }

    //从本地获得收藏的物品
    public  ArrayList<Goods> getCollectGoods(Context context) {
        ArrayList<Goods> list = new ArrayList<>();
        HashSet<String> set = (HashSet<String>) context.getSharedPreferences("user", context.MODE_PRIVATE).getStringSet("collect_goods", null);
        if (set != null && set.size() > 0) {
            for (String s : set) {
                list.add(Goods.findById(Goods.class, Long.valueOf(s)));
            }
        }
        return list;
    }

    //给用户集添加出售商品集
    public  ArrayList<User> getGoodsToUser(ArrayList<User> userList) {
        for (int i = 0; i < userList.size(); i++) {
            ArrayList<Goods> goodsList = (ArrayList<Goods>) Goods.find(Goods.class, "user_id=? and flag=?", String.valueOf(userList.get(i).getId()), "1");
            userList.get(i).setListSale(goodsList);
        }
        return userList;
    }

    //更新用户描述
    public  void updateUserDescribe(Context context, String describe) {
        //更新网络
        UploadImpl.getInstance(context).updateUser(describe, getId(context));
        //更新本地
        User.executeQuery("update user set describe =? where id=?", describe, getId(context));
    }

    //用户是否收藏了此物品
    public  boolean isCollected(Context context, String id, int flag) {

        Set<String> set;
        if (flag == 1) {
            set = context.getSharedPreferences("user", Context.MODE_PRIVATE).getStringSet("collect_goods", null);
        } else {
            set = context.getSharedPreferences("user", Context.MODE_PRIVATE).getStringSet("collect_user", null);
        }
        if (set != null && set.contains(id)) {
            return true;
        }
        return false;
    }

    //删除一个收藏(本地及服务器)
    public  void deletCollection(Context context, String obj_id, int flag) {
        Set<String> set;
        if (flag == 1) {
            set = context.getSharedPreferences("user", Context.MODE_PRIVATE).getStringSet("collect_goods", null);
            if (set != null) {
                if (set.contains(obj_id)) {
                    set.remove(obj_id);
                    context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putStringSet("collect_goods", set).commit();
                }
            }
        } else {
            set = context.getSharedPreferences("user", Context.MODE_PRIVATE).getStringSet("collect_user", null);
            if (set != null && set.contains(obj_id)) {
                set.remove(obj_id);
                context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putStringSet("collect_user", set).commit();
            }
        }

        UploadImpl.getInstance(context).deleteCollection(getId(context), obj_id);
    }

    //添加一个收藏(本地以及服务器)
    public  void addCollection(Context context, String obj_id, int flag) {
        Set<String> set;
        if (flag == 1) {
            set = context.getSharedPreferences("user", Context.MODE_PRIVATE).getStringSet("collect_goods", null);
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(obj_id);
            context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putStringSet("collect_goods", set).commit();
        } else {
            set = context.getSharedPreferences("user", Context.MODE_PRIVATE).getStringSet("collect_user", null);
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(obj_id);
            context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putStringSet("collect_user", set).commit();
        }

        UploadImpl.getInstance(context).addCollection(getId(context), obj_id, String.valueOf(flag));
    }

    //更具物品id获得用户的头像
    public  String getIcon(String goods_id) {
        List<Goods> listGoods = Goods.find(Goods.class, "goodsid=?", goods_id);
        if (listGoods.size() > 0) {
            String user_id = listGoods.get(0).getUserId();
            List<User> listUser = User.find(User.class, "userid=?", user_id);
            if (listUser.size() > 0) {
                return listUser.get(0).getIcon();
            }
        }
        return null;
    }

    //删除物品
    public  void deleteGoods(Context context, List<Goods> list) {
        for (Goods goods : list) {
            UploadImpl.getInstance(context).deleteGoods(goods.getGoods_id());
        }
    }

    //获得wifi设置
    public  boolean onlyWifi(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getBoolean(App.SETTING_WIFI, false);
    }

    //清空本地设置
    public  void clearSetting(Context context) {
        SharedPreferences pf = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        pf.edit().remove(App.SETTING_WIFI).commit();
        pf.edit().remove(App.SETTING_STALL).commit();
        pf.edit().remove(App.SETTING_GOODS).commit();
    }

    //请求通知
    public  void getNotify(NetQuery query, Context context) {
        boolean setting_goods = context.getSharedPreferences("user", Context.MODE_PRIVATE).getBoolean(App.SETTING_GOODS, false);
        boolean setting_stall = context.getSharedPreferences("user", Context.MODE_PRIVATE).getBoolean(App.SETTING_STALL, false);
        String account = context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("account", null);

        if (account != null) {
            ArrayList<User> list = (ArrayList<User>) User.find(User.class, "account=?", account);
            if (list.size() > 0) {
                User user = list.get(0);
                String user_id = user.getUser_id();
                if (setting_goods) {
                    query.getNotifyGoods(user_id, context);    //请求通知   物品
                }
                if (setting_stall) {
                    query.getNotifyStall(user_id, context);      //请求通知  摊位
                }
            }
        }
    }

    //价格改变通知
    public  void sendNotifyGoods(ArrayList<Goods> list, Context context) {
        if (list.size() > 0) {
            String title = list.get(0).getTitle();
            String msg;
            if (list.size() == 1) {
                msg = "您收藏的[" + title + "]物品价格有变化";
            } else {
                msg = "您收藏的[" + title + "]等" + list.size() + "个物品价格有变化";
            }
            Notification not = new NotificationCompat.Builder(context)
                    .setContentTitle("校园集市")
                    .setSmallIcon(R.drawable.icon_notify_goods)
                    .setContentText(msg)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
            not.flags = Notification.FLAG_NO_CLEAR;  //点击删除
            Intent intent = new Intent(context, MyCollectionActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            not.contentIntent = pendingIntent;

            NotificationManagerCompat.from(context).notify(1, not);
        }
    }

    //摊位改变通知
    public void sendNotifyStall(ArrayList<User> list, Context context) {
        if (list.size() > 0) {
            String name = list.get(0).getName();
            String msg;
            if (list.size() == 1) {
                msg = "您收藏的[" + name + "]摊位有物品更新";
            } else {
                msg = "您收藏的[" + name + "]等" + list.size() + "个摊位有物品更新";
            }
            Notification not = new NotificationCompat.Builder(context)
                    .setContentTitle("校园集市")
                    .setSmallIcon(R.drawable.icon_notify_stall)
                    .setContentText(msg)
                    .setDefaults(Notification.DEFAULT_ALL)  //默认声音，震动等效果
                    .build();
            not.flags = Notification.FLAG_NO_CLEAR;  //点击自动删除
            Intent intent = new Intent(context, MyCollectionActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            not.contentIntent = pendingIntent;
            NotificationManagerCompat.from(context).notify(1, not);
        }
    }

    //更新物品价格
    public  void updateGoods(Context context, Editable text, String goods_id) {
        if (text.length() > 0) {
            String price = String.valueOf(text);
            UploadImpl.getInstance(context).updateGoods(Integer.parseInt(goods_id), Integer.parseInt(price));
        }
    }

}
