package views;

import controllers.*;
import models.*;
import models.Enums.Menu;
import models.Enums.commands.InteractionsCommand;
import models.Enums.commands.NPCGameCommand;
import models.Enums.commands.GameCommands;

import java.util.regex.Matcher;

public class GameView implements View {
    GameController controller = AppControllers.gameController;
    PlayersInteractionController interactionsController = AppControllers.playersInteractionController;
    private final NPCGameController npcController = new NPCGameController();

    @Override
    public void checkCommand(String command) {

        Matcher matcher;
        Result result;

        if ((GameCommands.SHOW_CURRENT_MENU.getMatcher(command)) != null) {
            System.out.println(controller.showCurrentMenu());
        } else if ((GameCommands.EXIT_GAME.getMatcher(command)) != null) {
            System.out.println(controller.exitGame());
        } else if ((GameCommands.EXIT_APP.getMatcher(command)) != null) {
            controller.exitApp();
        } else if ((GameCommands.NEXT_TURN.getMatcher(command)) != null) {
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

            result = controller.walkToCheck(x,y);
            System.out.println(result);
            if (result.success()) {
                System.out.println("do you want to go? (y / n)");
                String str = Input.getNextLine();
                while (str.isEmpty()) {
                    str = Input.getNextLine();
                }
                char character = str.charAt(0);
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
        else if ((GameCommands.BUILD_GREENHOUSE.getMatcher(command)) != null) {
            Result res;
            System.out.println((res = controller.buildGreenHouseRequest()));
            if (res.success()) {
                System.out.println(controller.buildGreenHouse());
            }
        } else if ((GameCommands.INVENTORY_SHOW.getMatcher(command)) != null) {
            System.out.println(controller.showInventory());
        } else if ((matcher = GameCommands.INVENTORY_TRASH.getMatcher(command)) != null) {
            System.out.println(controller.throwToInventoryTrash(matcher));
        }
        else if ((matcher = GameCommands.TOOLS_EQUIP.getMatcher(command)) != null) {
            System.out.println(controller.equipTool(matcher));
        } else if ((GameCommands.TOOL_SHOW_CURRENT.getMatcher(command)) != null) {
            System.out.println(controller.showCurrentTool());
        } else if ((GameCommands.TOOL_SHOW_AVAILABLE.getMatcher(command)) != null) {
            System.out.println(controller.showAvailableTools());
        }

        else if ((matcher = GameCommands.TOOL_USE.getMatcher(command)) != null) {
            System.out.println(controller.useTool(matcher));
        } else if ((matcher = GameCommands.TOOLS_UPGRADE.getMatcher(command)) != null) {
            System.out.println(controller.upgradeTool(matcher));
        }

        else if ((GameCommands.HOWMUCH_WATER.getMatcher(command)) != null) {
            System.out.println(controller.howMuchWater());
        }

        else if ((matcher = GameCommands.GO_FISHING.getMatcher(command)) != null) {
            System.out.println(controller.fishing(matcher));
        }
        else if ((matcher = GameCommands.SELL_PRODUCTS.getMatcher(command)) != null) {
            System.out.println(controller.sellProduct(matcher));
        }

        // NPC and friendships
        else if ((matcher = NPCGameCommand.MeetNPC.getMatcher(command)) != null) {
            result = npcController.meetNPC(matcher);
            System.out.println(result.message());
        } else if ((matcher = NPCGameCommand.GiveGiftToNPC.getMatcher(command)) != null) {
            result = npcController.giveGift(matcher);
            System.out.println(result.message());
        } else if ((matcher = NPCGameCommand.ShowFriendShipList.getMatcher(command)) != null) {
            result = npcController.showFriendship();
            System.out.println(result.message());
        } else if ((matcher = NPCGameCommand.ShowQuestsList.getMatcher(command)) != null) {
            result = npcController.showQuestsList();
            System.out.println(result.message());
        } else if ((matcher = NPCGameCommand.QuestsFinish.getMatcher(command)) != null) {
            result = npcController.finishQuests(matcher);
            System.out.println(result.message());
        }

        // Interactions with players :
        else if ((matcher = InteractionsCommand.ShowFriendshipList.getMatcher(command)) != null) {
            result = interactionsController.showFriendshipsList();
            System.out.println(result.message());
        } else if ((matcher = InteractionsCommand.Talk.getMatcher(command)) != null) {
            result = interactionsController.talk(matcher);
            System.out.println(result.message());
        } else if ((matcher = InteractionsCommand.TalkHistory.getMatcher(command)) != null) {
            result = interactionsController.talkHistory(matcher);
            System.out.println(result.message());
        } else if ((matcher = InteractionsCommand.Hug.getMatcher(command)) != null) {
            result = interactionsController.hug(matcher);
            System.out.println(result.message());
        } else if ((matcher = InteractionsCommand.Gift.getMatcher(command)) != null) {
            result = interactionsController.buyGift(matcher);
            System.out.println(result.message());
        } else if ((matcher = InteractionsCommand.GiftList.getMatcher(command)) != null) {
            result = interactionsController.showGiftsList();
            System.out.println(result.message());
        } else if ((matcher = InteractionsCommand.GiftRate.getMatcher(command)) != null) {
            result = interactionsController.getRateToGift(matcher);
            System.out.println(result.message());
        } else if ((matcher = InteractionsCommand.GiftHistory.getMatcher(command)) != null) {
            result = interactionsController.showGiftHistory(matcher);
            System.out.println(result.message());
        } else if ((matcher = InteractionsCommand.GetFlower.getMatcher(command)) != null) {
            result = interactionsController.getFlower(matcher);
            System.out.println(result.message());
        } else if ((matcher = GameCommands.DELETE_GAME.getMatcher(command)) != null) {
            result = controller.deleteGame();
            System.out.println(result.message());
            if (result.success()) {
                int count = 1;
                int tru = 0;
                while (count != 3) {
                    System.out.println("next player enter yse or no :");
                    String input = Input.getNextLine();
                    if (input.equalsIgnoreCase("yes")) {
                        System.out.println("next player enter yse or no :");
                        input = Input.getNextLine();
                        count ++;
                        tru ++;
                    } else if (input.equalsIgnoreCase("no")) {
                        System.out.println("next player enter yse or no :");
                        input = Input.getNextLine();
                        count ++;
                    } else {
                        System.out.println("next player enter yse or no :");
                        input = Input.getNextLine();
                    }
                }
                if (tru == 3) {
                    App app = App.getApp();
                    Game game = app.getCurrentGame();
                    for (Player player : game.getPlayers()) {
                        ProfileMenuController.setHighScore(player.getUsername());
                        app.getUsers().get(ProfileMenuController.getIndexInUsers(player.getUsername())).setCurrentGame(null);
                    }
                    app.setCurrentGame(null);
                    app.setCurrentMenu(Menu.MAIN_MENU);
                    System.out.println("The game has been successfully deleted. You're now back at the main menu!");
                } else {
                    System.out.println("The game cannot be deleted because not all players agreed to the removal.");
                }
            }

        } else {
            System.out.println("invalid command.");
        }

        // todo: check
        // Time checking
        result = controller.checkTime();
        if (result.success()) {
            System.out.println(result);
            System.out.println(controller.goNextDay());
        }
    }
}
