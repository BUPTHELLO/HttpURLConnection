package com.example.HttpConnection.Gson;

public class data {
    private String title;
    private String author;
    private String content;

    public data(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public data(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
