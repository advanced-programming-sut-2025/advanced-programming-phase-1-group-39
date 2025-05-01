package views;

import controllers.AppControllers;
import controllers.GameController;
import models.Enums.commands.GameCommands;

import java.util.regex.Matcher;

public class GameView implements View {
    @Override
    public void checkCommand(String command) {
        GameController controller = AppControllers.gameController;

        Matcher matcher;

        if ((matcher = GameCommands.PRINT_MAP.getMatcher(command)) != null) {
            System.out.println(controller.printMap(matcher));
        } else if (GameCommands.HELP_READING_MAP.getMatcher(command) != null) {
            System.out.println(controller.helpReadingMap());
        }
    }
}
