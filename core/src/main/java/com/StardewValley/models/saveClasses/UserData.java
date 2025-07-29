package com.StardewValley.models.saveClasses;

import com.StardewValley.models.Game;
import com.StardewValley.models.Player;
import com.StardewValley.models.SecurityQuestion;
import com.StardewValley.models.User;


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

    private int currentGameId;
    private ArrayList<Integer> playersIds = new ArrayList<>();

    public UserData(User user) {
        userName = user.getUserName();
        password = user.getPassword();
        nickname = user.getNickname();
        email = user.getEmail();
        isMale = user.getIsMale();
        securityQuestion = user.getSecurityQuestion();
        numberOfGamesPlayed = user.getNumberOfGamesPlayed();
        highestMoneyEarnedInASingleGame = user.getHighestMoneyEarnedInASingleGame();

        currentGameId = -1;
        Game game = user.getCurrentGame();
        if (game != null) {
            currentGameId = user.getCurrentGame().getId();
        }

        for (Player player : user.getPlayers()) {
            playersIds.add(player.getId());
        }
    }

    public User getUser() {
        User user = new User(userName, password, nickname, email, isMale);
        user.setSecurityQuestion(securityQuestion);
        user.setNumberOfGamesPlayed(numberOfGamesPlayed);
        user.setHighestMoneyEarnedInASingleGame(highestMoneyEarnedInASingleGame);

        return user;
    }
}