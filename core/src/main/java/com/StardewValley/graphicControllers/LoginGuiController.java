package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.graphicViews.ForgetPasswordMenuScreen;
import com.StardewValley.graphicViews.LoginMenuScreen;
import com.StardewValley.graphicViews.MainMenuScreen;
import com.StardewValley.graphicViews.SignupMenuScreen;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.User;
import com.StardewValley.models.services.HashSHA256;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class LoginGuiController {
    private LoginMenuScreen view;

    public void setView(LoginMenuScreen view) {
        this.view = view;
    }

    public void handleLogin() {
        if (view != null) {

            view.getStayLoggedInCheckBox().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {

                }
            });

            view.getLoginButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    String username = view.getUsernameField().getText();
                    String password = view.getPasswordField().getText();
                    if (view.getLoginButton().isChecked()) {
                        if (username == null) {
                            cleanMessages();
                            view.getUsernameErrorLabel().setText("Username field cannot be empty");
                            view.getLoginButton().setChecked(false);
                        } else if (!isUsernameExist(username)) {
                            cleanMessages();
                            view.getUsernameErrorLabel().setText("This username does not exist");

                        } else if (!HashSHA256.checkPassword(password, App.getApp().getUserByUsername(username).getPassword())) {
                            cleanMessages();
                            view.getPasswordErroeLabel().setText("The password is incorrect");
                        } else if (view.getStayLoggedInCheckBox().isChecked()) {
                            App.getApp().setStayLoggedIn(true);
                            App.getApp().setLoggedInUser(getUserByUsername(username));
                            Main.getMain().switchScreen(new MainMenuScreen());
                        } else {
                            App.getApp().setLoggedInUser(App.getApp().getUserByUsername(username));
                            App.getApp().setCurrentMenu(Menu.MAIN_MENU);
                            Main.getMain().switchScreen(new MainMenuScreen());
                        }
                    }
                }
            });

            view.getForgotPasswordButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    App.getApp().setCurrentMenu(Menu.FORGET_PASSWORD_MENU);
                    Main.getMain().switchScreen(new ForgetPasswordMenuScreen());
                }
            });

            view.getBackButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    App.getApp().setCurrentMenu(Menu.SIGNUP_MENU);
                    Main.getMain().switchScreen(new SignupMenuScreen());
                }
            });
        }
    }

    // Auxiliary functions :
    private boolean isUsernameExist(String username) {
        for (User user : App.getApp().getUsers()) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private User getUserByUsername(String username) {
        for (User user : App.getApp().getUsers()) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private void cleanMessages() {
        view.getUsernameErrorLabel().setText("");
        view.getPasswordErroeLabel().setText("");
    }
}
