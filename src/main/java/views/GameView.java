package views;

import controllers.AppControllers;
import controllers.GameController;
import models.Enums.commands.GameCommands;
import models.Input;
import models.Result;

import java.util.regex.Matcher;

public class GameView implements View {
    @Override
    public void checkCommand(String command) {
        GameController controller = AppControllers.gameController;

        Matcher matcher;
        if ((GameCommands.NEXT_TURN.getMatcher(command)) != null) {
            System.out.println(controller.goNextTurn());
        } else if ((GameCommands.TIME.getMatcher(command)) != null) {
            System.out.println(controller.showTime());
        } else if ((GameCommands.DATE.getMatcher(command)) != null) {
            System.out.println(controller.showDate());
        } else if ((GameCommands.DATE_AND_TIME.getMatcher(command)) != null) {
            System.out.println(controller.showDateTime());
        } else if ((GameCommands.DAY_OF_WEEK.getMatcher(command)) != null) {
            System.out.println(controller.showDayOfWeek());
        } else if ((matcher = GameCommands.CHEAT_ADVANCE_TIME.getMatcher(command)) != null) {
            System.out.println(controller.cheatAdvanceTime(matcher));
        } else if ((matcher = GameCommands.CHEAT_ADVANCE_DATE.getMatcher(command)) != null) {
            System.out.println(controller.cheatAdvanceDate(matcher));
        } else if ((GameCommands.SHOW_SEASON.getMatcher(command)) != null) {
            System.out.println(controller.showSeason());
        } else if ((matcher = GameCommands.PRINT_MAP.getMatcher(command)) != null) {
            System.out.println(controller.printMap(matcher));
        } else if (GameCommands.HELP_READING_MAP.getMatcher(command) != null) {
            System.out.println(controller.helpReadingMap());
        }
        else if ((matcher = GameCommands.WALK.getMatcher(command)) != null) {
            int x = Integer.parseInt(matcher.group("x"));
            int y = Integer.parseInt(matcher.group("y"));

            Result result = controller.walkToCheck(x,y);
            System.out.println(result);
            if (result.success()) {
                System.out.println("do you want to go? (y / n)");
                char character = Input.getNextChar();
                if (character == 'y' || character == 'Y') {
                    System.out.println(controller.walkTo());
                }
            }
        }
        else if ((GameCommands.SHOW_ENERGY.getMatcher(command)) != null) {
            System.out.println(controller.showEnergy());
        } else if ((matcher = GameCommands.CHEAT_SET_ENERGY.getMatcher(command)) != null) {
            System.out.println(controller.cheatSetEnergy(matcher));
        } else if ((GameCommands.CHEAT_ENERGY_UNLIMITED.getMatcher(command)) != null) {
            System.out.println(controller.cheatEnergyUnlimited());
        } else if ((GameCommands.WEATHER.getMatcher(command)) != null) {
            System.out.println(controller.showWeather());
        } else if ((GameCommands.WEATHER_FORECAST.getMatcher(command)) != null) {
            System.out.println(controller.forecastWeather());
        } else if ((matcher = GameCommands.CHEAT_WEATHER_SET.getMatcher(command)) != null) {
            System.out.println(controller.cheatWeather(matcher));
        } else if ((matcher = GameCommands.CHEAT_THOR.getMatcher(command)) != null) {
            System.out.println(controller.cheatThor(matcher));
        }
        else if ((matcher = GameCommands.BUILD_GREENHOUSE.getMatcher(command)) != null) {
            Result res;
            System.out.println((res = controller.buildGreenHouseRequest()));
            if (res.success()) {
                System.out.println(controller.buildGreenHouse());
            }
        }

        else {
            System.out.println("invalid command.");
        }

        // Time checking
        Result result = controller.checkTime();
        if (result.success()) {
            System.out.println(result);
            System.out.println(controller.goNextDay());
        }
    }
}
