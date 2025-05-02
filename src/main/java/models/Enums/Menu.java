package models.Enums;

import views.AppMenu;

public enum Menu {
    LOGIN_MENU("login menu"),
    MAIN_MENU("main menu"),
    PROFILE_MENU("profile menu"),
    GAME_MENU("game menu"),
    GAME("game");

    private AppMenu menuView;
    private String name;

    Menu(String name) {
        this.name = name;
    }

    public boolean checkInput(String input) {
        return menuView.checkCommand(input);
    }

    public void setMenuView(AppMenu menuView) {
        this.menuView = menuView;
    }

    public AppMenu getMenuView() {
        return menuView;
    }

    public String getName() {
        return name;
    }
}
