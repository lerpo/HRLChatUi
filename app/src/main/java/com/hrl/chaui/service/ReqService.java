package com.hrl.chaui.service;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hrl.chaui.activity.LoginActivity;
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

interface BaseCallBack {
    void onSuccess(ReqResultBody info);
    void onFailure(HttpInfo info);

}

public class ReqService {

    private static volatile ReqService instance;

    private String requesturl = "http://aigc.ezrpro.work/api";
    private Context mContext;
    private Gson gson = new Gson();

    private ReqService() {
    }

    public static ReqService getInstance(Context context) {
        if (instance == null) {
            synchronized (ReqService.class) {
                if (instance == null) {
                    instance = new ReqService();
                    instance.mContext = context;
                }
            }
        }
        return instance;
    }

    private void request(Map params, String url, String method, final BaseCallBack callBack) {
        HttpInfo.Builder builder = HttpInfo.Builder();
        if (getToken().length() > 0) {
            builder.addHead("token", getToken());
        }
        if (method.equals("get")) {
            if (params != null) {
                builder.addParams(params);
            }
            builder.setUrl(requesturl + url);
            OkHttpUtil.getDefault(this).doGetAsync(
                    builder.build(),
                    new Callback() {
                        @Override
                        public void onSuccess(HttpInfo httpInfo) throws IOException {
                            String result = httpInfo.getRetDetail();
                            ReqResultBody resultBody = GsonUtils.stringToObject(result, ReqResultBody.class);
                            if (resultBody.getCode() == 40001) {
                                Toast.makeText(mContext,resultBody.getMessage(),Toast.LENGTH_SHORT).show();
                                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            } else if(resultBody.getCode() == 200) {
                                callBack.onSuccess(resultBody);
                            } else {
                                Toast.makeText(mContext,resultBody.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(HttpInfo httpInfo) throws IOException {
                           callBack.onFailure(httpInfo);
                        }
                    });

        } else if (method.equals("post")) {
            if (params != null) {
                builder.addParamJson(GsonUtils.objectToJsonString(params));
            }
            builder.setUrl(requesturl + url);
            OkHttpUtil.getDefault(this).doPostAsync(
                    builder.build(),
                    new Callback() {
                        @Override
                        public void onSuccess(HttpInfo httpInfo) throws IOException {
                            String result = httpInfo.getRetDetail();
                            ReqResultBody resultBody = GsonUtils.stringToObject(result, ReqResultBody.class);
                            callBack.onSuccess(resultBody);
                        }

                        @Override
                        public void onFailure(HttpInfo httpInfo) throws IOException {
                            callBack.onFailure(httpInfo);
                        }
                    });


        }

    }

    /**
     * 获取验证码
     */
    public void sendCodeMsg(String mobile, final ReqCallBack callback) {
        Map param = new HashMap();
        param.put("mobile", mobile);
        request(param, "/user/login/captcha", "get", new BaseCallBack() {
            @Override
            public void onSuccess(ReqResultBody info)  {
                String result = info.getData();
                callback.onFailure(result);
            }

            @Override
            public void onFailure(HttpInfo info)  {
                String result = info.getRetDetail();
                callback.onSuccess(result);
            }
        });

    }

    /**
     * 登录
     */
    public void login(String mobile, String code, final ReqCallBack callback) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("mobile", mobile);
        param.put("captcha", code);
        request(param, "/user/login", "post", new BaseCallBack() {
            @Override
            public void onSuccess(ReqResultBody info)  {

                if (info.getCode() == 200) {
                    LoginInfo loginInfo = GsonUtils.stringToObject(info.getData(), LoginInfo.class);
                    callback.onSuccess(loginInfo);
                } else {
                    callback.onFailure(info.getMessage());
                }
            }

            @Override
            public void onFailure(HttpInfo info) {
                String result = info.getRetDetail();
                callback.onFailure(result);
            }
        });

    }

    /**
     * 获取会话列表
     */
    public void getConversationList(final ReqCallBack callback) {


        request(null, "/openai/chat/conversation", "get", new BaseCallBack() {
            @Override
            public void onSuccess(ReqResultBody info) {

                if (info.getCode() == 200) {
                    List<ConversationListBody> conversationList = gson.fromJson(info.getData(), new TypeToken<List<ConversationListBody>>() {
                    }.getType());
                    callback.onSuccess(conversationList);
                } else {
                    callback.onFailure(info.getMessage());
                }
            }

            @Override
            public void onFailure(HttpInfo info) {
                String result = info.getRetDetail();
            }
        });
    }


    public void fetchConversationMsg(String conversationId, final ReqCallBack callback) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("id", conversationId);
        request(param, "/openai/chat/conversation/messages", "get", new BaseCallBack() {
            @Override
            public void onSuccess(ReqResultBody info) {

                if (info.getCode() == 200) {
                    List<ConversationMsgBody> conversationList = gson.fromJson(info.getData(), new TypeToken<List<ConversationMsgBody>>() {
                    }.getType());
                    callback.onSuccess(conversationList);
                } else {
                    callback.onFailure(info.getMessage());
                }
            }

            @Override
            public void onFailure(HttpInfo info) {
                String result = info.getRetDetail();
            }
        });

    }

    public void sendConversationMsg(String modelId, String conversationId, String title, final ReqCallBack callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.DAYS)
                .readTimeout(10, TimeUnit.DAYS)//这边需要将超时显示设置长一点，不然刚连上就断开，之前以为调用方式错误被坑了半天
                .build();

        Request request = new Request.Builder().url(requesturl + "/openai/chat/conversation/send_message?modelId=" + modelId + "&conversationId=" + conversationId + "&message=" + title)
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
                System.out.println("xxx:" + data);//请求到的数据
                GptMessageBody messageBody = GsonUtils.stringToObject(data, GptMessageBody.class);
                if(messageBody.getId() != null) {
                    callback.onSuccess(messageBody);
                } else {
                    callback.onFailure("已超过最大会话条数，请新建一个会话");
                }
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

    public void createConversation(final ReqCallBack callback) {

        request(null, "/openai/chat/create_conversation", "get", new BaseCallBack() {
            @Override
            public void onSuccess(ReqResultBody info)  {
                String result = info.getData();
            }

            @Override
            public void onFailure(HttpInfo info) {
                String result = info.getRetDetail();
            }
        });
    }

    private String getToken() {
        SPUtils.init(this.mContext, "token");
        String token = SPUtils.getString("token", "");
        return token;
    }
}
