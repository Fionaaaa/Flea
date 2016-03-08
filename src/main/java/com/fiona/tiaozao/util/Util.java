package com.fiona.tiaozao.util;

/**
 * Created by fiona on 16-3-7.
 */
public class Util {

    //图片地址格式化   http:\/\/qzapp.qlogo.cn\/qzapp\/222222\/EE025CFD82DC8F3C2AEB923CDD2B94AB\/100
    public static String picUrlFormat(String pic){
        StringBuffer head=new StringBuffer();
        int start=0;
        int end;
        while ((end=pic.indexOf("\\"))!=-1){
            head.append(pic.substring(start,end));
            pic=pic.substring(++end);
            start=end;
        }
        head.append(pic);

        return new String(head);
    }

}
