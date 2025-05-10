package controllers;

import models.App;
import models.Enums.Menu;
import models.Enums.commands.SignupMenuCommands;
import models.Result;
import models.User;
import models.services.HashSHA256;

import java.util.regex.Matcher;

public class ProfileMenuController {

    public Result changeUsername(Matcher matcher) {
        String username = matcher.group("username");
        Matcher matcher1;

        if (App.getApp().getLoggedInUser().getUserName().equals(username)) {
            return new Result(false, "the new username is the same as the previous one.");
        } else if (isUsernameExist(username)) {
            return new Result(false, "this username has already been registered. Please choose another username.");
        } else if ((matcher1 = SignupMenuCommands.UserName.getMatcher(username)) == null) {
            return new Result(false, "the username format is invalid.");
        } else {
            App.getApp().getLoggedInUser().setUserName(username);
            App.getApp().getUsers().get(getIndexInUsers(App.getApp().getLoggedInUser().getUserName())).setUserName(username);
            return new Result(true, "username changed successfully.");
        }
    }

    public Result changeNickname(Matcher matcher) {
        String nickname = matcher.group("nickname");
        Matcher matcher1;

        if (App.getApp().getLoggedInUser().getNickname().equals(nickname)) {
            return new Result(false, "the new nickname is the same as the previous one.");
        } else if ((matcher1 = SignupMenuCommands.UserName.getMatcher(nickname)) == null) {
            return new Result(false, "the nickname format is invalid.");
        } else {
            App.getApp().getLoggedInUser().setNickname(nickname);
            App.getApp().getUsers().get(getIndexInUsers(App.getApp().getLoggedInUser().getUserName())).setNickname(nickname);
            return new Result(true, "nickname changed successfully.");
        }
    }

    public Result changeEmail(Matcher matcher) {
        String email = matcher.group("email");
        Matcher matcher1;

        if (App.getApp().getLoggedInUser().getEmail().equals(email)) {
            return new Result(false, "the new email is the same as the previous one.");
        } else if ((matcher1 = SignupMenuCommands.Email.getMatcher(email)) == null) {
            return new Result(false, "the email format is invalid.");
        } else {
            App.getApp().getLoggedInUser().setEmail(email);
            App.getApp().getUsers().get(getIndexInUsers(App.getApp().getLoggedInUser().getUserName())).setEmail(email);
            return new Result(true, "email changed successfully.");
        }
    }

    public Result changePassword(Matcher matcher) {
        String password = matcher.group("password");
        String oldPassword = matcher.group("oldPassword");
        Matcher matcher1;

        if (!HashSHA256.checkPassword(oldPassword, App.getApp().getLoggedInUser().getPassword())) {
            return new Result(false, "The old password you entered is incorrect.");
        } else if (HashSHA256.checkPassword(password, App.getApp().getLoggedInUser().getPassword())) {
            return new Result(false, "the new password is the same as the previous one.");
        } else if ((matcher1 = SignupMenuCommands.Password.getMatcher(password)) == null) {
            return new Result(false, "the password format is invalid.");
        } else if ((matcher1 = SignupMenuCommands.WeakPassword.getMatcher(password)) == null) {
            StringBuilder output = validatePassword(password);
            return new Result(false, output.toString());
        } else {
            App.getApp().getLoggedInUser().setPassword(HashSHA256.hashPassword(password));
            App.getApp().getUsers().get(getIndexInUsers(App.getApp().getLoggedInUser().getUserName())).setPassword(password);
            return new Result(true, "password changed successfully.");
        }
    }

    public Result showUserInfo() {
        StringBuilder output = new StringBuilder();
        output.append("username : ").append(App.getApp().getLoggedInUser().getUserName()).append("\n");
        output.append("nickname : ").append(App.getApp().getLoggedInUser().getNickname()).append("\n");
        output.append("number of games played : ").append(App.getApp().getLoggedInUser().getNumberOfGamesPlayed()).append("\n");
        output.append("highest score in on game : ").append(App.getApp().getLoggedInUser().getHighestMoneyEarnedInASingleGame());
        return new Result(true, output.toString());
    }

    public Result showCurrentMenu() {
        return new Result(true, "you are now in profile menu.");
    }

    public Result gotoMainMenu() {
        App.getApp().setCurrentMenu(Menu.MAIN_MENU);
        return new Result(true, "you are now in main menu.");
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
        for (int i = 0; i < App.getApp().getUsers().size(); i++) {
            if (App.getApp().getUsers().get(i).getUserName().equals(username)) {
                return i;
            }
        }
        return -1;
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
