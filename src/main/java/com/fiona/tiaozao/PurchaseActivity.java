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

import com.fiona.tiaozao.net.UploadImpl;

import java.util.HashMap;
import java.util.Map;

public class PurchaseActivity extends AppCompatActivity {

    Spinner spinner;
    EditText editText;

    String classify;
    String describe;
    String userID="1";    //用户id  暂时为1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        final String[] data=getResources().getStringArray(R.array.classify);

        editText= (EditText) findViewById(R.id.editText2_edit_purchase_describe);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classify=data[position];
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
        describe= editText.getText().toString();

        if(describe!=null){
            Map<String,String>map= new HashMap<>();
            map.put("classify",classify);
            map.put("describe",describe);
            map.put("user_id",userID);
            UploadImpl.getInstance(this).addGoods(null,map,false);
        }else{
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
