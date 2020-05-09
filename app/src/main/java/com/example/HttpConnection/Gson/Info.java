package com.example.HttpConnection.Gson;

import androidx.annotation.NonNull;

public class Info {
    private String status;
    private data data;
    private String msg;

    public Info(String status, com.example.HttpConnection.Gson.data data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }
    public Info(){};

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public com.example.HttpConnection.Gson.data getData() {
        return data;
    }

    public void setData(com.example.HttpConnection.Gson.data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }



    public void setMsg(String msg) {
        this.msg = msg;
    }

    @NonNull
    @Override
    public String toString() {
        String res = "{"
                + '\n'
                +"status:" + status +',' + '\n'
                +"data:" + "{" +'\n'
                +"title:" + data.getTitle() + '\n'
                +"author:" + data.getAuthor() + '\n'
                +"content" + data.getContent() + '\n'
                +"}," + '\n'
                +"msg:" + getMsg() + '\n'
                +"}";
        return res;
    }
}
