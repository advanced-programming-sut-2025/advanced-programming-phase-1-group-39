package models;

import models.Enums.Menu;
import models.services.AppDataManager;

import java.util.ArrayList;

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
    private Player currentPlayer = null;
    private int currentGameId = 101;

    private Menu currentMenu = Menu.SIGNUP_MENU;

    private App() {

    }

    public static App getApp() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Game getNowGame() {
        return currentGame;
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

    public int getCurrentGameId() {
        return currentGameId;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
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

    public void setCurrentGameId(int gameId) {
        this.currentGameId = gameId;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }
}
