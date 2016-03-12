package com.fiona.tiaozao.account.sinaWeibo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.interactor.Interactor;
import com.fiona.tiaozao.util.Util;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class WeiboLoginActivity extends AppCompatActivity implements OnClickListener {

    private Context context;
    private IWeiboShareAPI iWeiboShareAPI;
    private final static String APP_ID = "3754304876";
    private AuthInfo authInfo;
    private SsoHandler ssoHandler;
    private Oauth2AccessToken accessToken;
    private TextView textView;
    private User user;
    //新浪微博回调页
    private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    private  static final String SCOPE = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        iWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context,APP_ID);
        textView = (TextView) findViewById(R.id.textView);

        onClick(null);
    }
    @Override
    public void onClick(View v) {
        authInfo =  new AuthInfo(context,APP_ID,REDIRECT_URL,SCOPE);
        ssoHandler = new SsoHandler(WeiboLoginActivity.this,authInfo);
        ssoHandler.authorize(new AuthListener() {
        });
    }

    public void clickLoginActivity(View view) {
        finish();
    }

    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle bundle) {
            accessToken = Oauth2AccessToken.parseAccessToken(bundle);//从Bundle中解析Token
            String phoneNum = accessToken.getPhoneNum();//获取用户输入的电话号码信息


            //用户授权
            if (accessToken.isSessionValid()) {

                AccessTokenKeeper.writeAccessToken(WeiboLoginActivity.this, accessToken); // 保存Token
                Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT)
                        .show();
            }else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = bundle.getString("code");
                String message = "授权失败";
                if(!TextUtils.isEmpty(code)){
                    message = message + "\nObtained the code: "+code ;
                }
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UsersAPI usersAPI = new UsersAPI(context,APP_ID,accessToken);
                    usersAPI.show(Long.valueOf(accessToken.getUid()),new SinaRequestListener());
                }
            }).start();
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }

        @Override
        public void onCancel() {
            Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();
        }
    }

    //获取用户信息
    public class SinaRequestListener implements RequestListener {//微博请求接口

        @Override
        public void onComplete(String s) {

            try {

                JSONObject jsonObject = new JSONObject(s);
                String idStr = jsonObject.getString("idstr");// 唯一标识符(uid)
                String name = jsonObject.getString("name");// 姓名
                String avatarHd = jsonObject.getString("avatar_hd");// 头像

                user=new User();
                user.setFlag(0);
                user.setIcon(avatarHd);
                user.setName(name);
                user.setAccount(idStr);

                user.setDescribe("这是" + name + "的摊位");

                //保存用户信息
                saveUserData(user);

                Toast.makeText(WeiboLoginActivity.this, "用户名："+name, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
                e.printStackTrace();
            }

    }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.i("mylog", "Auth exception : " + e.getMessage());
        }
    }

    //保存用户
    private void saveUserData(User user) {
        //选项存储
        getSharedPreferences("user", MODE_PRIVATE).edit().putBoolean("isLoad", true).commit();
        getSharedPreferences("user", MODE_PRIVATE).edit().putString("icon", user.getIcon()).commit();
        getSharedPreferences("user", MODE_PRIVATE).edit().putString("name", user.getName()).commit();
        getSharedPreferences("user", MODE_PRIVATE).edit().putString("account", user.getAccount()).commit();
        getSharedPreferences("user", MODE_PRIVATE).edit().putString("flag", "0").commit();
        getSharedPreferences("user", MODE_PRIVATE).edit().putString("describe", user.getDescribe()).commit();

        //服务器
        Interactor.insertUser(this,user);

        Toast.makeText(WeiboLoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

        //开始所有的网络请求
        Interactor.startAllNetTask(WeiboLoginActivity.this);

        //清空本地设置
//        Interactor.clearSetting(WeiboLoginActivity.this);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
