package controllers;

import models.App;
import models.Enums.Menu;
import models.Enums.SecurityQuestionCommands;
import models.Enums.commands.SignupMenuCommands;
import models.Result;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignupMenuTest {
    private SignupMenuController controller = new SignupMenuController();
    Matcher matcher;
    String input;
    String result;

    @BeforeEach
    void setup() {
        controller = new SignupMenuController();
    }

    @Mock
    App appMock = App.getApp();

    // testing the username :

    @Test
    void correctUsername() {
        input = "register    -u   mostafa   -p paSS12#$   paSS12#$  -n mosi  -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "Registration was successful. now, please choose one of the security questions below and answer it:\n" +
                SecurityQuestionCommands.DREAM_JOB.getQuestion() + "\n" + SecurityQuestionCommands.FAVORITE_TEACHER.getQuestion() + "\n" +
                SecurityQuestionCommands.HISTORICAL_FIGURE.getQuestion() + "\n" + SecurityQuestionCommands.FIRST_SCHOOL.getQuestion() + "\n" +
                SecurityQuestionCommands.FIRST_PHONE_MODEL.getQuestion();
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    void invalidUsernameWithSpecialCharacters() {
        input = "register -u mostafa! -p paSS12#$ paSS12#$ -n mosi -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "The username format is incorrect. Please make sure it contains only letters, numbers, '-', no spaces, and does not exceed 8 characters.";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    void invalidUsernameWithWhiteSpace() {
        input = "register -u mostafa user -p paSS12#$ paSS12#$ -n mosi -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "The username format is incorrect. Please make sure it contains only letters, numbers, '-', no spaces, and does not exceed 8 characters.";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    void invalidUsernameWithSpace() {
        input = "register -u     -p paSS12#$ paSS12#$ -n mosi -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "The username format is incorrect. Please make sure it contains only letters, numbers, '-', no spaces, and does not exceed 8 characters.";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    void invalidUsernameWithMore8Characters() {
        input = "register -u   mosimosimosimosi  -p paSS12#$ paSS12#$ -n mosi -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "The username format is incorrect. Please make sure it contains only letters, numbers, '-', no spaces, and does not exceed 8 characters.";
        assertEquals(result, controller.register(matcher).message());
    }

    // testing the Email :

    @Test
    void correctEmail() {
        input = "register    -u   mostafa   -p paSS12#$   paSS12#$  -n mosi  -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "Registration was successful. now, please choose one of the security questions below and answer it:\n" +
                SecurityQuestionCommands.DREAM_JOB.getQuestion() + "\n" + SecurityQuestionCommands.FAVORITE_TEACHER.getQuestion() + "\n" +
                SecurityQuestionCommands.HISTORICAL_FIGURE.getQuestion() + "\n" + SecurityQuestionCommands.FIRST_SCHOOL.getQuestion() + "\n" +
                SecurityQuestionCommands.FIRST_PHONE_MODEL.getQuestion();
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    // invalid Email without @
    void invalidEmail() {
        input = "register -u mostafa -p paSS12#$ paSS12#$ -n mosi -e mostafagmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "The email address you entered is invalid. Please enter a valid email address.";
        assertEquals(result, controller.register(matcher).message());
    }
    // invalid Email with two @
    @Test
    void invalidEmail2() {
        input = "register -u mostafa -p paSS12#$ paSS12#$ -n mosi -e mostafa@@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "The email address you entered is invalid. Please enter a valid email address.";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    // invalid Email with consecutive dots
    void invalidEmail3() {
        input = "register -u mostafa -p paSS12#$ paSS12#$ -n mosi -e mosta..fa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "The email address you entered is invalid. Please enter a valid email address.";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    // invalid Email with a dot in the domain section
    void invalidEmail4() {
        input = "register -u mostafa -p paSS12#$ paSS12#$ -n mosi -e mostafa@.gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "The email address you entered is invalid. Please enter a valid email address.";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    // invalid Email with a short domain extension (one letter)
    void invalidEmail5() {
        input = "register -u mostafa -p paSS12#$ paSS12#$ -n mosi -e mostafa@gmail.c -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "The email address you entered is invalid. Please enter a valid email address.";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    // invalid Email with an Email containing illegal symbols
    void invalidEmail6() {
        input = "register -u mostafa -p paSS12#$ paSS12#$ -n mosi -e mostafa@gmai!l.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "The email address you entered is invalid. Please enter a valid email address.";
        assertEquals(result, controller.register(matcher).message());
    }

    // testing the password :

    @Test
    // Weak password (no numbers)
    void invalidPassword() {
        input = "register -u mostafa -p paSS#$%^ paSS#$%^ -n mosi -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "Password must contain at least one digit.";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    void invalidPasswordConfirm() {
        input = "register -u mostafa -p pa@ pa@ -n mosi -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "Invalid password format. You can use letters, numbers, and special characters in your password.";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    // weak password (no uppercase letters)
    void invalidPassword2() {
        input = "register -u mostafa -p pass12#$ pass12#$ -n mosi -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "Password must include at least one uppercase letter.";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    // Weak password (lacking special symbols)
    void invalidPassword3() {
        input = "register -u mostafa -p passWord12 passWord12 -n mosi -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "Password must contain at least one special character (e.g. !, @, #, $...).";
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    // Password and repeat failed (mismatch)
    void invalidPassword4() {
        input = "register -u mostafa -p paSS12#$ paSS12#% -n mosi -e mostafa@gmail.com -g male";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "Password and confirmation do not match.";
        assertEquals(result, controller.register(matcher).message());
    }

    // testing pick question :

    @Test
    void correctPickQuestion() {
        appMock.setPendingUser(new User("mostafa", "paSS12#$", "mosi", "mostafa@gamul.com", true));
        input = "pick question -q 1 -a salam -c salam";
        matcher = SignupMenuCommands.SecurityQuestion.getMatcher(input);
        result = "your registration is complete! you are now in the login menu.";
        assertEquals(result, controller.securityQuestion(matcher).message());
    }

    @Test
    // Selecting a security question with an invalid answer (answers do not match)
    void invalidPickQuestion() {
        appMock.setPendingUser(new User("mostafa", "paSS12#$", "mosi", "mostafa@gamul.com", true));
        input = "pick question -q 2 -a myAnswer -c myAnswerr";
        matcher = SignupMenuCommands.SecurityQuestion.getMatcher(input);
        result = "the answer does not match its repetition.";
        assertEquals(result, controller.securityQuestion(matcher).message());
    }

    @Test
    // Selecting a security question with an invalid question number
    void invalidPickQuestion2() {
        appMock.setPendingUser(new User("mostafa", "paSS12#$", "mosi", "mostafa@gamul.com", true));
        input = "pick question -q 99 -a myAnswer  -c  myAnswer";
        matcher = SignupMenuCommands.SecurityQuestion.getMatcher(input);
        result = "the question number must be between one and five.";
        assertEquals(result, controller.securityQuestion(matcher).message());
    }

    @Test
    // testing the gender :
    void correctGender() {
        input = "register    -u   mostafa   -p paSS12#$   paSS12#$  -n mosi  -e mostafa@gmail.com -g female";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "Registration was successful. now, please choose one of the security questions below and answer it:\n" +
                SecurityQuestionCommands.DREAM_JOB.getQuestion() + "\n" + SecurityQuestionCommands.FAVORITE_TEACHER.getQuestion() + "\n" +
                SecurityQuestionCommands.HISTORICAL_FIGURE.getQuestion() + "\n" + SecurityQuestionCommands.FIRST_SCHOOL.getQuestion() + "\n" +
                SecurityQuestionCommands.FIRST_PHONE_MODEL.getQuestion();
        assertEquals(result, controller.register(matcher).message());
    }

    @Test
    // invalid gender
    void invalidGender() {
        input = "register    -u   mostafa   -p paSS12#$   paSS12#$  -n mosi  -e mostafa@gmail.com -g unknown";
        matcher = SignupMenuCommands.Register.getMatcher(input);
        result = "Please enter a valid gender. Accepted values are 'male' or 'female'.";
        assertEquals(result, controller.register(matcher).message());
    }

    // show current menu
    @Test
    void showCurrentMenu() {
        result = "You are now in signup menu.";
        assertEquals(result, controller.showCurrentManu().message());
    }
}

// register    -u   mostafa   -p paSS12#$   paSS12#$  -n mosi  -e mostafa@gmail.com -g male
//App app = App.getApp();
//app.addUser(new User("mostafa", "paSS12#$", "mosi", "mostafa@gamil.com", true));