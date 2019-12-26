package com.example.viewpagafragment;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import okhttp3.Cookie;
import okhttp3.HttpUrl;


public class CookieRepository {
    Context context;
    private String configUrl="cookie.properties";
    Properties p=new Properties();
    private static List<Cookie> list;
//    public CookieRepository(){};
//
//    public CookieRepository(Context context){
//        this.context = context;
////        try{
//////            InputStream in=context.openFileInput(configUrl);
//////            p.load(in);
//////            in.close();
////        }catch(Exception e){
////            e.printStackTrace();
////            System.out.println("书池化一场");
////        }
//
//
//    }

   synchronized public static void saveCookie(List<Cookie> cookieList){

        if(list!=null)    return;
//        if(!cookieList.isEmpty()){
//            String host = cookieList.get(0).domain();
//
//            if(!host.equals(IP.ip)){
//                    return;
//            }
//        }


        if(cookieList.size()!=0) {
            System.out.println("gfggggggggggggggggggggggggg");
            if(list!=null){
                System.out.println(list.get(0).domain());
                System.out.println(list.get(0).path());
            }

            list = cookieList;
            System.out.println(list.get(0).domain());
            System.out.println(list.get(0).path());
        }
        System.out.println("saveAfter"+list.get(0).value());
    }
    public static  List<Cookie> getCookie(){
        if(list!=null) System.out.println("getfter"+list.get(0).value());
        return list;
    }

//    public void saveCookie(List<Cookie> list){
//        try{
//            Properties p=new Properties();
//            for(Cookie cookie:list){
//                p.setProperty(cookie.name(),cookie.value());
//            }
//            OutputStream out=null;
//           out=context.openFileOutput(configUrl,Context.MODE_PRIVATE);
//           p.store(out,"test");
//           out.close();
//
//
//        }catch(Exception e){
//            System.out.println("saveCookieError错误");
//            e.printStackTrace();
//        }
//
//
//    }
//
//    public  List<Cookie> getCookie(){
//        Properties p=new Properties();
//        try{
//            InputStream in=context.openFileInput(configUrl);
//            p.load(in);
//            in.close();
//        }catch(Exception e){
//            e.printStackTrace();
//            System.out.println("getCookie");
//        }
//
//        List<Cookie> list=new LinkedList<Cookie>();
//        for(Object o:p.keySet()){
//            String key=String.valueOf(o);
//            Cookie cookie = new Cookie.Builder().name("cookie").value(p.getProperty(key)).domain(IP.ip).build();
//            list.add(cookie);
//        }
//        return list;
//    }
}
