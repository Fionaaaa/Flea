package com.fiona.tiaozao.account;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.interactor.Interactor;
import com.tencent.tauth.Tencent;

public class AccountActivity extends AppCompatActivity {
    SimpleDraweeView draweeView;
    TextView textViewName;
    EditText editText;

    String describe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        draweeView = (SimpleDraweeView) findViewById(R.id.imageView_account);
        textViewName = (TextView) findViewById(R.id.textView_account);
        editText = (EditText) findViewById(R.id.editText_account_describe);

        draweeView.setImageURI(Uri.parse(getSharedPreferences("user", MODE_PRIVATE).getString("icon", "000")));
        textViewName.setText(getSharedPreferences("user", MODE_PRIVATE).getString("name", "000"));

        describe = getSharedPreferences("user", MODE_PRIVATE).getString("describe", "这是"+getSharedPreferences("user", MODE_PRIVATE).getString("name", "000")+"的摊位");
        editText.setText(describe);
    }

    public void returnActivity(View view) {
        if (view.getId() == R.id.textView_click_login_out) {
            //注销
            loginOut();

            getSharedPreferences("user", MODE_PRIVATE)
                    .edit()
                    .remove("isLoad")
                    .remove("icon")
                    .remove("name")
                    .remove("account")
                    .remove("flag")
                    .commit();
            getSharedPreferences("user", MODE_PRIVATE).edit().remove("describe").commit();
        } else {
            saveDescribe();
        }
        finish();
    }

    public void onClickEdit(View view) {
        view.setFocusable(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveDescribe();
        }
        return super.onKeyDown(keyCode, event);
    }

    //保存描述
    void saveDescribe() {
        String text = editText.getText().toString();
        if (!describe.equals(text)) {
            getSharedPreferences("user", MODE_PRIVATE).edit().putString("describe", text).commit();

            //更新
            Interactor.updateUserDescribe(this, text);
        }
    }

    //注销
    void loginOut(){
        Tencent.createInstance(LoginAsQQActivity.AppId, getApplicationContext()).logout(this);
    }
}
