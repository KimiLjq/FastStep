package com.example.viewpagafragment;

public class IP {
    public static String ip = "172.16.83.38";


    public static String searchURL = "http://"+ip+":8080/fastStep/searchCoupon.action";
    public static String searchTip = "http://"+ip+":8080/fastStep/searchCoupon_ready.action";
    public static String recommend = "http://"+ip+":8080/fastStep/getCouponArr.action";
    public static String track = "http://"+ip+":8080/fastStep/newStep.action";
    public static String collect="http://"+ip+":8080/fastStep/newCollect.action";
    public static String getCollect = "http://"+ip+":8080/fastStep/getCollects.action";
    public static String getSteps = "http://"+ip+":8080/fastStep/getSteps.action";
    public static String login = "http://"+ip+":8080/fastStep/login.action";
    public static String register = "http://"+ip+":8080/fastStep/RegisterActivity.action";
    public static String getVercode = "http://"+ip+":8080/fastStep/sendMail.action";
    public static String isLogin = "http://"+ip+":8080/fastStep/isLogin.action";
}
