package com.StardewValley.models;

import java.io.Serializable;

public class SecurityQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String question;
    private String answer;

    public SecurityQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }
}
