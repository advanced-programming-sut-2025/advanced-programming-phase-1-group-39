package views;

import controllers.AppControllers;
import controllers.GameController;
import models.Enums.commands.GameCommands;

import java.util.regex.Matcher;

public class GameMenuView implements AppMenu {

    @Override
    public boolean checkCommand(String command) {
        GameController controller = AppControllers.gameController;

        Matcher matcher;

        if ((matcher = GameCommands.PRINT_MAP.getMatcher(command)) != null) {
            System.out.println(controller.printMap(matcher));
        } else if (GameCommands.PRINT_MAP.getMatcher(command) != null) {
            System.out.println();
        }

        return true;
    }
}
