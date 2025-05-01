package models.Enums;

import views.*;

public enum Menu {
    LOGIN_MENU("login menu", new LoginMenuView()),
    MAIN_MENU("main menu", new MainMenuView()),
    PROFILE_MENU("profile menu", new ProfileMenuView()),
    GAME_MENU("game menu", new GameMenuView()),
    GAME("game", new GameView()),
    ExitMenu("exit", null);

    private View menuView;
    private String name;

    Menu(String name, View menuView) {
        this.name = name;
    }


    public void setMenuView(View menuView) {
        this.menuView = menuView;
    }

    public void checkInput(String input) {
        menuView.checkCommand(input);

    }

    public View getMenuView() {
        return menuView;
    }

    public String getName() {
        return name;
    }
}
