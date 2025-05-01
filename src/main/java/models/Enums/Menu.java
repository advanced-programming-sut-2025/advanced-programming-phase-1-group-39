package models.Enums;

import views.View;

public enum Menu {
    LOGIN_MENU("login menu"),
    MAIN_MENU("main menu"),
    PROFILE_MENU("profile menu"),
    GAME_MENU("game menu"),
    GAME("game");

    private View menuView;
    private String name;

    Menu(String name) {
        this.name = name;
    }

    public boolean checkInput(String input) {
        return menuView.checkCommand(input);
    }

    public void setMenuView(View menuView) {
        this.menuView = menuView;
    }

    public View getMenuView() {
        return menuView;
    }

    public String getName() {
        return name;
    }
}
