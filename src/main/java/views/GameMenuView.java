package views;

import controllers.AppControllers;
import controllers.GameMenuController;
import models.Enums.commands.GameMenuCommands;
import models.Result;

import java.util.regex.Matcher;

public class GameMenuView implements View {
    private final GameMenuController controller = AppControllers.gameMenuController;

    @Override
    public void checkCommand(String command) {
        Matcher matcher;
        Result result;

        if ((matcher = GameMenuCommands.NewGame.getMatcher(command)) != null) {
            result = controller.startNewGame(matcher);
            System.out.println(result.message());
        } else if ((matcher = GameMenuCommands.ChooseMap.getMatcher(command)) != null) {
            result = controller.chooseMap(matcher);
            System.out.println(result.message());
        } else if ((matcher = GameMenuCommands.LoadGame.getMatcher(command)) != null) {
            result = controller.loadGame();
            System.out.println(result.message());
        } else if ((matcher = GameMenuCommands.GoMainMenu.getMatcher(command)) != null) {
            result = controller.goMainMenu();
            System.out.println(result.message());
        } else if ((matcher = GameMenuCommands.ShowCurrentMenu.getMatcher(command)) != null) {
            result = controller.showCurrentMenu();
            System.out.println(result.message());
        } else {
            System.out.println("invalid command!");
        }
    }
}
