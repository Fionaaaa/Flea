package com.fiona.tiaozao;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.interactor.Interactor;
import com.fiona.tiaozao.net.NetQueryImpl;
import com.fiona.tiaozao.util.ImageOprator;
import com.fiona.tiaozao.net.UploadImpl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SaleActivity extends AppCompatActivity {

    SimpleDraweeView imageView;

    String title;               //  物品名称
    String price;               //  价格
    String contact;              //  联系方式
    String describe;             //  描述
    String classify;             //  分类 （暂时为电器）
    String userID;           //  用户id (暂时为1)

    public File file;                //  物品图片

    EditText editTextTitle;
    EditText editTextPrice;
    EditText editTextContact;
    EditText editTextDescribe;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_sale);

        //获取本地用户ID
        String account = getSharedPreferences("user", MODE_PRIVATE).getString("account", "000");
        List<User> list = User.find(User.class, "account=?", account);
        if (list.size() > 0) {
            userID = String.valueOf(list.get(0).getId());
        }

        Log.d("debug", "用户ID" + userID);

        final String[] data = getResources().getStringArray(R.array.classify);

        editTextTitle = (EditText) findViewById(R.id.textView21_edit_sale_title);
        editTextPrice = (EditText) findViewById(R.id.textView23_edit_sale_price);
        editTextContact = (EditText) findViewById(R.id.textView25_edit_sale_contact);
        editTextDescribe = (EditText) findViewById(R.id.textView29_edit_sale_describe);
        spinner = (Spinner) findViewById(R.id.spinner_edit_sale);

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
        title = editTextTitle.getText().toString();
        price = editTextPrice.getText().toString();
        contact = editTextContact.getText().toString();
        describe = editTextDescribe.getText().toString();

        if (title != null && price != null && contact != null && describe != null && file != null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("price", price);
            map.put("contact", contact);
            map.put("describe", describe);
            map.put("classify", classify);
            map.put("user_id", userID);

            UploadImpl.getInstance(this).addGoods(file, map, true);

            //发网络请求(出售物品)
            NetQueryImpl.getInstance(this).getSaleGoods();

            Toast.makeText(SaleActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
            finish();

        } else {
            Toast.makeText(SaleActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
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

    /**
     * 点击添加图片
     *
     * @param view
     */
    public void clickAddPicture(View view) {
        imageView = (SimpleDraweeView) view;

        new AlertDialog.Builder(this)
                .setTitle("请选择图片")
                .setItems(new String[]{"拍照", "从本地选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                new Interactor().doCapture(SaleActivity.this);
                                break;
                            case 1:
                                new Interactor().pickPicture(SaleActivity.this);
                                break;
                        }
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //选择图片
                case App.REQUEST_PICK_PICTURE:
                    imageView.setImageURI(intent.getData());
                    file = new ImageOprator().getFileFromUri(intent.getData(), this);
                    break;
                //拍照
                case App.REQUEST_CAPTURE:
                    ImageOprator oprator = new ImageOprator();
                    Bitmap bitmap = oprator.getimage(file.getPath());
                    File nerFile = null;
                    try {
                        nerFile = oprator.saveFile(bitmap, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageURI(Uri.fromFile(nerFile));
                    break;
            }
        }
    }
}
