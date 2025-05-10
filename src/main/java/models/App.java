package models;

import models.Enums.Menu;

import java.util.ArrayList;

public class App {
    private static App app;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Game> games = new ArrayList<>();

    private User loggedInUser = null;
    private boolean stayLoggedIn = false;

    private Game currentGame = null;

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

    public Game getCurrentGame() {
        return currentGame;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public User getUserByPlayer(Player player) {
        for (User user : users) {
            for (Player p : user.getPlayers()) {
                if (player.equals(p)) return user;
            }
        }
        return null;
    }
}