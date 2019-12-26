package com.example.viewpagafragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.viewpagafragment.JavaBean.RegisterBean;
import com.example.viewpagafragment.JavaBean.UserMessageBean;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar = null;
    private EditText registerAccount;
    private EditText registerEmail;
    private EditText registerPassword;
    private EditText registerVerCode;
    private Button registerSubmit;
    private Button bt_getVerCode;

    private UserMessageBean userMessageBean;
    private String userMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setLister();


    }

    private void init(){
        toolbar = findViewById(R.id.rtoolbar);
        registerSubmit = findViewById(R.id.registerSubmit);
        registerAccount = findViewById(R.id.registerAccount);
        registerEmail = findViewById(R.id.registerE_mail);
        registerPassword = findViewById(R.id.registerPassword);
        registerVerCode = findViewById(R.id.registerVerCode);
        bt_getVerCode = findViewById(R.id.bt_getVercode);
    }

    private void setLister(){
        final HttpRequest httpRequest =new HttpRequest(RegisterActivity.this);

        bt_getVerCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = registerEmail.getText().toString();
                new Thread() {
                    @Override
                    public void run() {
                        //传递参数获取响应内容
                        String result = httpRequest.getVercode(email);
                        //判断数据库查询出来值是否与输入的值相等
                        if (result != null && !result.isEmpty()){
                            /****************/
                        }
                        else {
                            Log.e("LoginResult","获取验证码出错");
                        }
                    }
                }.start();
            }
        });

        registerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RegisterBean registerBean = new RegisterBean();
                registerBean.setUserName(registerAccount.getText().toString());
                registerBean.setPassword(registerPassword.getText().toString());
                registerBean.setEmail(registerEmail.getText().toString());
                registerBean.setVerCode(registerVerCode.getText().toString());

                userMessage = null;

                new Thread() {
                    @Override
                    public void run() {
                        //传递参数获取响应内容
                        String result = httpRequest.registerRequest(registerBean);
                        //判断数据库查询出来值是否与输入的值相等
                        if (result != null && !result.isEmpty()){
                            userMessage = result;
                        }
                        else {
                            Log.e("LoginResult","注册失败");
                        }

                        int code = 200;
                        if(userMessage != null){
                            code = parseJSON(userMessage);
                        }

                        System.out.println(code);
                        if(code==200){
                            Intent intent = new Intent();
                            intent.putExtra("userMessage",userMessage);
                            setResult(1010,intent);
                            finish();
                        }else{
                            System.out.println(code+"注册失败");
                        }
                    }
                }.start();


            }
        });
    }


    private int parseJSON(String jsonData){
        int errcode = 500;
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            String data = null;
            String message;

            errcode = jsonObject.getInt("errcode");

            System.out.println(errcode);


            data = jsonObject.getString("data");
            message = jsonObject.getString("massage");

            JSONObject dataJson = new JSONObject(data);
            userMessageBean.setId(dataJson.getInt("id"));
            userMessageBean.setAccount(dataJson.getString("number"));

        }catch (Exception e){
            Log.e("parseJSON","json数据解析失败");
        }finally {
            return errcode;
        }

    }
}
