package views;

import controllers.SignupMenuController;
import models.App;
import models.Enums.commands.SignupMenuCommand;
import models.Result;

import java.util.regex.Matcher;

public class SignupMenuView implements View {
    private final SignupMenuController controller = new SignupMenuController();

    @Override
    public void checkCommand(String command) {
        Matcher matcher;
        Result result;

        if ((matcher = SignupMenuCommand.Register.getMatcher(command)) != null && !App.getApp().isRegisterSuccessful()) {
            result = controller.register(matcher);
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommand.SetRandomPassword.getMatcher(command)) != null && !App.getApp().isRegisterSuccessful()) {
            result = controller.setRandomPassword();
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommand.GetAnotherRandomPassword.getMatcher(command)) != null && !App.getApp().isRegisterSuccessful()) {
            result = controller.getAnotherRandomPassword();
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommand.CancelGetRandomPassword.getMatcher(command)) != null && !App.getApp().isRegisterSuccessful()) {
            result = controller.cancelGetRandomPassword();
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommand.SecurityQuestion.getMatcher(command)) != null && App.getApp().isRegisterSuccessful()) {
            result = controller.securityQuestion(matcher);
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommand.ShowCurrentManu.getMatcher(command)) != null && !App.getApp().isRegisterSuccessful()) {
            result = controller.showCurrentManu();
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommand.GoToLoginMenu.getMatcher(command)) != null && !App.getApp().isRegisterSuccessful()) {
            result = controller.goToLoginMenu();
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommand.ExitMenu.getMatcher(command)) != null) {
            controller.exit();
        } else {
            System.out.println("invalid command!");
        }
    }
}
