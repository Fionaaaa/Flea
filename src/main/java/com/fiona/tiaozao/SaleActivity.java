package com.fiona.tiaozao;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.fiona.tiaozao.interactor.GoodsInteractor;
import com.fiona.tiaozao.interactor.ImageOprator;
import com.fiona.tiaozao.net.UploadImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SaleActivity extends AppCompatActivity {

    ImageView imageView;

    String title;               //  物品名称
    String price;               //  价格
    String contact;              //  联系方式
    String describe;             //  描述
    String classify;             //  分类 （暂时为电器）
    String userID = "1";        //  用户id (暂时为1)

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
        imageView = (ImageView) view;

        new AlertDialog.Builder(this)
                .setTitle("请选择图片")
                .setItems(new String[]{"拍照", "从本地选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                new GoodsInteractor().doCapture(SaleActivity.this);
                                break;
                            case 1:
                                new GoodsInteractor().pickPicture(SaleActivity.this);
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
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), intent.getData());
                        imageView.setImageBitmap(ImageOprator.comp(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    file = ImageOprator.getFileFromUri(intent.getData(),this);
                    break;
                //拍照
                case App.REQUEST_CAPTURE:
                    Bitmap bitmap = ImageOprator.getimage(file.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                    break;
            }
        }
    }
}
