package com.example.moodlight.screen.main1;

public class CommunityItem {
    private String question;
    private String answer;
    private int heart;
    private int comment;
    private int todayMood;

    public CommunityItem(String question, String answer, int heart, int comment, int todayMood) {
        this.question = question;
        this.answer = answer;
        this.heart = heart;
        this.comment = comment;
        this.todayMood = todayMood;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getTodayMood() {
        return todayMood;
    }

    public void setTodayMood(int todayMood) {
        this.todayMood = todayMood;
    }
}
