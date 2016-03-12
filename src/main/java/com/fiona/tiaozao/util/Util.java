package com.fiona.tiaozao.util;

import java.util.ArrayList;

/**
 * Created by fiona on 16-3-7.
 */
public class Util {

    //图片地址格式化   http:\/\/qzapp.qlogo.cn\/qzapp\/222222\/EE025CFD82DC8F3C2AEB923CDD2B94AB\/100
    public static String picUrlFormat(String pic) {
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

    //快速排序法
    public static void quick_sort(ArrayList<Integer>array, int left, int right) {
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
