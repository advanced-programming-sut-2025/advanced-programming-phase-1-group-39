package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.controllers.ProfileMenuController;
import com.StardewValley.graphicViews.ForgetPasswordMenuView;
import com.StardewValley.graphicViews.LoginMenuView;
import com.StardewValley.graphicViews.MainMenuView;
import com.StardewValley.graphicViews.SignupMenuView;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.User;
import com.StardewValley.models.services.HashSHA256;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class LoginMenuController {
    private LoginMenuView view;

    public void setView(LoginMenuView view) {
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
                        if (username == null || username.equals("Enter your Username")) {
                            cleanMessages();
                            view.getUsernameErrorLabel().setText("Username cannot be empty");
                            view.getLoginButton().setChecked(false);
                        } else if (!isUsernameExist(username)) {
                            cleanMessages();
                            view.getUsernameErrorLabel().setText("Introduce local variable");
                            view.getLoginButton().setChecked(false);
                        } else if (!HashSHA256.checkPassword(password, App.getApp().getUsers().get(getIndexInUsers(username)).getPassword())) {
                            cleanMessages();
                            view.getPasswordErroeLabel().setText("The password is incorrect");
                            view.getLoginButton().setChecked(false);
                        } else if (view.getStayLoggedInCheckBox().isChecked()) {
                            App.getApp().setStayLoggedIn(true);
                            App.getApp().setLoggedInUser(getUserByUsername(username));
                            Main.getMain().setScreen(new MainMenuView());
                        } else {
                            App.getApp().setLoggedInUser(getUserByUsername(username));
                            Main.getMain().setScreen(new MainMenuView());
                        }
                    }
                }
            });

            view.getForgotPasswordButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getForgotPasswordButton().isChecked()) {
                        Main.getMain().setScreen(new ForgetPasswordMenuView());
                    }
                }
            });

            view.getBackButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getBackButton().isChecked()) {
                        Main.getMain().setScreen(new SignupMenuView());
                    }
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

    private int getIndexInUsers(String username) {
        return ProfileMenuController.getIndexInUsers(username);
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
