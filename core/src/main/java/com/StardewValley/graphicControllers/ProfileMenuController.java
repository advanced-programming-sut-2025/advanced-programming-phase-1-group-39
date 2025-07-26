package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.graphicViews.MainMenuScreen;
import com.StardewValley.graphicViews.ProfileMenuScreen;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.Enums.commands.SignupMenuCommands;
import com.StardewValley.models.User;
import com.StardewValley.models.services.HashSHA256;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

public class ProfileMenuController {
    private ProfileMenuScreen view;

    public void setView(ProfileMenuScreen view) {
        this.view = view;
    }

    public void handleProfileMenu() {
        if (view != null) {
            String newUsername = view.getChangeUsernameField().getText();
            String newPassword = view.getChangePasswordField().getText();
            String newNickname = view.getChangeNicknameField().getText();
            String newEmail = view.getChangeEmailField().getText();
            App app = App.getApp();
            User user = app.getLoggedInUser();

            view.getChangeUsernameButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    Matcher matcher;
                    if (newUsername == null || newUsername.equals("Enter your new Username")) {
                        cleanMessages();
                        view.getChangeUsernameErrorLabel().setText("Username cannot be empty");
                    } else if (isUsernameExist(newUsername)) {
                        cleanMessages();
                        view.getChangeUsernameErrorLabel().setText("Username already exists");
                    } else if ((matcher = SignupMenuCommands.UserName.getMatcher(newUsername)) == null) {
                        cleanMessages();
                        view.getChangeUsernameErrorLabel().setText("the username format is invalid");
                    } else {
                        app.getLoggedInUser().setUserName(newUsername);
                        view.getUsername().setText("Username : " + newUsername);
                    }
                }
            });

            view.getChangePasswordButton().addListener(new ChangeListener() {
                Matcher matcher;
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (newPassword == null || newPassword.equals("Enter your new Password")) {
                        cleanMessages();
                        view.getChangePasswordErrorLabel().setText("Password cannot be empty");
                    } else if (HashSHA256.checkPassword(newPassword, user.getPassword())) {
                        cleanMessages();
                        view.getChangePasswordErrorLabel().setText("the new password is the same as the previous one");
                    } else if ((matcher = SignupMenuCommands.Password.getMatcher(newPassword)) == null) {
                        cleanMessages();
                        view.getChangePasswordErrorLabel().setText("the new Password format is invalid");
                    } else if ((matcher = SignupMenuCommands.WeakPassword.getMatcher(newPassword)) == null) {
                        cleanMessages();
                        view.getChangePasswordErrorLabel().setText(generateStrongPassword());
                    } else {
                        App.getApp().getLoggedInUser().setPassword(HashSHA256.hashPassword(newPassword));
                        view.getChangePasswordErrorLabel().setText("Your new Password Changed Successfully");
                    }
                }
            });

            view.getChangeNicknameField().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (newNickname == null || newNickname.equals("Enter your new Nickname")) {
                        cleanMessages();
                        view.getChangeNicknameErrorLabel().setText("Nickname cannot be empty");
                    } else {
                        App.getApp().getLoggedInUser().setNickname(newNickname);
                        view.getNickname().setText("Nickname : " + newNickname);
                    }
                }
            });

            view.getChangeEmailButton().addListener(new ChangeListener() {
                Matcher matcher;
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (newEmail == null || newEmail.equals("Enter your new Email")) {
                        cleanMessages();
                        view.getChangeEmailErrorLabel().setText("Email cannot be empty");
                    } else if (newEmail.equals(user.getEmail())) {
                        cleanMessages();
                        view.getChangeEmailErrorLabel().setText("The new email is the same as the previous one");
                    } else if ((matcher = SignupMenuCommands.Email.getMatcher(newEmail)) == null) {
                        cleanMessages();
                        view.getChangeEmailErrorLabel().setText("The email format is invalid");;
                    } else {
                        App.getApp().getLoggedInUser().setEmail(newEmail);
                        view.getChangeEmailErrorLabel().setText("Your new Email Changed Successfully");
                    }
                }

            });

            view.getBackButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    App.getApp().setCurrentMenu(Menu.MAIN_MENU);
                    Main.getMain().switchScreen(new MainMenuScreen());
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

    public static String generateStrongPassword() {
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String special = "?<>,\"';:/|}{+=)*&^%$#!";
        String all = lower + upper + digits + special;

        Random random = new Random();
        List<Character> passwordChars = new ArrayList<>();

        // حداقل ۲ کاراکتر از هر نوع
        for (int i = 0; i < 2; i++) {
            passwordChars.add(lower.charAt(random.nextInt(lower.length())));
            passwordChars.add(upper.charAt(random.nextInt(upper.length())));
            passwordChars.add(digits.charAt(random.nextInt(digits.length())));
            passwordChars.add(special.charAt(random.nextInt(special.length())));
        }

        // بقیه رو با کاراکترهای ترکیبی پر کن
        while (passwordChars.size() < 16) {
            passwordChars.add(all.charAt(random.nextInt(all.length())));
        }

        // پسورد رو کاملاً درهم بریز
        Collections.shuffle(passwordChars);

        // تبدیل به رشته
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }

    private void cleanMessages() {
        view.getChangeUsernameErrorLabel().setText("");
        view.getChangePasswordErrorLabel().setText("");
        view.getChangeNicknameErrorLabel().setText("");
        view.getChangeEmailErrorLabel().setText("");
    }
}
