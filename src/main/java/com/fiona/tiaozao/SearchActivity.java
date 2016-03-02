package com.fiona.tiaozao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editText = (EditText) findViewById(R.id.editText_search);
        textView = (TextView) findViewById(R.id.textView_search);

        /**
         * 输入监听
         */
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().toString().length() == 0) {
                    textView.setText("取消");
                } else {
                    textView.setText("搜索");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * 键盘监听
         */
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clickCancel(editText);
                }
                return false;
            }
        });
    }

    public void clickCancel(View view) {
        String input = editText.getText().toString();
        if (input.length() == 0) {
            finish();   //  取消搜索
        } else {
            Toast.makeText(SearchActivity.this, "竹篮打水一场空，你什么也没搜索到。", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickTaoZiHai(View view) {
        Toast.makeText(SearchActivity.this, "别点这儿", Toast.LENGTH_SHORT).show();

    }
}
