package com.hrl.chaui.service;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hrl.chaui.activity.ChatActivity;
import com.hrl.chaui.bean.ConversationListBody;
import com.hrl.chaui.bean.ConversationMsgBody;
import com.hrl.chaui.bean.gpt.GptMessageBody;
import com.hrl.chaui.bean.LoginInfo;
import com.hrl.chaui.bean.ReqResultBody;
import com.hrl.chaui.util.GsonUtils;
import com.hrl.chaui.util.SPUtils;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;


public class ReqService {

    private static volatile ReqService instance;

    private String requesturl = "http://aigc.ezrpro.work/api";
    private Context mContext;
    private Gson gson = new Gson();
    private ReqService() {}
    public static ReqService getInstance(Context context) {
        if (instance == null) {
            synchronized(ReqService.class) {
                if (instance == null) {
                    instance = new ReqService();
                    instance.mContext = context;
                }
            }
        }
        return instance;
    }


    /**
     * 获取验证码
     */
    public void sendCodeMsg(String mobile, final ReqCallBack callback) {
        OkHttpUtil.getDefault(this).doGetAsync(
                HttpInfo.Builder().setUrl(requesturl+ "/user/login/captcha")
                        .addParam("mobile",mobile)
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                        callback.onFailure(result);
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 登录
     */
    public void login(String mobile, String code, final ReqCallBack callback) {
        Map<String,String> param = new HashMap<String,String>();
        param.put("mobile",mobile);
        param.put("captcha",code);
        OkHttpUtil.getDefault(this).doPostAsync(
                HttpInfo.Builder().setUrl(requesturl+ "/user/login")
                        .addParamJson(GsonUtils.objectToJsonString(param))//添加Json参数
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                        callback.onFailure(result);
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                        ReqResultBody resultBody = GsonUtils.stringToObject(result, ReqResultBody.class);
                        if(resultBody.getCode() == 200) {
                            LoginInfo loginInfo = GsonUtils.stringToObject(resultBody.getData(),LoginInfo.class);
                            callback.onSuccess(loginInfo);
                        } else {
                            callback.onFailure(resultBody.getMessage());
                        }
                    }
                });
    }
    /**
     * 获取会话列表
     */
    public void getConversationList(final ReqCallBack callback) {
        OkHttpUtil.getDefault(this).doGetAsync(
                HttpInfo.Builder().setUrl(requesturl+ "/openai/chat/conversation")
                        .addHead("token", getToken())
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                        ReqResultBody resultBody = GsonUtils.stringToObject(result, ReqResultBody.class);
                        if(resultBody.getCode() == 200) {
                            List<ConversationListBody> conversationList = gson.fromJson(resultBody.getData(), new TypeToken<List<ConversationListBody>>() {
                            }.getType());
                            callback.onSuccess(conversationList);
                        } else {
                            callback.onFailure(resultBody.getMessage());
                        }
                    }
        });
    }


    public void fetchConversationMsg(String conversationId, final ReqCallBack callback) {
        OkHttpUtil.getDefault(this).doGetAsync(
                HttpInfo.Builder().setUrl( requesturl+ "/openai/chat/conversation/messages")
                        .addHead("token", getToken())
                        .addParam("id",conversationId)
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                        ReqResultBody resultBody = GsonUtils.stringToObject(result, ReqResultBody.class);
                        if(resultBody.getCode() == 200) {
                            List<ConversationMsgBody> conversationList = gson.fromJson(resultBody.getData(), new TypeToken<List<ConversationMsgBody>>() {
                            }.getType());
                            callback.onSuccess(conversationList);
                        } else {
                            callback.onFailure(resultBody.getMessage());
                        }
                    }
                });
    }

    public void sendConversationMsg(String modelId, String conversationId, String title,final ReqCallBack callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.DAYS)
                .readTimeout(10, TimeUnit.DAYS)//这边需要将超时显示设置长一点，不然刚连上就断开，之前以为调用方式错误被坑了半天
                .build();

        Request request = new Request.Builder().url(requesturl+ "/openai/chat/conversation/send_message?modelId="+modelId+"&conversationId="+conversationId+"&message="+title)
                .addHeader("token", getToken())
                .addHeader("Accept", "text/event-stream")
                .build();
        RealEventSource realEventSource = new RealEventSource(request, new EventSourceListener() {
            private long callStartNanos;

            private void printEvent(String name) {
                long nowNanos = System.nanoTime();
                if (name.equals("callStart")) {
                    callStartNanos = nowNanos;
                }
                long elapsedNanos = nowNanos - callStartNanos;
                System.out.printf("=====> %.3f %s%n", elapsedNanos / 1000000000d, name);
            }

            @Override
            public void onOpen(EventSource eventSource, Response response) {
                printEvent("onOpen");
            }

            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                printEvent("onEvent");
                System.out.println("xxx:"+data);//请求到的数据
                GptMessageBody messageBody = GsonUtils.stringToObject(data, GptMessageBody.class);
                callback.onSuccess(messageBody);
            }

            @Override
            public void onClosed(EventSource eventSource) {
                printEvent("onClosed");
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                printEvent("onFailure");//这边可以监听并重新打开
            }
        });
        realEventSource.connect(okHttpClient);//真正开始请求的一步

    }

    public void createConversation() {
        OkHttpUtil.getDefault(this).doGetAsync(
                HttpInfo.Builder().setUrl( requesturl+"/openai/chat/create_conversation").build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                    }
        });
    }
    private String getToken(){
        SPUtils.init(this.mContext,"token");
        String token = SPUtils.getString("token","");
        return token;
    }
}
