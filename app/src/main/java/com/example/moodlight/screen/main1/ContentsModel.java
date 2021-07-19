package com.example.moodlight.screen.main1;

public class ContentsModel {
    public String answer;
    public boolean commentAlram;
    public boolean likeAlram;

    public ContentsModel(String answer, boolean commentAlram, boolean likeAlram) {
        this.answer = answer;
        this.commentAlram = commentAlram;
        this.likeAlram = likeAlram;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCommentAlram() {
        return commentAlram;
    }

    public void setCommentAlram(boolean commentAlram) {
        this.commentAlram = commentAlram;
    }

    public boolean isLikeAlram() {
        return likeAlram;
    }

    public void setLikeAlram(boolean likeAlram) {
        this.likeAlram = likeAlram;
    }
}
