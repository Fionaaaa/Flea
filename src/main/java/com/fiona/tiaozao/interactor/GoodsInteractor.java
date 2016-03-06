package com.fiona.tiaozao.interactor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.fiona.tiaozao.App;
import com.fiona.tiaozao.SaleActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fiona on 16-3-6.
 */
public class GoodsInteractor {

    //从本地取一张照片
    public void pickPicture(Context context) {

        Intent intent = new Intent();
        //设置动作
        intent.setAction(Intent.ACTION_PICK);

        //设置数据
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Uri uri = Uri.fromFile(path);
//        Uri uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        intent.setDataAndType(uri, "image/*");

        //带结果的intent 请求码
        SaleActivity saleActivity = (SaleActivity) context;
        saleActivity.startActivityForResult(intent, App.REQUEST_PICK_PICTURE);
    }

    //拍照
    public void doCapture(Context context) {
        SaleActivity saleActivity = (SaleActivity) context;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //格式化照片文件名字
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String fileName = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        //照片文件位置
        saleActivity.file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "flea_" + fileName + ".jpg");

        Uri imageUri = Uri.fromFile(saleActivity.file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        saleActivity.startActivityForResult(intent, App.REQUEST_CAPTURE);
    }

}
