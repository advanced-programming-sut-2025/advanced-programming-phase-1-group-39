package com.StardewValley.models;


import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.services.GameAssetManager;
import com.StardewValley.network.shares.Lobby;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static App app;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Game> games = new ArrayList<>();

    private User loggedInUser = null;
    private String randomPassword = null;
    private User pendingUser = null;
    private boolean isRegisterSuccessful = false;
    private boolean stayLoggedIn = false;

    private Game currentGame = null;

    private Menu currentMenu = Menu.SIGNUP_MENU;

    // NETWORK
    private List<String> onlineUsers = new ArrayList<>();
    private List<Lobby> availableLobbies = new ArrayList<>();


    private App() {

    }

    public static App getApp() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    // NETWORK
    public List<String> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(List<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public List<Lobby> getAvailableLobbies() {
        return availableLobbies;
    }

    public void setAvailableLobbies(List<Lobby> availableLobbies) {
        this.availableLobbies = availableLobbies;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void updateUser(User user) {
        for (User appUser : users ) {
            if (appUser.getUserName().equals(user.getUserName())) {
                users.remove(appUser);
                users.add(user);
            }
        }
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public String getRandomPassword() {
        return randomPassword;
    }

    public User getPendingUser() {
        return pendingUser;
    }

    public boolean getIsRegisterSuccessful() {
        return isRegisterSuccessful;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public int getLastGameId() {
        return Game.lastGameId;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public boolean isStayLoggedIn() {
        return stayLoggedIn;
    }

    public static void setInstance(App loadedApp) {
        app = loadedApp;
    }

    //

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void setRegisterSuccessful(boolean registerSuccessful) {
        isRegisterSuccessful = registerSuccessful;
    }

    public void setRandomPassword(String randomPassword) {
        this.randomPassword = randomPassword;
    }

    public void setStayLoggedIn(boolean stayLoggedIn) {
        this.stayLoggedIn = stayLoggedIn;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void setPendingUser(User pendingUser) {
        this.pendingUser = pendingUser;
    }

    public void resetLastGameId() {
        int max = 101;
        for (User user : users) {
            for (GameMetadata gameData : user.getGamesData()) {
                max = Math.max(gameData.gameId + 1, max);
            }
        }

        Game.lastGameId = max;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }


    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUserName().equals(username)) return user;
        }
        return null;
    }

    public void removeGame(Game game) {
        games.remove(game);
    }
}
