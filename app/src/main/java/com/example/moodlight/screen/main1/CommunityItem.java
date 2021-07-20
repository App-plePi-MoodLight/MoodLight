package com.example.moodlight.screen.main1;

public class CommunityItem {
    private String todayQuestion;
    private String answer;
    private int heart;
    private int comment;
    private int todayMood;
    private int postNumber;
    private int isHeart;

    public CommunityItem() {
    }

    public CommunityItem(String todayQuestion, String answer, int heart, int comment, int todayMood, int postNumber, int isHeart) {
        this.todayQuestion = todayQuestion;
        this.answer = answer;
        this.heart = heart;
        this.comment = comment;
        this.todayMood = todayMood;
        this.postNumber = postNumber;
        this.isHeart = isHeart;
    }

    public String getTodayQuestion() {
        return todayQuestion;
    }

    public void setTodayQuestion(String todayQuestion) {
        this.todayQuestion = todayQuestion;
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

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    public int getIsHeart() {
        return isHeart;
    }

    public void setIsHeart(int isHeart) {
        this.isHeart = isHeart;
    }
}
