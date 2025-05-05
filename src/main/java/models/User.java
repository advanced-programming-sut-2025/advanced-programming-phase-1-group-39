package models;

import java.util.ArrayList;

public class User {
    private String userName;
    private String password;
    private String nickname;
    private String email;
    private final boolean isMale;
    private SecurityQuestion securityQuestion;

    private boolean isStayLoggedIn = false;

    private int numberOfGamesPlayed;
    private int highestMoneyEarnedInASingleGame;

    private Game currentGame = null;
    private Game savedGame = null;

    private ArrayList<Player> players ;

    public User(String userName, String password, String nickname, String email, boolean isMale) {
        this.userName = userName;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.isMale = isMale;
        this.numberOfGamesPlayed = 0;
        this.highestMoneyEarnedInASingleGame = 0;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public void setHighestMoneyEarnedInASingleGame(int highestMoneyEarnedInASingleGame) {
        this.highestMoneyEarnedInASingleGame = highestMoneyEarnedInASingleGame;
    }

    public void setNumberOfGamesPlayed(int numberOfGamesPlayed) {
        this.numberOfGamesPlayed = numberOfGamesPlayed;
    }

    public void setCurrentGame(Game currentGame) { this.currentGame = currentGame; }

    public void setSavedGame(Game savedGame) { this.savedGame = savedGame; }

    public void setStayLoggedIn(boolean stayLoggedIn) { this.isStayLoggedIn = stayLoggedIn; }

    public void addPlayer(Player player) { players.add(player); }




    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public boolean getIsMale() {
        return isMale;
    }

    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }

    public int getHighestMoneyEarnedInASingleGame() {
        return highestMoneyEarnedInASingleGame;
    }

    public int getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }

    public Game getCurrentGame() { return currentGame; }

    public Game getSavedGame() { return savedGame; }

    public boolean getIsStayLoggedIn() { return isStayLoggedIn; }

    public ArrayList<Player> getPlayers() { return players; }
}
