package controllers;

import models.App;
import models.Enums.commands.LoginMenuCommands;
import models.Enums.commands.ProfileMenuCommands;
import models.User;
import models.services.HashSHA256;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.regex.Matcher;

import static controllers.ProfileMenuController.validatePassword;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileMenuTest {
    private ProfileMenuController controller = new ProfileMenuController();
    Matcher matcher;
    String input;
    String result;

    @BeforeEach
    void setup() {
        controller = new ProfileMenuController();
    }

    @Mock
    App appMock = App.getApp();

    // change username :

    @Test
    void correctChangeUsername() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change username -u Mostafa";
        matcher = ProfileMenuCommands.ChangeUsername.getMatcher(input);
        result = "username changed successfully.";
        assertEquals(result, controller.changeUsername(matcher).message());
    }

    @Test
    // Duplicate username :
    void invalidChangeUsername() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.getUsers().add(new User("Mostafa", "paSS12#$", "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change username -u Mostafa";
        matcher = ProfileMenuCommands.ChangeUsername.getMatcher(input);
        result = "this username has already been registered. Please choose another username.";
        assertEquals(result, controller.changeUsername(matcher).message());
    }

    @Test
    // Invalid username (illegal characters or more than 8 characters) :
    void invalidChangeUsername2() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change username -u invalid@name";
        matcher = ProfileMenuCommands.ChangeUsername.getMatcher(input);
        result = "the username format is invalid.";
        assertEquals(result, controller.changeUsername(matcher).message());
    }

    @Test
    void invalidChangeUsername3() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change username -u verylongname";
        matcher = ProfileMenuCommands.ChangeUsername.getMatcher(input);
        result = "the username format is invalid.";
        assertEquals(result, controller.changeUsername(matcher).message());
    }

    @Test
    // New username = current:
    void invalidChangeUsername4() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change username -u mostafa";
        matcher = ProfileMenuCommands.ChangeUsername.getMatcher(input);
        result = "the new username is the same as the previous one.";
        assertEquals(result, controller.changeUsername(matcher).message());
    }

    // testing the change nickname :

    @Test
    void correctChangeNickname() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change nickname -u Mosi";
        matcher = ProfileMenuCommands.ChangeNickname.getMatcher(input);
        result = "nickname changed successfully.";
        assertEquals(result, controller.changeNickname(matcher).message());
    }

    @Test
    // New nickname = current :
    void invalidChangeNickname() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change nickname -u mosi";
        matcher = ProfileMenuCommands.ChangeNickname.getMatcher(input);
        result = "the new nickname is the same as the previous one.";
        assertEquals(result, controller.changeNickname(matcher).message());
    }

    // testing change email :

    @Test
    void correctChangeEmail() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change email -e newmail@gmail.com";
        matcher = ProfileMenuCommands.ChangeEmail.getMatcher(input);
        result = "email changed successfully.";
        assertEquals(result, controller.changeEmail(matcher).message());
    }

    @Test
    // New email = Previous :
    void invalidChangeEmail() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change email -e mostafa@gamil.com";
        matcher = ProfileMenuCommands.ChangeEmail.getMatcher(input);
        result = "the new email is the same as the previous one.";
        assertEquals(result, controller.changeEmail(matcher).message());
    }

    @Test
    // Invalid email :
    void invalidChangeEmail2() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change email -e change email -e user@@mail.com";
        matcher = ProfileMenuCommands.ChangeEmail.getMatcher(input);
        result = "the email format is invalid.";
        assertEquals(result, controller.changeEmail(matcher).message());
    }

    // testing change password :

    @Test
    void correctChangePassword() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change password -p NewPass#123 -o paSS12#$";
        matcher = ProfileMenuCommands.ChangePassword.getMatcher(input);
        result = "password changed successfully.";
        assertEquals(result, controller.changePassword(matcher).message());
    }

    @Test
    // New password = Previous password :
    void invalidChangePassword() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change password -p paSS12#$ -o paSS12#$";
        matcher = ProfileMenuCommands.ChangePassword.getMatcher(input);
        result = "the new password is the same as the previous one.";
        assertEquals(result, controller.changePassword(matcher).message());
    }

    @Test
    // Previous password incorrect :
    void invalidChangePassword2() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change password -p NewPass#123 -o paSSS12#$";
        matcher = ProfileMenuCommands.ChangePassword.getMatcher(input);
        result = "The old password you entered is incorrect.";
        assertEquals(result, controller.changePassword(matcher).message());
    }

    @Test
    // New weak password:
    void invalidChangePassword3() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        input = "change password -p 1234 -o paSS12#$";
        matcher = ProfileMenuCommands.ChangePassword.getMatcher(input);
        result = validatePassword("1234").toString();
        assertEquals(result, controller.changePassword(matcher).message());
    }
}