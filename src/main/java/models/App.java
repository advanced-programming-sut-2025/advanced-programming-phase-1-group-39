package models;

import java.util.ArrayList;

public class App {
    private static App app;
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Game> games = new ArrayList<>();

    User loggedInUser = null;
    boolean stayLoggedIn = false;

    Game nowGame = null;

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
}
