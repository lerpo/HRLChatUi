package com.hrl.chaui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hrl.chaui.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.common_toolbar_title)
    TextView pageTitle;
    @BindView(R.id.common_toolbar_back)
    RelativeLayout btnBack;
    @BindView(R.id.view1)
    LinearLayout view1;
    @BindView(R.id.view2)
    LinearLayout view2;
    @BindView(R.id.view3)
    LinearLayout view3;
    @BindView(R.id.view4)
    LinearLayout view4;
    @BindView(R.id.view5)
    LinearLayout view5;
    @BindView(R.id.view6)
    LinearLayout view6;
    @BindView(R.id.view7)
    LinearLayout view7;
    @BindView(R.id.view8)
    LinearLayout view8;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnBack.setVisibility(View.GONE);
        pageTitle.setText("嗨！Chat");
        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
        view4.setOnClickListener(this);
        view5.setOnClickListener(this);
        view6.setOnClickListener(this);
        view7.setOnClickListener(this);
        view8.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = 0;
        switch (v.getId()) {
            case R.id.view1:
                // 点击了 Button 1
                break;
            case R.id.view2:
                // 点击了 Button 2
                break;
            case R.id.view3:
                // 点击了 Button 2
                break;
            case R.id.view4:
                // 点击了 Button 2
                break;
            case R.id.view5:
                // 点击了 Button 2
                break;
            case R.id.view6:
                // 点击了 Button 2
                break;
            case R.id.view7:
                // 点击了 Button 2
                break;
            case R.id.view8:
                // 点击了 Button 2
                break;

            default:
                // 其他控件的点击事件
                break;
        }
        startActivity(new Intent(this,ChatActivity.class));
    }
}
