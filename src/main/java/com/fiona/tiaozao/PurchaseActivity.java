package com.fiona.tiaozao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.net.UploadImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseActivity extends AppCompatActivity {

    Spinner spinner;
    EditText editText;

    String classify;
    String describe;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        //获取本地用户ID
        String account = getSharedPreferences("user", MODE_PRIVATE).getString("account", "000");
        List<User> list = User.find(User.class, "account=?", account);
        if (list.size() > 0) {
            userID = String.valueOf(list.get(0).getId());
        }

        final String[] data = getResources().getStringArray(R.array.classify);

        editText = (EditText) findViewById(R.id.editText2_edit_purchase_describe);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classify = data[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 点击提交
     *
     * @param view
     */
    public void clickSure(View view) {
        describe = editText.getText().toString();

        if (describe != null) {
            Map<String, String> map = new HashMap<>();
            map.put("classify", classify);
            map.put("describe", describe);
            map.put("user_id", userID);
            UploadImpl.getInstance(this).addGoods(null, map, false);

            Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(PurchaseActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void returnActivity(View view) {
        finish();
    }

}
