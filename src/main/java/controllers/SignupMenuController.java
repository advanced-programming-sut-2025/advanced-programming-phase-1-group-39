package controllers;

import models.App;
import models.Enums.Menu;
import models.Enums.SecurityQuestionCommands;
import models.Enums.commands.SignupMenuCommands;
import models.Result;
import models.SecurityQuestion;
import models.User;
import models.services.HashSHA256;
import models.services.SaveAppManager;
import models.services.UsersDataManager;

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
        } else if ((matcher1 = SignupMenuCommands.UserName.getMatcher(username)) == null) {
            return new Result(false, "The username format is incorrect. Please make sure it contains only letters, numbers, - ,and no spaces.");
        } else if ((matcher1 = SignupMenuCommands.Email.getMatcher(email)) == null) {
            return new Result(false, "The email address you entered is invalid. Please enter a valid email address.");
        } else if (password != null && (matcher1 = SignupMenuCommands.Password.getMatcher(password)) == null) {
            return new Result(false, "Invalid password format. You can use letters, numbers, and special characters in your password.");
        } else if (password != null && (matcher1 = SignupMenuCommands.WeakPassword.getMatcher(password)) == null) {
            StringBuilder output = validatePassword(password);
            return new Result(false, output.toString());
        } else if (password != null && passwordConfirm != null && !password.equals(passwordConfirm)) {
            return new Result(false, "Password and confirmation do not match.");
        } else if ((matcher1 = SignupMenuCommands.Gender.getMatcher(gender)) == null) {
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
                    "\n"+"If you wish to use this password, please type 'yes'.\n" +
                    "If you would like to generate another random password, please type 'random'.\n" +
                    "Otherwise, type 'no' to cancel.");
        } else {
            if (gender.equals("male")) {
                App.getApp().setPendingUser(new User(username, password, nickName, email, true));
            }
            else {
                App.getApp().setPendingUser(new User(username, password, nickName, email, false));
            }
            App.getApp().setRegisterSuccessful(true);
            return new Result(true, "Registration was successful. now, please choose one of the security questions below and answer it:\n" +
                    SecurityQuestionCommands.DREAM_JOB.getQuestion() + "\n" + SecurityQuestionCommands.FAVORITE_TEACHER.getQuestion() + "\n" +
                    SecurityQuestionCommands.HISTORICAL_FIGURE .getQuestion()+ "\n" + SecurityQuestionCommands.FIRST_SCHOOL.getQuestion() + "\n" +
                    SecurityQuestionCommands.FIRST_PHONE_MODEL.getQuestion());
        }
    }

    public Result setRandomPassword () {
        if (App.getApp().getRandomPassword() != null) {
            App.getApp().setRandomPassword(null);
            App.getApp().setRegisterSuccessful(true);
            return new Result(true, "Registration was successful. now, please choose one of the security questions below and answer it:\n" +
                    SecurityQuestionCommands.DREAM_JOB.getQuestion() + "\n" + SecurityQuestionCommands.FAVORITE_TEACHER.getQuestion() + "\n" +
                    SecurityQuestionCommands.HISTORICAL_FIGURE .getQuestion()+ "\n" + SecurityQuestionCommands.FIRST_SCHOOL.getQuestion() + "\n" +
                    SecurityQuestionCommands.FIRST_PHONE_MODEL.getQuestion());
        } else {
            return new Result(false, "invalid command!");
        }
    }

   public Result getAnotherRandomPassword () {
        if (App.getApp().getRandomPassword() != null) {
            String randomPassword = generateRandomPassword();
            App.getApp().setRandomPassword(randomPassword);
            App.getApp().getPendingUser().setPassword(randomPassword);
            return new Result(true , "A secure password has been generated for you: " + randomPassword +
                    "\n"+"If you wish to use this password, please type 'yes'.\n" +
                    "If you would like to generate another random password, please type 'random'.\n" +
                    "Otherwise, type 'no' to cancel.");
        } else {
            return new Result(false, "invalid command!");
        }
    }

    public Result cancelGetRandomPassword () {
        if (App.getApp().getRandomPassword() != null) {
            App.getApp().setRandomPassword(null);
            App.getApp().setPendingUser(null);
            return new Result(true, "You are now in signup menu.");
        } else {
            return new Result(false, "invalid command!");
        }
    }

    public Result securityQuestion(Matcher matcher) {
        String questionNumber = matcher.group("questionNumber");
        String answer = matcher.group("answer");
        String answerConfirm = matcher.group("answerConfirm");

        if (!isNumeric(questionNumber)) {
            return new Result(false, "the question number must be a numeric value between one and five.");
        } else if (Integer.parseInt(questionNumber) < 1 || Integer.parseInt(questionNumber) > 5) {
            return new Result(false, "the question number must be between one and five.");
        } else if (!answer.equals(answerConfirm)) {
            return new Result(false, "the answer does not match its repetition.");
        } else {
            SecurityQuestion question = setSecurityQuestion(questionNumber, answer);
            App.getApp().getPendingUser().setSecurityQuestion(question);
            String hashPass = HashSHA256.hashPassword(App.getApp().getPendingUser().getPassword());
            App.getApp().getPendingUser().setPassword(hashPass);
            App.getApp().addUser(App.getApp().getPendingUser());
            App.getApp().setPendingUser(null);
            App.getApp().setRegisterSuccessful(false);
            App.getApp().setCurrentMenu(Menu.LOGIN_MENU);
            return new Result(true, "your registration is complete! you are now in the login menu.");
        }
    }

    public Result showCurrentManu() {
        return new Result(true, "You are now in signup menu.");
    }

    public Result goToLoginMenu() {
        App.getApp().setCurrentMenu(Menu.LOGIN_MENU);
        return new Result(true, "You are now in login menu.");
    }

    public void exit() {
        App.getApp().setCurrentMenu(Menu.ExitMenu);
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

    // Function to get a unique name
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

    private boolean isNumeric(String questionNumber) {
        if (questionNumber == null || questionNumber.isEmpty()) return false;
        try {
            Integer.parseInt(questionNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private SecurityQuestion setSecurityQuestion(String questionNumber, String answer) {
        if (questionNumber.equals("1")) {
            return new SecurityQuestion(SecurityQuestionCommands.DREAM_JOB.toString(), answer);
        } else if (questionNumber.equals("2")) {
            return new SecurityQuestion(SecurityQuestionCommands.FAVORITE_TEACHER.toString(), answer);
        } else if (questionNumber.equals("3")) {
            return new SecurityQuestion(SecurityQuestionCommands.HISTORICAL_FIGURE.toString(), answer);
        } else if (questionNumber.equals("4")) {
            return new SecurityQuestion(SecurityQuestionCommands.FIRST_SCHOOL.toString(), answer);
        } else {
            return new SecurityQuestion(SecurityQuestionCommands.FIRST_PHONE_MODEL.toString(), answer);
        }
    }


}
