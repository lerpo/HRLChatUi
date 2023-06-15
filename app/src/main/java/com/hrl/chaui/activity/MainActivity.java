package com.hrl.chaui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hrl.chaui.R;
import com.hrl.chaui.adapter.ConversationAdapter;
import com.hrl.chaui.bean.ConversationListBody;
import com.hrl.chaui.service.ReqCallBack;
import com.hrl.chaui.service.ReqService;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.common_toolbar_title)
    TextView pageTitle;
    @BindView(R.id.common_toolbar_back)
    RelativeLayout btnBack;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ConversationAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnBack.setVisibility(View.GONE);
        pageTitle.setText("AI 助手");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConversationAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReqService.getInstance(MainActivity.this).getConversationList(new ReqCallBack<List<ConversationListBody>>() {
            @Override
            public void onSuccess(List<ConversationListBody> result) {
               adapter.setData(result);
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    public void onClick(View v) {

        startActivity(new Intent(this,ChatActivity.class));
    }
}
