package views;

import controllers.SignupMenuController;
import models.App;
import models.Enums.commands.SignupMenuCommands;
import models.Result;

import java.util.regex.Matcher;

public class SignupMenuView implements View {
    private final SignupMenuController controller = new SignupMenuController();

    @Override
    public void checkCommand(String command) {
        Matcher matcher;
        Result result;

        if ((matcher = SignupMenuCommands.Register.getMatcher(command)) != null && !App.getApp().getIsRegisterSuccessful()) {
            result = controller.register(matcher);
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommands.SetRandomPassword.getMatcher(command)) != null && !App.getApp().getIsRegisterSuccessful()) {
            result = controller.setRandomPassword();
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommands.GetAnotherRandomPassword.getMatcher(command)) != null && !App.getApp().getIsRegisterSuccessful()) {
            result = controller.getAnotherRandomPassword();
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommands.CancelGetRandomPassword.getMatcher(command)) != null && !App.getApp().getIsRegisterSuccessful()) {
            result = controller.cancelGetRandomPassword();
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommands.SecurityQuestion.getMatcher(command)) != null && App.getApp().getIsRegisterSuccessful()) {
            result = controller.securityQuestion(matcher);
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommands.ShowCurrentManu.getMatcher(command)) != null && !App.getApp().getIsRegisterSuccessful()) {
            result = controller.showCurrentManu();
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommands.GoToLoginMenu.getMatcher(command)) != null && !App.getApp().getIsRegisterSuccessful()) {
            result = controller.goToLoginMenu();
            System.out.println(result.message());
        } else if ((matcher = SignupMenuCommands.ExitMenu.getMatcher(command)) != null) {
            controller.exit();
        } else {
            System.out.println("invalid command!");
        }
    }
}
