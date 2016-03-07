package com.fiona.tiaozao.account;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.interactor.Interactor;
import com.fiona.tiaozao.util.ImageOprator;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class LoginAsQQActivity extends AppCompatActivity {

    Tencent mTencent;
    QQAuth mQQqAuth;
    String AppId = "222222";

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

            Bitmap bitmap = TencentUtil.getbitmap(picURL);
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "flea_" + openId + ".jpg");
            ImageOprator.saveFile(bitmap, file);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置登陆状态（写到选项文件）

        //保存到网络
        User user = new User(file.getAbsolutePath(), userName, openId, 1);

        Toast.makeText(LoginAsQQActivity.this, user.toString(), Toast.LENGTH_SHORT).show();

        Interactor.insertUser(user, file);
    }
}
