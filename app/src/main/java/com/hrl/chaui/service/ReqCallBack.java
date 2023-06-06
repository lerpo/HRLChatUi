package com.hrl.chaui.service;

public interface ReqCallBack<T> {
    void onSuccess(T result);
    void onFailure(String error);
}
