package views;

import controllers.AppControllers;
import controllers.LoginMenuController;
import models.App;
import models.Enums.commands.LoginMenuCommands;
import models.Result;

import java.util.regex.Matcher;

public class LoginMenuView implements View {
    private final LoginMenuController controller = AppControllers.loginMenuController;

    @Override
    public void checkCommand(String command) {
        Matcher matcher;
        Result result;
        App app = App.getApp();

        if ((matcher = LoginMenuCommands.Login.getMatcher(command)) != null && app.getPendingUser() == null) {
            result = controller.login(matcher);
            System.out.println(result.message());
        } else if ((matcher = LoginMenuCommands.ForgetPassword.getMatcher(command)) != null && app.getPendingUser() == null) {
            result = controller.forgetPassword(matcher);
            System.out.println(result.message());
        } else if ((matcher = LoginMenuCommands.Answer.getMatcher(command)) != null && app.getPendingUser() != null) {
            result = controller.checkAnswer(matcher);
            System.out.println(result.message());
        } else if ((matcher = LoginMenuCommands.NewPassword.getMatcher(command)) != null && app.getPendingUser() != null &&
                app.getPendingUser().getPassword() == null) {
            result = controller.setNewPassword(matcher);
            System.out.println(result.message());
        } else if ((matcher = LoginMenuCommands.NewPassword.getMatcher(command)) != null && app.getPendingUser() != null &&
                app.getPendingUser().getPassword() != null) {
            result = controller.interNewPassword(matcher);
            System.out.println(result.message());
        } else if ((matcher = LoginMenuCommands.ShowCurrentMenu.getMatcher(command)) != null) {
            result = controller.showCurrentMenu();
            System.out.println(result.message());
        } else if ((matcher = LoginMenuCommands.GoTOSignupMenu.getMatcher(command)) != null) {
            result = controller.goToSignupMenu();
            System.out.println(result.message());
        } else if ((matcher = LoginMenuCommands.ExitMenu.getMatcher(command)) != null) {
            controller.exit();
        } else {
            System.out.println("invalid command!");
        }

    }
}
