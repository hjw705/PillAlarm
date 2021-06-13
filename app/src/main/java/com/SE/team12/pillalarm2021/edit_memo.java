package com.SE.team12.pillalarm2021;

public class edit_memo  {

    private String title;   //제목
    private String date;  //날짜
    private String content;   //내용

    public edit_memo() {
        this.title = "아직 입력하지 않았습니다.";
    }

    public edit_memo(String title,String date,String dday) {
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public String getTitle() { return title; }
    public String getDate(){ return date; }
    public String getContent(){ return content; }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
