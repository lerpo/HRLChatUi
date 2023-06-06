package com.hrl.chaui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hrl.chaui.R;
import com.hrl.chaui.bean.LoginInfo;
import com.hrl.chaui.service.ReqCallBack;
import com.hrl.chaui.service.ReqService;
import com.hrl.chaui.util.GsonUtils;
import com.hrl.chaui.util.SPUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText mPhoneEditText;
    private EditText mCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPhoneEditText = findViewById(R.id.phone_number);
        mCodeEditText = findViewById(R.id.verification_code);
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.get_verification_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneEditText.getText().toString();
                if (phone.length() == 11) {
                    ReqService.getInstance(LoginActivity.this).sendCodeMsg(phone, new ReqCallBack() {
                        @Override
                        public void onSuccess(Object result) {
                           Toast.makeText(LoginActivity.this,result.toString(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(LoginActivity.this,error,Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this,"请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void login() {
        String phone = mPhoneEditText.getText().toString();
        String code = mCodeEditText.getText().toString();
        ReqService.getInstance(LoginActivity.this).login(phone,code, new ReqCallBack<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo info) {
                SPUtils.init(LoginActivity.this,"token");
                SPUtils.putString("token",info.getToken());
                SPUtils.putString("userInfo", GsonUtils.objectToJsonString(info.getUserInfo()));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}