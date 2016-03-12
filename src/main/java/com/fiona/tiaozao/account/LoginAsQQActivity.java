package com.fiona.tiaozao.account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.interactor.Interactor;
import com.fiona.tiaozao.util.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class LoginAsQQActivity extends AppCompatActivity {

    Tencent mTencent;
    QQAuth mQQqAuth;
    static String AppId = "222222";

    String userName;
    String picURL;
    String openId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTencent = Tencent.createInstance(AppId, getApplicationContext());
        mQQqAuth = QQAuth.createInstance(AppId, getApplicationContext());

        login();
    }


    public void clickLoginActivity(View view) {
        finish();
    }

    //登陆
    public void login() {
        IUiListener listener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Log.d("test", "登陆成功:" + o.toString());

                try {
                    openId = (String) ((JSONObject) o).get("openid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //取得用户信息
                UserInfo info = new UserInfo(getApplicationContext(), mQQqAuth.getQQToken());
                info.getUserInfo(new BaseUiListener());
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(LoginAsQQActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }
        };

        mTencent.login(this, "all", listener);
    }


    //回调接口
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Log.d("test", "获取用户信息：" + o.toString());
            handleUserInfo((JSONObject) o);
        }

        @Override
        public void onError(UiError e) {
            Toast.makeText(LoginAsQQActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
        }
    }

    //保存数据
    private void handleUserInfo(JSONObject object) {
        File file = null;
        try {
            userName = (String) object.get("nickname");
            picURL = (String) object.get("figureurl_qq_2");

            //存到本地（不是很需要）
//            Bitmap bitmap = TencentUtil.getbitmap(picURL);
//            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "flea_" + openId + ".jpg");
//            ImageOprator.saveFile(bitmap, file);

        } catch (JSONException e) {
            e.printStackTrace();
//        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置登陆状态（写到选项文件）
        getSharedPreferences("user", MODE_PRIVATE).edit().putBoolean("isLoad", true).commit();
        getSharedPreferences("user", MODE_PRIVATE).edit().putString("icon", Util.picUrlFormat(picURL)).commit();
        getSharedPreferences("user", MODE_PRIVATE).edit().putString("name", userName).commit();
        getSharedPreferences("user", MODE_PRIVATE).edit().putString("account", openId).commit();
        getSharedPreferences("user", MODE_PRIVATE).edit().putString("flag", "1").commit();

        Log.d("debug77", String.valueOf(getPreferences(MODE_PRIVATE).getBoolean("isLoad", false)));

        //保存到网络
        User user = new User(Util.picUrlFormat(picURL), userName, openId, 1);
        user.setDescribe("这是" + userName + "的摊位");

        Interactor.insertUser(this, user);

        Toast.makeText(LoginAsQQActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

        //开始所有的网络请求
        Interactor.startAllNetTask(LoginAsQQActivity.this);

        //清空本地设置
//        Interactor.clearSetting(LoginAsQQActivity.this);

        finish();   //结束活动
    }

}
