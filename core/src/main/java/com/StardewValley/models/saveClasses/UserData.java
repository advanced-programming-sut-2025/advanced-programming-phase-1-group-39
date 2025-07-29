package com.StardewValley.models.saveClasses;

import com.StardewValley.models.*;


import java.util.ArrayList;

public class UserData {
    private String userName;
    private String password;
    private String nickname;
    private String email;
    private boolean isMale;
    private SecurityQuestion securityQuestion;

    private int numberOfGamesPlayed;
    private int highestMoneyEarnedInASingleGame;

    private ArrayList<GameMetadata> gamesData;

    public UserData(User user) {
        userName = user.getUserName();
        password = user.getPassword();
        nickname = user.getNickname();
        email = user.getEmail();
        isMale = user.getIsMale();
        securityQuestion = user.getSecurityQuestion();
        numberOfGamesPlayed = user.getNumberOfGamesPlayed();
        highestMoneyEarnedInASingleGame = user.getHighestMoneyEarnedInASingleGame();
        gamesData = user.getGamesData();
    }

    public User getUser() {
        User user = new User(userName, password, nickname, email, isMale);
        user.setSecurityQuestion(securityQuestion);
        user.setNumberOfGamesPlayed(numberOfGamesPlayed);
        user.setHighestMoneyEarnedInASingleGame(highestMoneyEarnedInASingleGame);
        user.setGamesData(gamesData);

        return user;
    }

    public String getUserName() {
        return userName;
    }
}