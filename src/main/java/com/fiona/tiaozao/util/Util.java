package com.fiona.tiaozao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by fiona on 16-3-7.
 */
public class Util {

    //图片地址格式化   http:\/\/qzapp.qlogo.cn\/qzapp\/222222\/EE025CFD82DC8F3C2AEB923CDD2B94AB\/100
    public  String picUrlFormat(String pic) {
        StringBuffer head = new StringBuffer();
        int start = 0;
        int end;
        while ((end = pic.indexOf("\\")) != -1) {
            head.append(pic.substring(start, end));
            pic = pic.substring(++end);
            start = end;
        }
        head.append(pic);

        return new String(head);
    }

    //复制图片
    public  File copyFile(File file,boolean isSmall) throws IOException {
        File newFile;
        if(isSmall) {
            newFile = new File(file.getParentFile(), file.getName() + "small");
        }else{
            newFile=new File(file.getParentFile(),file.getName()+"big");
        }
        if(newFile.exists()){
            newFile.delete();
            newFile.createNewFile();
        }
        FileInputStream in=new FileInputStream(file);
        FileOutputStream out=new FileOutputStream(newFile);
        byte[]buf=new byte[1024*10];
        int size;
        while ((size=in.read(buf))!=-1){
            out.write(buf,0,size);
        }
        in.close();
        out.close();
        return newFile;
    }

    //快速排序法
    public  void quick_sort(ArrayList<Integer>array, int left, int right) {
        if (left < right) {
            int i = left;
            int j = right;
            int key = array.get(i);
            while (i < j) {
                while (i < j && key <= array.get(j)) {
                    j--;
                }
                if (i < j) {
                    array.add(i++,array.get(j));
                }
                while (i < j && key > array.get(i)) {
                    i++;
                }
                if (i < j) {
                    array.add(j--,array.get(i));
                }
            }
            array.add(i,key);
            quick_sort(array, left, i - 1);
            quick_sort(array, j + 1, right);
        }
    }

}
