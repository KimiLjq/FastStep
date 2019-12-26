package com.example.viewpagafragment;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.viewpagafragment.JavaBean.RegisterBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequest {

    List<Cookie> allCookie;
    SharedPreferences sp;
//    private CookieRepository cookieRepository;
    private Context context;

//    public HttpRequest(){
//    }

    public HttpRequest(Context context){
        this.context = context;
//        cookieRepository = new CookieRepository(context);
        allCookie = CookieRepository.getCookie();
    }

    public String isLogin(){
        RequestBody formBody = new FormBody.Builder().build();

        return sendRequest(formBody,IP.isLogin);
    }

    public String loginRequest(String userName,String password){
        RequestBody formBody = new FormBody.Builder()
                .add("number",userName)
                .add("password",password)
                .build();
        return sendRequest(formBody,IP.login);
    }

    public String registerRequest(RegisterBean registerBean){
        RequestBody formBody = new FormBody.Builder()
                .add("number",registerBean.getUserName())
                .add("password",registerBean.getPassword())
                .add("code",registerBean.getVerCode())
                .build();
        return sendRequest(formBody,IP.register);
    }

    public String getVercode(String email){
        RequestBody formBody = new FormBody.Builder()
                .add("mail",email)
                .build();

        return sendRequest(formBody,IP.getVercode);
    }

    public String searchRequest(String searchText){
        RequestBody formBody = new FormBody.Builder()
                .add("input",searchText)
                .build();

        return sendRequest(formBody,IP.searchURL);
    }

    public String searchTip(String searchText){
        RequestBody formBody = new FormBody.Builder()
                .add("inputReady",searchText)
                .build();

        return sendRequest(formBody,IP.searchTip);
    }

    public String getRecommend(){
        RequestBody formBody = new FormBody.Builder()
                .build();

        return sendRequest(formBody,IP.recommend);
    }

    public String addTrack(String couponId){
        RequestBody formBody = new FormBody.Builder()
                .add("couponId",couponId)
                .build();

        return sendRequest(formBody,IP.track);
    }

    public String addCollect(String couponId){
        RequestBody formBody = new FormBody.Builder()
                .add("couponId",couponId)
                .build();

        return sendRequest(formBody,IP.collect);
    }

    public String getCollect(){
        RequestBody formBody = new FormBody.Builder()
                .build();

        return sendRequest(formBody,IP.getCollect);
    }

    public String getSteps(){
        RequestBody formBody = new FormBody.Builder().build();

        return sendRequest(formBody,IP.getSteps);
    }

    private String sendRequest(RequestBody formBody,String url){
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
//                        cookieStore.put(httpUrl, list);
//                        System.out.println(httpUrl.host());
//                        for(Cookie cookie:list){
//                            System.out.println("save cookie Name:"+cookie.name());
//                            System.out.println("save cookie Path:"+cookie.path());
//                            System.out.println("save cookie Value:"+cookie.value());
//                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
//                        List<Cookie> cookies = cookieStore.get(httpUrl);
                        List<Cookie> cookies = allCookie;

                        if(cookies == null){
                            System.out.println("cookies==null");
                        }else{
                            for(Cookie cookie:cookies){
                                System.out.println("cookie Name:"+cookie.name());
                                System.out.println("cookie Path:"+cookie.path());
                                System.out.println("cookie Value:"+cookie.value());
                                System.out.println("cookieStore:"+cookieStore.entrySet().toString());
                            }
                        }
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();


        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                Headers headers = response.headers();
                HttpUrl loginUrl = request.url();
                if(allCookie == null){
                    allCookie = new ArrayList<>();
                }
                allCookie=Cookie.parseAll(loginUrl, headers);
//                CookieRepository repositroy=new CookieRepository(context);
                CookieRepository.saveCookie(allCookie);
                return response.body().string();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
