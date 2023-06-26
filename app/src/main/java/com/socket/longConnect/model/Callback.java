package com.socket.longConnect.model;

public interface Callback<T> {
    void onEvent(int code, String msg, T t);
}
