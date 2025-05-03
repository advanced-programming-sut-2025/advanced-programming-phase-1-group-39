package controllers;

import models.App;
import models.Enums.Menu;
import models.Enums.commands.SignupMenuCommands;
import models.Result;
import models.User;
import services.LoginPersistence;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

public class LoginMenuController {

    public Result login(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String stayLoggedIn = matcher.group("stayLoggedIn");

        if (!isUsernameExist(username)) {
            return new Result(false, "Username not found.");
        } else if (!isPasswordCorrect(username, password)) {
            return new Result(false, "The password is incorrect.");
        } else if (stayLoggedIn != null) {
            LoginPersistence.clearLoggedInUser();
            LoginPersistence.saveLoggedInUser(username);
            App.getApp().setLoggedInUser(getUserByUsername(username));
            App.getApp().setCurrentMenu(Menu.MAIN_MENU);
            return new Result(true, "login was successful. you are now in the main menu.");
        } else {
            App.getApp().setLoggedInUser(getUserByUsername(username));
            App.getApp().setCurrentMenu(Menu.MAIN_MENU);
            return new Result(true, "login was successful. you are now in the main menu.");
        }
    }

    public Result forgetPassword(Matcher matcher) {
        String username = matcher.group("username");

        if (!isUsernameExist(username)) {
            return new Result(false, "Username not found.");
        } else {
            App.getApp().setPendingUser(getUserByUsername(username));
            return new Result(true, "please enter the answer to your security question.");
        }
    }

    public Result checkAnswer(Matcher matcher) {
        String answer = matcher.group("answer");

        if (!answer.equals(App.getApp().getPendingUser().getSecurityQuestion().getAnswer())) {
            return new Result(false, "the answer you entered is incorrect. please try again to log in.");
        } else {
            App.getApp().getPendingUser().setPassword(null);
            return new Result(true, "enter your new password. if you want to request a random password, type the word 'random'.");

        }
    }

    public Result setNewPassword(Matcher matcher) {
        String newPassword = matcher.group("newPassword");
        Matcher matcher1;

        if (newPassword.equals("random")) {
            String randomPassword = generateRandomPassword();
            App.getApp().getPendingUser().setPassword(randomPassword);
            return new Result(true, "our random password: " + randomPassword + "\n" +
                    "please enter the new password to log in.");
        } else {
            if ((matcher1 = SignupMenuCommands.Password.getMatcher(newPassword)) == null) {
                return new Result(false, "invalid password format. You can use letters, numbers, and special characters in your password.");
            } else if ((matcher1 = SignupMenuCommands.WeakPassword.getMatcher(newPassword)) == null) {
                StringBuilder output = validatePassword(newPassword);
                return new Result(false, output.toString());
            } else {
                App.getApp().getPendingUser().setPassword(newPassword);
                return new Result(true, "your new password has been successfully changed. please enter your new password:");
            }
        }
    }

    public Result interNewPassword(Matcher matcher) {
        String newPassword = matcher.group("newPassword");

        if (!newPassword.equals(App.getApp().getPendingUser().getPassword())) {
            App.getApp().setPendingUser(null);
            return new Result(false, "the new password entered is incorrect. to log in, you must start over.");
        } else {
            App.getApp().getUsers().set(getIndexInUsers(App.getApp().getPendingUser().getUserName()),
                    App.getApp().getPendingUser());
            App.getApp().setLoggedInUser(getUserByUsername(App.getApp().getPendingUser().getUserName()));
            App.getApp().setPendingUser(null);
            App.getApp().setCurrentMenu(Menu.MAIN_MENU);
            return new Result(true, "login was successful. you are now in the main menu.");
        }
    }

    public Result showCurrentMenu() {
        return new Result(true, "you are now in the login menu.");
    }

    public Result goToSignupMenu() {
        App.getApp().setPendingUser(null);
        App.getApp().setLoggedInUser(null);
        App.getApp().setRandomPassword(null);
        App.getApp().setCurrentMenu(Menu.SIGNUP_MENU);
        return new Result(true, "You are now in the signup menu.");
    }

    public void exit() {
        App.getApp().setCurrentMenu(Menu.ExitMenu);
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

    private boolean isPasswordCorrect(String username, String password) {
        for (User user : App.getApp().getUsers()) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
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

    private int getIndexInUsers(String username) {
        for (int i = 0; i < App.getApp().getUsers().size(); i++) {
            if (App.getApp().getUsers().get(i).getUserName().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    private String generateRandomPassword() {
        int minLength = 8;
        int maxLength = 20;
        SecureRandom random = new SecureRandom();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "?><,\"';:/\\|][}{+=)(*&^%$#!";

        List<Character> passwordChars = new ArrayList<>();

        passwordChars.add(upper.charAt(random.nextInt(upper.length())));
        passwordChars.add(lower.charAt(random.nextInt(lower.length())));
        passwordChars.add(digits.charAt(random.nextInt(digits.length())));
        passwordChars.add(special.charAt(random.nextInt(special.length())));

        String allChars = upper + lower + digits + special;
        for (int i = 4; i < length; i++) {
            passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
        }

        Collections.shuffle(passwordChars);

        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }

    public static StringBuilder validatePassword(String password) {
        StringBuilder errors = new StringBuilder();

        if (password.length() < 8) {
            errors.append("Password must be at least 8 characters long. \n");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.append("Password must include at least one lowercase letter.\n");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.append("Password must include at least one uppercase letter. \n");
        }
        if (!password.matches(".*[0-9].*")) {
            errors.append("Password must contain at least one digit. \n");
        }
        if (!password.matches(".*[?<>,\"';:/\\\\|\\]\\[\\}\\{\\+=\\)\\(\\*&\\^%\\$#!].*")) {
            errors.append("Password must contain at least one special character (e.g. !, @, #, $...).");
        }
        return errors;
    }

}
