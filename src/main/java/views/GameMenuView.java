package views;

import models.Enums.commands.GameCommands;

public class GameMenuView implements View {

    @Override
    public boolean checkCommand(String command) {

        if (GameCommands.PRINT_MAP.getMatcher(command) != null) {
            System.out.println();
        }

        return true;
    }
}
