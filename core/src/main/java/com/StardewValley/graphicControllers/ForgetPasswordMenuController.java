package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.graphicViews.ForgetPasswordMenuScreen;
import com.StardewValley.graphicViews.LoginMenuScreen;
import com.StardewValley.models.App;
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

public class ForgetPasswordMenuController {
    private ForgetPasswordMenuScreen view;

    public void setView(ForgetPasswordMenuScreen view) {
        this.view = view;
    }

    public void handleForgetPassword() {
        if (view != null) {
            view.getChangePasswordButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    String username = view.getUsernameField().getText();
                    String answer = view.getSecurityField().getText();
                    String newPassword = view.getNewPasswordField().getText();
                    Matcher matcher;
                    App app = App.getApp();
                    if (view.getChangePasswordButton().isChecked()) {
                        if (username == null || username.equals("Enter your Username")) {
                            cleanMessages();
                            view.getUsernameErrorLabel().setText("username cannot be empty");
                        } else if (!isUsernameExist(username)) {
                            cleanMessages();
                            view.getUsernameErrorLabel().setText("Username not found");
                        } else if (!answer.equals(getUserByUsername(username).getSecurityQuestion().getAnswer())) {
                            cleanMessages();
                            view.getSecurityErrorLabel().setText("The answer you entered is incorrect");
                        } else if (newPassword == null || newPassword.equals("Enter your New Password")) {
                            cleanMessages();
                            view.getNewPasswordErrorLabel().setText("New password cannot be empty");
                        } else if ((matcher = SignupMenuCommands.Password.getMatcher(newPassword)) == null) {
                            cleanMessages();
                            view.getNewPasswordErrorLabel().setText("invalid password format");
                        } else if ((matcher = SignupMenuCommands.WeakPassword.getMatcher(newPassword)) == null) {
                            cleanMessages();
                            view.getNewPasswordErrorLabel().setText(validatePassword(newPassword).toString());
                        } else {
                            getUserByUsername(username).setPassword(HashSHA256.hashPassword(newPassword));
                            Main.getMain().setScreen(new LoginMenuScreen());
                        }
                    }
                }
            });

            view.getRandomPasswordButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getRandomPasswordButton().isChecked()) {
                        view.getNewPasswordField().setText(generateStrongPassword());
                        view.getRandomPasswordButton().setChecked(false);
                    }
                }
            });

            view.getBackButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getBackButton().isChecked()) {
                        Main.getMain().setScreen(new LoginMenuScreen());
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

    private User getUserByUsername(String username) {
        for (User user : App.getApp().getUsers()) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
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

    public static StringBuilder validatePassword(String password) {
        return SignupMenuController.validatePassword(password);
    }

    private void cleanMessages() {
        view.getUsernameErrorLabel().setText("");
        view.getSecurityErrorLabel().setText("");
        view.getNewPasswordErrorLabel().setText("");
    }


}
