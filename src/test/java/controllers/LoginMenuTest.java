package controllers;

import models.App;
import models.Enums.commands.LoginMenuCommands;
import models.Enums.commands.SignupMenuCommands;
import models.SecurityQuestion;
import models.User;
import models.services.HashSHA256;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginMenuTest {
    private LoginMenuController controller = new LoginMenuController();
    Matcher matcher;
    String input;
    String result;

    @BeforeEach
    void setup() {
        controller = new LoginMenuController();
    }

    @Mock
    App appMock = App.getApp();

    // testing login :

    @Test
    // Successful login
    void correctLogin() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        input = "login -u mostafa -p paSS12#$";
        matcher = LoginMenuCommands.Login.getMatcher(input);
        result = "login was successful. you are now in the main menu.";
        assertEquals(result, controller.login(matcher).message());
    }

    @Test
    // Successful login with stay logged in :
    void correctLogin2() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        input = "login -u mostafa -p paSS12#$ -stay-logged-in";
        matcher = LoginMenuCommands.Login.getMatcher(input);
        result = "login was successful. you are now in the main menu.";
        assertEquals(result, controller.login(matcher).message());
    }

    @Test
    // Username does not exist :
    void invalidLogin() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        input = "login -u mostafaaa -p paSS12#$ -stay-logged-in";
        matcher = LoginMenuCommands.Login.getMatcher(input);
        result = "Username not found.";
        assertEquals(result, controller.login(matcher).message());
    }

    @Test
    // incorrect password :
    void invalidLogin2() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        input = "login -u mostafa -p paSSS12#$ -stay-logged-in";
        matcher = LoginMenuCommands.Login.getMatcher(input);
        result = "The password is incorrect.";
        assertEquals(result, controller.login(matcher).message());
    }

    // testing forget password :

    @Test
    // Successful forget password
    void correctForgetPassword() {
        appMock.setPendingUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        SecurityQuestion question = new SecurityQuestion("1", "salam");
        appMock.getPendingUser().setSecurityQuestion(question);
        appMock.addUser(appMock.getPendingUser());
        input = "forget password -u mostafa";
        matcher = LoginMenuCommands.ForgetPassword.getMatcher(input);
        result = "please enter the answer to your security question: \n" +
                App.getApp().getUsers().get(0).getSecurityQuestion().getQuestion();
        assertEquals(result, controller.forgetPassword(matcher).message());
    }

    @Test
    // Username does not exist :
    void invalidForgetPassword() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        input = "forget password -u mostafaa";
        matcher = LoginMenuCommands.ForgetPassword.getMatcher(input);
        result = "Username not found.";
        assertEquals(result, controller.forgetPassword(matcher).message());
    }

    @Test
    // Successful check answer :
    void correctCheckPassword() {
        appMock.setPendingUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        SecurityQuestion question = new SecurityQuestion("1", "salam");
        appMock.getPendingUser().setSecurityQuestion(question);
        appMock.addUser(appMock.getPendingUser());
        input = "answer -a salam";
        matcher = LoginMenuCommands.Answer.getMatcher(input);
        result = "enter your new password. if you want to request a random password, type the word 'random'.";
        assertEquals(result, controller.checkAnswer(matcher).message());
    }

    @Test
    // incorrect pick question
    void invalidCheckPassword() {
        appMock.setPendingUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        SecurityQuestion question = new SecurityQuestion("1", "salam");
        appMock.getPendingUser().setSecurityQuestion(question);
        appMock.addUser(appMock.getPendingUser());
        input = "answer -a salamm";
        matcher = LoginMenuCommands.Answer.getMatcher(input);
        result = "the answer you entered is incorrect. please try again to log in.";
        assertEquals(result, controller.checkAnswer(matcher).message());
    }

    // show current menu :
    @Test
    void showCurrentMenu() {
        result = "you are now in the login menu.";
        assertEquals(result, controller.showCurrentMenu().message());
    }
}
