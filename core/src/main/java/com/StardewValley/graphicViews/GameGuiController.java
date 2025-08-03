package com.StardewValley.graphicViews;

import com.StardewValley.controllers.*;
import com.StardewValley.models.*;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.Enums.commands.GameCommands;
import com.StardewValley.models.Enums.commands.InteractionsCommand;
import com.StardewValley.models.Enums.commands.NPCGameCommand;
import com.StardewValley.views.AppView;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.regex.Matcher;


public class GameGuiController {

    public void handleButtonDisable(Game game, TextButton button) {
        if (!game.getPlayerInTurn().equals(game.getMainPlayer())) {
            button.setDisabled(true);
            button.setColor(Color.GRAY);
        } else {
            button.setDisabled(false);
            button.setColor(Color.WHITE);
        }
    }

    public void changeTurn() {
        Game game = App.getApp().getCurrentGame();
        game.nextTurn();
    }

    public String processCommand(String command) {
        Matcher matcher;
        Result result = null;

        GameController gameController = AppControllers.gameController;
        NPCGameController npcController = new NPCGameController();
        PlayersInteractionController interactionsController = AppControllers.playersInteractionController;
        if ((GameCommands.SHOW_CURRENT_MENU.getMatcher(command)) != null) {
            return (gameController.showCurrentMenu());
        } else if ((GameCommands.EXIT_GAME.getMatcher(command)) != null) {
            return (gameController.exitGame());
        } else if ((GameCommands.EXIT_APP.getMatcher(command)) != null) {
            gameController.exitApp();
        } else if ((GameCommands.NEXT_TURN.getMatcher(command)) != null) {
            return (gameController.goNextTurn().message());
        } else if ((GameCommands.TIME.getMatcher(command)) != null) {
            return (gameController.showTime());
        } else if ((GameCommands.DATE.getMatcher(command)) != null) {
            return (gameController.showDate());
        } else if ((GameCommands.DATE_AND_TIME.getMatcher(command)) != null) {
            return (gameController.showDateTime());
        } else if ((GameCommands.DAY_OF_WEEK.getMatcher(command)) != null) {
            return (gameController.showDayOfWeek());
        } else if ((matcher = GameCommands.CHEAT_ADVANCE_TIME.getMatcher(command)) != null) {
            return (gameController.cheatAdvanceTime(matcher).message());
        } else if ((matcher = GameCommands.CHEAT_ADVANCE_DATE.getMatcher(command)) != null) {
            return (gameController.cheatAdvanceDate(matcher).message());
        } else if ((GameCommands.SHOW_SEASON.getMatcher(command)) != null) {
            return (gameController.showSeason());
        } else if ((matcher = GameCommands.PRINT_MAP.getMatcher(command)) != null) {
            return (gameController.printMap(matcher).message());
        } else if (GameCommands.HELP_READING_MAP.getMatcher(command) != null) {
            return (gameController.helpReadingMap().message());
//        } else if ((matcher = GameCommands.WALK.getMatcher(command)) != null) {
//            int x = Integer.parseInt(matcher.group("x"));
//            int y = Integer.parseInt(matcher.group("y"));
//
//            result = gameController.walkToCheck(x, y);
//            return (result.message());
//            if (result.success()) {
//                return ("do you want to go? (y / n)");
//                String str = Input.getNextLine();
//                while (str.isEmpty()) {
//                    str = Input.getNextLine();
//                }
//                char character = str.charAt(0);
//                if (character == 'y' || character == 'Y') {
//                    return (gameController.walkTo().message());
//                }
//            }
        } else if ((matcher = GameCommands.SET_LOCATION.getMatcher(command)) != null) {
            return (gameController.setLocation(matcher).message());
        } else if ((GameCommands.SHOW_ENERGY.getMatcher(command)) != null) {
            return (gameController.showEnergy());
        } else if ((matcher = GameCommands.CHEAT_SET_ENERGY.getMatcher(command)) != null) {
            return (gameController.cheatSetEnergy(matcher));
        } else if ((GameCommands.CHEAT_ENERGY_UNLIMITED.getMatcher(command)) != null) {
            return (gameController.cheatEnergyUnlimited());
        } else if ((GameCommands.WEATHER.getMatcher(command)) != null) {
            return (gameController.showWeather());
        } else if ((GameCommands.WEATHER_FORECAST.getMatcher(command)) != null) {
            return (gameController.forecastWeather());
        } else if ((matcher = GameCommands.CHEAT_WEATHER_SET.getMatcher(command)) != null) {
            return (gameController.cheatWeather(matcher).message());
        } else if ((matcher = GameCommands.CHEAT_THOR.getMatcher(command)) != null) {
            return (gameController.cheatThor(matcher));
        } else if ((GameCommands.BUILD_GREENHOUSE.getMatcher(command)) != null) {
            result = gameController.buildGreenHouseRequest();
            if (result.success()) {
                return (gameController.buildGreenHouse());
            } else {
                return result.message();
            }
        } else if ((GameCommands.INVENTORY_SHOW.getMatcher(command)) != null) {
            return (gameController.showInventory());
        } else if ((matcher = GameCommands.INVENTORY_TRASH.getMatcher(command)) != null) {
            return (gameController.throwToInventoryTrash(matcher).message());
        } else if ((matcher = GameCommands.TOOLS_EQUIP.getMatcher(command)) != null) {
            return (gameController.equipTool(matcher).message());
        } else if ((GameCommands.TOOL_SHOW_CURRENT.getMatcher(command)) != null) {
            return (gameController.showCurrentTool().message());
        } else if ((GameCommands.TOOL_SHOW_AVAILABLE.getMatcher(command)) != null) {
            return (gameController.showAvailableTools());
        } else if ((matcher = GameCommands.TOOL_USE.getMatcher(command)) != null) {
            return (gameController.useTool(matcher).message());
        } else if ((matcher = GameCommands.TOOLS_UPGRADE.getMatcher(command)) != null) {
            return (gameController.upgradeTool(matcher).message());
        } else if ((GameCommands.HOWMUCH_WATER.getMatcher(command)) != null) {
            return (gameController.howMuchWater());
        } else if ((matcher = GameCommands.GO_FISHING.getMatcher(command)) != null) {
            return (gameController.fishing(matcher).message());
        } else if ((matcher = GameCommands.SELL_PRODUCTS.getMatcher(command)) != null) {
            return (gameController.sellProduct(matcher).message());
        }
        // friendship
        else if ((matcher = GameCommands.ASK_MARRIAGE.getMatcher(command)) != null) {
            return (gameController.askMarriage(matcher).message());
        } else if ((matcher = GameCommands.RESPOND_MARRIAGE.getMatcher(command)) != null) {
            return (gameController.respondToMarriage(matcher).message());
        }

        // NPC
        else if ((matcher = NPCGameCommand.MeetNPC.getMatcher(command)) != null) {
            result = npcController.meetNPC(matcher);
            return (result.message());
        } else if ((matcher = NPCGameCommand.GiveGiftToNPC.getMatcher(command)) != null) {
            result = npcController.giveGift(matcher);
            return (result.message());
        } else if ((matcher = NPCGameCommand.ShowFriendShipList.getMatcher(command)) != null) {
            result = npcController.showFriendship();
            return (result.message());
        } else if ((matcher = NPCGameCommand.ShowQuestsList.getMatcher(command)) != null) {
            result = npcController.showQuestsList();
            return (result.message());
        } else if ((matcher = NPCGameCommand.QuestsFinish.getMatcher(command)) != null) {
            result = npcController.finishQuests(matcher);
            return (result.message());
        } else if ((matcher = GameCommands.SHOW_CRAFT_INFO.getMatcher(command)) != null) {
            result = (gameController.showCraftInfo(matcher));
        } else if ((matcher = GameCommands.SHOW_TREE_INFO.getMatcher(command)) != null) {
            result = (gameController.showTreeInfo(matcher));
        } else if ((matcher = GameCommands.SHOW_PLANT.getMatcher(command)) != null) {
            result = (gameController.showPlant(matcher));
        } else if ((matcher = GameCommands.SHOW_TREE.getMatcher(command)) != null) {
            result = (gameController.showTree(matcher));
        } else if ((matcher = GameCommands.FERTILIZE.getMatcher(command)) != null) {
            result = (gameController.fertilize(matcher));
        } else if ((matcher = GameCommands.PLANT.getMatcher(command)) != null) {
            result = (gameController.plant(matcher));
        } else if ((GameCommands.SHOW_CRAFTING_RECIPES.getMatcher(command)) != null) {
            result = (gameController.showCraftingRecipes());
        } else if ((matcher = GameCommands.CRAFT.getMatcher(command)) != null) {
            result = (gameController.Craft(matcher));
        } else if ((matcher = GameCommands.CHEAT_ADD_CRAFTING_RECIPE.getMatcher(command)) != null) {
            result = (gameController.cheatAddCraftingRecipe(matcher));
        } else if ((matcher = GameCommands.CHEAT_ADD_FOOD_RECIPE.getMatcher(command)) != null) {
            result = (gameController.cheatAddFoodRecipe(matcher));
        } else if ((matcher = GameCommands.CHEAT_ADD_ITEM.getMatcher(command)) != null) {
            result = (gameController.cheatAddToInventory(matcher));
        } else if ((matcher = GameCommands.PLACE_ITEM.getMatcher(command)) != null) {
            result = (gameController.placeItem(matcher));
        } else if ((matcher = GameCommands.COOKING_REFRIGERATOR.getMatcher(command)) != null) {
            result = (gameController.manageRefrigerator(matcher));
        } else if ((matcher = GameCommands.COOK.getMatcher(command)) != null) {
            result = (gameController.cook(matcher));
        } else if ((matcher = GameCommands.EAT.getMatcher(command)) != null) {
            result = (gameController.eatFood(matcher));
        } else if ((GameCommands.SHOW_FOOD_RECIPES.getMatcher(command)) != null) {
            result = (gameController.showCookingRecipes());
        } else if ((matcher = GameCommands.PET.getMatcher(command)) != null) {
            result = (gameController.petAnimal(matcher));
        } else if ((matcher = GameCommands.CHEAT_FRIENDSHIP_ANIMAL.getMatcher(command)) != null) {
            result = (gameController.cheatFriendshipAnimal(matcher));
        } else if ((GameCommands.SHOW_ANIMALS.getMatcher(command)) != null) {
            result = (gameController.showAnimalsInfo());
        } else if ((matcher = GameCommands.SHEPHERD_ANIMALS.getMatcher(command)) != null) {
            result = (gameController.shepherdAnimals(matcher));
        } else if ((matcher = GameCommands.FEED_ANIMAL.getMatcher(command)) != null) {
            result = (gameController.feedHayAnimal(matcher));
        } else if ((matcher = GameCommands.SHOW_ANIMAL_PRODUCTS.getMatcher(command)) != null) {
            result = (gameController.showAnimalsProducts(matcher));
        } else if ((matcher = GameCommands.COLLECT_PRODUCE.getMatcher(command)) != null) {
            result = (gameController.collectProducts(matcher));
        } else if ((matcher = GameCommands.ARTISAN_USE.getMatcher(command)) != null) {
            result = (gameController.artisanUse(matcher));
        } else if ((matcher = GameCommands.ARTISAN_GET.getMatcher(command)) != null) {
            result = (gameController.artisanGet(matcher));
        } else if ((matcher = GameCommands.PURCHASE.getMatcher(command)) != null) {
            result = (gameController.purchaseProduct(matcher));
        } else if ((GameCommands.SHOW_ALL_PRODUCTS.getMatcher(command)) != null) {
            result = (gameController.showAllProducts());
        } else if ((GameCommands.SHOW_AVAILABLE_PRODUCTS.getMatcher(command)) != null) {
            result = (gameController.showAllAvailableProducts());
        } else if ((matcher = GameCommands.START_TRADE.getMatcher(command)) != null) {
            result = (gameController.startTrade(matcher));
        } else if ((matcher = GameCommands.TRADE.getMatcher(command)) != null) {
            result = (gameController.trade(matcher));
        } else if ((matcher = GameCommands.SHOW_TRADES_LIST.getMatcher(command)) != null) {
            result = (gameController.ShowTradeList(matcher));
        } else if ((matcher = GameCommands.TRADE_HISTORY.getMatcher(command)) != null) {
            result = (gameController.showTradeHistory(matcher));
        } else if ((matcher = GameCommands.TRADE_RESPONSE.getMatcher(command)) != null) {
            result = (gameController.responseToTrade(matcher));
        } else if ((matcher = GameCommands.CHEAT_ADD_MONEY.getMatcher(command)) != null) {
            result = (gameController.cheatAddMoney(matcher));
        } else if ((matcher = GameCommands.BUILD.getMatcher(command)) != null) {
            result = (gameController.build(matcher));
        } else if ((matcher = GameCommands.BUY_ANIMAL.getMatcher(command)) != null) {
            result = (gameController.buyAnimal(matcher));
        } else if ((matcher = GameCommands.SELL_ANIMAL.getMatcher(command)) != null) {
            result = (gameController.sellAnimal(matcher));
        }

        // Interactions with players :
        else if ((matcher = InteractionsCommand.ShowFriendshipList.getMatcher(command)) != null) {
            result = interactionsController.showFriendshipsList();
        } else if ((matcher = InteractionsCommand.Talk.getMatcher(command)) != null) {
            result = interactionsController.talk(matcher);
        } else if ((matcher = InteractionsCommand.TalkHistory.getMatcher(command)) != null) {
            result = interactionsController.talkHistory(matcher);
        } else if ((matcher = InteractionsCommand.Hug.getMatcher(command)) != null) {
            result = interactionsController.hug(matcher);
        } else if ((matcher = InteractionsCommand.Gift.getMatcher(command)) != null) {
            result = interactionsController.buyGift(matcher);
        } else if ((matcher = InteractionsCommand.GiftList.getMatcher(command)) != null) {
            result = interactionsController.showGiftsList();
        } else if ((matcher = InteractionsCommand.GiftRate.getMatcher(command)) != null) {
            result = interactionsController.getRateToGift(matcher);
        } else if ((matcher = InteractionsCommand.GiftHistory.getMatcher(command)) != null) {
            result = interactionsController.showGiftHistory(matcher);
        } else if ((matcher = InteractionsCommand.GetFlower.getMatcher(command)) != null) {
            result = interactionsController.getFlower(matcher);
//        } else if ((matcher = GameCommands.DELETE_GAME.getMatcher(command)) != null) {
//            result = gameController.deleteGame();
//            if (result.success()) {
//                int count = 0;
//                int accepted = 0;
//                while (count != 3) {
//                     ("next player enter yes or no :");
//                    String input = Input.getNextLine();
//                    if (input.equalsIgnoreCase("yes")) {
//                        return ("next player enter yes or no :");
//                        input = Input.getNextLine();
//                        count++;
//                        accepted++;
//                    } else if (input.equalsIgnoreCase("no")) {
//                        return ("next player enter yes or no :");
//                        input = Input.getNextLine();
//                        count++;
//                    } else {
//                        return ("enter yes or no please :");
//                        input = Input.getNextLine();
//                    }
//                }
//                if (accepted == 3) {
//                    App app = App.getApp();
//                    Game game = app.getCurrentGame();
//                    for (Player player : game.getPlayers()) {
//                        ProfileMenuController.setHighScore(player.getUsername());
//                        app.getUsers().get(ProfileMenuController.getIndexInUsers(player.getUsername())).setCurrentGame(null);
//                    }
//                    app.removeGame(app.getCurrentGame());
//                    app.setCurrentGame(null);
//                    app.setCurrentMenu(Menu.MAIN_MENU);
//                    return ("The game has been successfully deleted. You're now back at the main menu!");
//                } else {
//                    return ("The game cannot be deleted because not all players agreed to the removal.");
//                }
//            }
        } else if ((GameCommands.SHOW_MONEY.getMatcher(command)) != null) {
            return (gameController.showMoney());
        } else {
            return ("invalid command.");
        }
        if (result != null) return result.message();
        else return ("invalid command.");
    }
}
