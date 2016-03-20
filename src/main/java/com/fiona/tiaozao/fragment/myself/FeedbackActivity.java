package com.fiona.tiaozao.fragment.myself;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fiona.tiaozao.R;

public class FeedbackActivity extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        editText= (EditText) findViewById(R.id.editText);
    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void returnFeedbackActivity(View view) {
        finish();
    }

    /**
     * 点击提交
     *
     * @param view
     */
    public void clickCommit(View view) {
        //auto
        if(editText.getText().length()==0){
            Toast.makeText(FeedbackActivity.this, "提交成功，谢谢您的反馈", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(FeedbackActivity.this, "你在逗我？", Toast.LENGTH_SHORT).show();
        }
    }
}
