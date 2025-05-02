package controllers;

import models.App;
import models.Enums.Menu;
import models.Enums.commands.SignupMenuCommand;
import models.Result;
import models.User;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

public class SignupMenuController {

    public Result register(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String random = matcher.group("random");
        String passwordConfirm = matcher.group("passwordConfirm");
        String nickName = matcher.group("nickName");
        String email = matcher.group("email");
        String gender = matcher.group("gender");
        Matcher matcher1;

        if (!isUsernameUnique(username)) {
            String newUsername = getUniqueName(username);
            return new Result(false, "The username you want is already in use. You can use this username: " +
                    "((" + newUsername + ")). If you'd like to use it, or choose a different username.");
        } else if ((matcher1 = SignupMenuCommand.UserName.getMatcher(username)) == null) {
            return new Result(false, "The username format is incorrect. Please make sure it contains only letters, numbers, - ,and no spaces.");
        } else if ((matcher1 = SignupMenuCommand.Email.getMatcher(email)) == null) {
            return new Result(false, "The email address you entered is invalid. Please enter a valid email address.");
        } else if (password != null && (matcher1 = SignupMenuCommand.Password.getMatcher(password)) == null) {
            return new Result(false, "Invalid password format. You can use letters, numbers, and special characters in your password.");
        } else if (password != null && (matcher1 = SignupMenuCommand.WeakPassword.getMatcher(password)) == null) {
            StringBuilder output = validatePassword(password);
            return new Result(false, output.toString());
        } else if (password != null && passwordConfirm != null && !password.equals(passwordConfirm)) {
            return new Result(false, "Password and confirmation do not match.");
        } else if ((matcher1 = SignupMenuCommand.Gender.getMatcher(gender)) == null) {
            return new Result(false, "Please enter a valid gender. Accepted values are 'male' or 'female'.");
        } else if (random != null && !random.equals("random")) {
            return new Result(false, "To request a random password, please type the word 'random.");
        } else if (random != null) {
            String randomPassword = generateRandomPassword();
            App.getApp().setRandomPassword(randomPassword);
            if (gender.equals("male")) {
                App.getApp().setPendingUser(new User(username, randomPassword, nickName, email, true));
            } else {
                App.getApp().setPendingUser(new User(username, randomPassword, nickName, email, false));
            }
            return new Result(true , "A secure password has been generated for you: " + randomPassword +
                    "\n"+"If you wish to use this password, please type 'Yes'.\n + " +
                    "If you would like to generate another random password, please type 'random'.\n" +
                    "Otherwise, type 'No' to cancel.");
        } else {
            if (gender.equals("male")) {
                App.getApp().addUser(new User(username, password, nickName, email, true));
            }
            else {
                App.getApp().addUser(new User(username, password, nickName, email, false));
            }
            App.getApp().setCurrentMenu(Menu.LOGIN_MENU);
            return new Result(true, "\"Registration successful! You are now redirected to the login menu...");
        }
    }



//    Register("register\s+-u\s+(?<username>\S+)\s+-p\s+(?:(?<password>\S+)\s+(?<passwordConfirm>\S+)|(?<random>random))\s+-n\s+(?<nickName>\S+)\s+-e\s+(?<email>\S+)\s+-g\s+(?<gender>\S+)),
//    UserName("[a-zA-Z0-9-]+"),
//    Password("[a-zA-Z0-9?<>,\"';:/|}\\{+)=*&^%$#!]+"),
//    WeakPassword("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-[\\]{}|:;\"'<>,.?/~`])[a-zA-Z0-9!@#$%^&*()_+=\\-[\\]{}|:;\"'<>,.?/~`]{8,}"),
//
//    Email("(?=.{1,64}@)([a-zA-Z0-9](?!.*\\.\\.)[a-zA-Z0-9._-]{0,62}[a-zA-Z0-9])@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9](?:\\.[a-zA-Z]{2,})+)"),
//
//    ShowCurrentManu("show\\s+current\\s+manu"),
//
//    GoToLoginMenu("manu\\s+enter\\s+login"),
//
//    ExitMenu("manu\\s+exit"),


    // Auxiliary functions :

    private boolean isUsernameUnique(String username) {
        for (User user : App.getApp().getUsers()) {
            if (user.getUserName().equals(username)) {
                return false;
            }
        }
        return true;
    }

    // Function to get a unique name
    private String getUniqueName(String baseName) {
        int counter = 1;
        String uniqueName = baseName;
        Random random = new Random();

        while (!isUsernameUnique(uniqueName)) {
            if (random.nextBoolean()) {
                uniqueName = baseName + counter;
            } else {
                uniqueName = baseName + "-" + counter;
            }
            counter++;
        }
        return uniqueName;
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


}
