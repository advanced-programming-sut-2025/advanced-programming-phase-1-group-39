package views;

import controllers.MainMenuController;
import models.Enums.commands.MainMenuCommands;
import models.Result;

import java.util.regex.Matcher;

public class MainMenuView implements View {
    private final MainMenuController controller = new MainMenuController();

    @Override
    public void checkCommand(String command) {
        Matcher matcher;
        Result result;

        if ((matcher = MainMenuCommands.Logout.getMatcher(command)) != null) {
            result = controller.logout();
            System.out.println(result.message());
        } else if ((matcher = MainMenuCommands.GoToProfileMenu.getMatcher(command)) != null) {
            result = controller.goProfileMenu();
            System.out.println(result.message());
        } else if ((matcher = MainMenuCommands.GoToGameMenu.getMatcher(command)) != null) {
            result = controller.goGameMenu();
            System.out.println(result.message());
        } else if ((matcher = MainMenuCommands.ShowCurrentMenu.getMatcher(command)) != null) {
            result = controller.showCurrentMenu();
            System.out.println(result.message());
        } else if ((matcher = MainMenuCommands.ExitMenu.getMatcher(command)) != null) {
            controller.exit();
        } else {
            System.out.println("invalid command!");
        }
    }
}
