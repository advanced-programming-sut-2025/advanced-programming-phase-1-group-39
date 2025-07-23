package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.graphicViews.LoginMenuScreen;
import com.StardewValley.graphicViews.SecurityQuestionMenuScreen;
import com.StardewValley.graphicViews.SignupMenuScreen;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.commands.SignupMenuCommands;
import com.StardewValley.models.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

public class SignupMenuController {
    private SignupMenuScreen view;

    public void setView(SignupMenuScreen view) {
        this.view = view;
    }

    public void handleSignup() {
        if (view != null) {
            view.getSignUpButton().addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    String username = view.getUsernameField().getText();
                    String password = view.getPasswordField().getText();
                    String confirmPassword = view.getConfirmPasswordField().getText();
                    String nickname = view.getNicknameField().getText();
                    String email = view.getEmailField().getText();
                    if (view.getSignUpButton().isChecked()) {
                        Matcher matcher;
                        // --- username ---
                        if (username == null || username.equals("Enter your Username")) {
                            cleanMessage();
                            view.getUsernameErrorLabel().setText("Username cannot be empty");
                            view.getSignUpButton().setChecked(false);
                        }
                            if (!isUsernameUnique(username)) {
                                cleanMessage();
                                String newUsername = getUniqueName(username);
                                view.getUsernameErrorLabel().setText(("This username is already taken. You can use this : " +
                                        "((" + newUsername + "))."));
                                view.getSignUpButton().setChecked(false);
                            } else if ((matcher = SignupMenuCommands.UserName.getMatcher(username)) == null) {
                                cleanMessage();
                                view.getUsernameErrorLabel().setText("The username format is incorrect");
                                view.getSignUpButton().setChecked(false);
                            }
                            // --- password ---
                            else if (password == null || password.equals("Enter your Password")) {
                                cleanMessage();
                                view.getPasswordErrorLabel().setText("the password cannot be empty");
                                view.getSignUpButton().setChecked(false);
                            } else if ((matcher = SignupMenuCommands.Password.getMatcher(password)) == null) {
                                cleanMessage();
                                view.getPasswordErrorLabel().setText("The password format is incorrect");
                                view.getSignUpButton().setChecked(false);
                            } else if ((matcher = SignupMenuCommands.WeakPassword.getMatcher(password)) == null) {
                                cleanMessage();
                                StringBuilder output = validatePassword(password);
                                view.getPasswordErrorLabel().setText(output.toString());
                                view.getSignUpButton().setChecked(false);
                            } else if (!password.equals(confirmPassword)) {
                                cleanMessage();
                                view.getConfirmPasswordErrorLabel().setText("Password and confirmation do not match");
                                view.getSignUpButton().setChecked(false);
                            }
                            // --- nickname ---
                            else if (nickname == null || nickname.equals("Enter your Nickname")) {
                                cleanMessage();
                                view.getNicknameErrorLabel().setText("The nickname cannot be empty");
                                view.getSignUpButton().setChecked(false);
                            }
                            // --- email ---
                            else if ((matcher = SignupMenuCommands.Email.getMatcher(email)) == null) {
                                cleanMessage();
                                view.getEmailErrorLabel().setText("The email address you entered is invalid");
                                view.getSignUpButton().setChecked(false);
                            }
                            // --- creat an account ---
                            else {
                                App app = App.getApp();
                                boolean isMale = view.getGenderField().getSelected().equals("Male");
                                app.setPendingUser(new User(username, password, nickname, email, isMale));
                                Main.getMain().setScreen(new SecurityQuestionMenuScreen());
                            }
                    }
                }
            });

            view.getLoginButton().addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getLoginButton().isChecked()) {
                        Main.getMain().setScreen(new LoginMenuScreen());
                    }
                }
            });

            view.getExitButton().addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getExitButton().isChecked()) {
                        Gdx.app.exit();
                    }
                }
            });

            view.getRandomPasswordButton().addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getRandomPasswordButton().isChecked()) {
                        String randomPass = generateStrongPassword();
                        view.getPasswordField().setText(randomPass);
                        view.getConfirmPasswordField().setText(randomPass);
                        view.getRandomPasswordButton().setChecked(false);
                    }
                }
            });
        }
    }

    // Auxiliary functions :

    private boolean isUsernameUnique(String username) {
        for (User user : App.getApp().getUsers()) {
            if (user.getUserName().equals(username)) {
                return false;
            }
        }
        return true;
    }

    private String getUniqueName(String baseName) {
        Random random = new Random();
        String uniqueName = baseName;

        while (!isUsernameUnique(uniqueName)) {
            int randNum = random.nextInt(100) + 1;
            uniqueName = baseName + "-" + randNum;
        }

        return uniqueName;
    }

    public static StringBuilder validatePassword(String password) {
        StringBuilder errors = new StringBuilder();

        if (password.length() < 8) {
            errors.append("Password must be at least 8 characters long.\n");
        } else if (!password.matches(".*[a-z].*")) {
            errors.append("Password must include at least one lowercase letter.\n");
        } else if (!password.matches(".*[A-Z].*")) {
            errors.append("Password must include at least one uppercase letter.\n");
        } else if (!password.matches(".*[0-9].*")) {
            errors.append("Password must contain at least one digit.\n");
        } else if (!password.matches(".*[?<>,\"';:/\\\\|\\]\\[\\}\\{\\+=\\)\\(\\*&\\^%\\$#!].*")) {
            errors.append("Password must contain at least one special character (e.g. !, @, #, $...). \n");
        }
        errors.deleteCharAt(errors.length() - 1);
        return errors;
    }

    public void cleanMessage() {
        view.getUsernameErrorLabel().setText("");
        view.getPasswordErrorLabel().setText("");
        view.getConfirmPasswordErrorLabel().setText("");
        view.getNicknameErrorLabel().setText("");
        view.getEmailErrorLabel().setText("");
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


}
