package models;

import models.Enums.Menu;
import services.UsersDataManager;

import java.util.ArrayList;

public class App {
    private static App app;
    private ArrayList<User> users = UsersDataManager.loadUsers();
    private ArrayList<Game> games = new ArrayList<>();


    private User loggedInUser = null;
    private String randomPassword = null;
    private User pendingUser = null;
    private boolean isRegisterSuccessful = false;
    private boolean stayLoggedIn = false;

    private Game nowGame = null;

    private Menu currentMenu = Menu.LOGIN_MENU;

    private App() {}

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
        return nowGame;
    }

    public Menu getCurrentMenu() { return currentMenu; }

    public ArrayList<User> getUsers() { return users; }

    public String getRandomPassword() { return randomPassword; }

    public User getPendingUser() { return pendingUser; }

    public boolean isRegisterSuccessful() { return isRegisterSuccessful; }


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
}
