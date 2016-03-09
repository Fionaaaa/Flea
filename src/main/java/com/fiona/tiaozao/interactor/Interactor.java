package com.fiona.tiaozao.interactor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.fiona.tiaozao.App;
import com.fiona.tiaozao.SaleActivity;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.net.NetQuery;
import com.fiona.tiaozao.net.NetQueryImpl;
import com.fiona.tiaozao.net.UploadImpl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
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
    public static void startAllNetTask(Context context) {
        NetQuery query = NetQueryImpl.getInstance(context);

        query.getSaleGoods();                //请求出售的物品

        query.getUsers();                    //获得所有用户

        query.getEmptionGoods();             //请求求购的物品

        query.getCollectUser(getId(context));//请求本人收藏的摊主

        query.getCollectGoods(getId(context));//请求本人收藏的物品

    }

    //添加一个用户
    public static void insertUser(final Context context, final User user) {
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
    public static String getId(Context context) {
        String account = context.getSharedPreferences("user", context.MODE_PRIVATE).getString("account", "000");
        List<User> list = User.find(User.class, "account=?", account);
        if (list.size() > 0) {
            return String.valueOf(list.get(0).getId());
        }
        return "000";
    }

    //从本地获得收藏的摊位
    public static ArrayList<User> getCollectUser(Context context) {
        ArrayList<User> list = new ArrayList<>();
        HashSet<String> set = (HashSet<String>) context.getSharedPreferences("user", context.MODE_PRIVATE).getStringSet("collect_user", null);
        if (set != null && set.size() > 0) {
            for (String s : set) {
                list.add(User.findById(User.class, Long.parseLong(s)));
            }
        }
        return list;
    }

    //从本地获得收藏的物品
    public static ArrayList<Goods> getCollectGoods(Context context) {
        ArrayList<Goods> list = new ArrayList<>();
        HashSet<String> set = (HashSet<String>) context.getSharedPreferences("user", context.MODE_PRIVATE).getStringSet("collect_goods", null);
        if (set != null && set.size() > 0) {
            for (String s : set) {
                list.add(Goods.findById(Goods.class, Long.parseLong(s)));
            }
        }
        return list;
    }

    //给用户集添加出售商品集
    public static ArrayList<User> getGoodsToUser(ArrayList<User> userList) {
        ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            ArrayList<Goods> goodsList = (ArrayList<Goods>) Goods.find(Goods.class, "user_id=? and flag=?", String.valueOf(userList.get(i).getId()), "1");
            if (goodsList.size() == 0) {
                list.add(userList.get(i));         //用户没有物品则不显示
            } else {
                userList.get(i).setListSale(goodsList);
            }
        }
        userList.removeAll(list);
        return userList;
    }

    //更新用户描述
    public static void updateUserDescribe(Context context,String describe){
        //更新网络
        UploadImpl.getInstance(context).updateUser(describe,getId(context));
        //更新本地
        User.executeQuery("update user set describe =? where id=?",describe,getId(context));
    }
}
