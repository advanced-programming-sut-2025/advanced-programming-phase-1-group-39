package views;

import controllers.AppControllers;
import controllers.GameController;
import controllers.NPCGameController;
import models.Enums.commands.NPCGameCommand;
import models.Enums.commands.GameCommands;
import models.Input;
import models.Result;

import java.util.regex.Matcher;

public class GameView implements View {
    GameController controller = AppControllers.gameController;
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

        else if ((matcher = GameCommands.SHOW_CRAFT_INFO.getMatcher(command)) != null) {
            System.out.println(controller.showCraftInfo(matcher));
        } else if ((matcher = GameCommands.SHOW_TREE_INFO.getMatcher(command)) != null) {
            System.out.println(controller.showTreeInfo(matcher));
        } else if ((matcher = GameCommands.SHOW_PLANT.getMatcher(command)) != null) {
            System.out.println(controller.showPlant(matcher));
        } else if ((matcher = GameCommands.SHOW_TREE.getMatcher(command)) != null) {
            System.out.println(controller.showTree(matcher));
        } else if ((matcher = GameCommands.FERTILIZE.getMatcher(command)) != null) {
            System.out.println(controller.fertilize(matcher));
        } else if ((matcher = GameCommands.PLANT.getMatcher(command)) != null) {
            System.out.println(controller.plant(matcher));
        } else if ((GameCommands.SHOW_CRAFTING_RECIPES.getMatcher(command)) != null) {
            System.out.println(controller.showCraftingRecipes());
        } else if ((matcher = GameCommands.CRAFT.getMatcher(command)) != null) {
            System.out.println(controller.Craft(matcher));
        } else if ((matcher = GameCommands.CHEAT_ADD_ITEM.getMatcher(command)) != null) {
            System.out.println(controller.cheatAddToInventory(matcher));
        } else if ((matcher = GameCommands.COOKING_REFRIGERATOR.getMatcher(command)) != null) {
            System.out.println(controller.manageRefrigerator(matcher));
        } else if ((matcher = GameCommands.COOK.getMatcher(command)) != null) {
            System.out.println(controller.cook(matcher));
        } else if ((matcher = GameCommands.EAT.getMatcher(command)) != null) {
            System.out.println(controller.eatFood(matcher));
        } else if ((GameCommands.SHOW_FOOD_RECIPES.getMatcher(command)) != null) {
            System.out.println(controller.showCookingRecipes());
        } else if ((matcher = GameCommands.PET.getMatcher(command)) != null) {
            System.out.println(controller.petAnimal(matcher));
        } else if ((matcher = GameCommands.CHEAT_FRIENDSHIP_ANIMAL.getMatcher(command)) != null) {
            System.out.println(controller.cheatFriendshipAnimal(matcher));
        } else if ((GameCommands.SHOW_ANIMALS.getMatcher(command)) != null) {
            System.out.println(controller.showAnimalsInfo());
        } else if ((matcher = GameCommands.SHEPHERD_ANIMALS.getMatcher(command)) != null) {
            System.out.println(controller.shepherdAnimals(matcher));
        } else if ((matcher = GameCommands.FEED_ANIMAL.getMatcher(command)) != null) {
            System.out.println(controller.feedHayAnimal(matcher));
        } else if ((matcher = GameCommands.SHOW_ANIMAL_PRODUCTS.getMatcher(command)) != null) {
            System.out.println(controller.showAnimalsProducts(matcher));
        } else if ((matcher = GameCommands.COLLECT_PRODUCE.getMatcher(command)) != null) {
            System.out.println(controller.collectProducts(matcher));
        } else if ((matcher = GameCommands.ARTISAN_USE.getMatcher(command)) != null) {
            System.out.println(controller.artisanUse(matcher));
        } else if ((matcher = GameCommands.ARTISAN_GET.getMatcher(command)) != null) {
            System.out.println(controller.artisanGet(matcher));
        } else if ((matcher = GameCommands.PURCHASE.getMatcher(command)) != null) {
            System.out.println(controller.purchaseProduct(matcher));
        } else if ((GameCommands.SHOW_ALL_PRODUCTS.getMatcher(command)) != null) {
            System.out.println(controller.showAllProducts());
        } else if ((GameCommands.SHOW_AVAILABLE_PRODUCTS.getMatcher(command)) != null) {
            System.out.println(controller.showAllAvailableProducts());
        } else if ((matcher = GameCommands.START_TRADE.getMatcher(command)) != null) {
            System.out.println(controller.startTrade(matcher));
        } else if ((matcher = GameCommands.TRADE.getMatcher(command)) != null) {
            System.out.println(controller.trade(matcher));
        } else if ((matcher = GameCommands.SHOW_TRADES_LIST.getMatcher(command)) != null) {
            System.out.println(controller.ShowTradeList(matcher));
        } else if ((matcher = GameCommands.TRADE_HISTORY.getMatcher(command)) != null) {
            System.out.println(controller.showTradeHistory(matcher));
        } else if ((matcher = GameCommands.TRADE_RESPONSE.getMatcher(command)) != null) {
            System.out.println(controller.responseToTrade(matcher));
        } else if ((matcher = GameCommands.CHEAT_ADD_MONEY.getMatcher(command)) != null) {
            System.out.println(controller.cheatAddMoney(matcher));
        } else if ((matcher = GameCommands.BUILD.getMatcher(command)) != null) {
            System.out.println(controller.build(matcher));
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

        else {
            System.out.println("invalid command.");
        }


        // Time checking
        result = controller.checkTime();
        if (result.success()) {
            System.out.println(result);
            System.out.println(controller.goNextDay());
        }
    }
}
