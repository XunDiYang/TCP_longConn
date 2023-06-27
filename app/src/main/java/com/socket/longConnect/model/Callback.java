package com.socket.longConnect.model;

public interface Callback<T> {
    void onEvent(String from, int code, int type, String msg, T t);

//    void onEvent(int code, String msg, CMessage cMessage);
}
