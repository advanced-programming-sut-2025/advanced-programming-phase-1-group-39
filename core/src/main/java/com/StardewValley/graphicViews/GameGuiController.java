package com.StardewValley.graphicViews;

import com.StardewValley.controllers.AppControllers;
import com.StardewValley.controllers.GameController;
import com.StardewValley.controllers.NPCGameController;
import com.StardewValley.controllers.PlayersInteractionController;
import com.StardewValley.models.*;
import com.StardewValley.models.Enums.commands.GameCommands;
import com.StardewValley.models.Enums.commands.InteractionsCommand;
import com.StardewValley.models.Enums.commands.NPCGameCommand;
import com.StardewValley.models.animals.Animal;
import com.StardewValley.models.buildings.AnimalBuilding;
import com.StardewValley.models.cooking.FoodManager;
import com.StardewValley.models.cooking.FoodRecipe;
import com.StardewValley.models.crafting.CraftingManager;
import com.StardewValley.models.crafting.CraftingRecipe;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.regex.Matcher;


public class GameGuiController {
    GameScreen screen;

    public void setScreen(GameScreen screen) {
        this.screen = screen;
    }

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
        Game game = screen.getGame();
        boolean shouldGoNextDay = !game.nextTurn();
        if (shouldGoNextDay) {
            screen.showError("All players are not conscious! Going to next day ...");
            screen.delayForAndDo(1.0f, () -> screen.blackBackgroundAnimation(game::goToNextDay, 1.0f));
        } else {
            screen.showError("Next turn : " + game.getPlayerInTurn().getNickname());
        }
    }

    public boolean nearGreenHouse(Player player) {
        Game game = screen.getGame();
        if (game.getMap().isNearBuilding(player, player.getBuildingByName("greenhouse"))) {
            return true;
        }
        return false;
    }

    public Result buildGreenHouseRequest(Player player) {
        if(player.canBuildGreenHouse()) {
            return new Result(true, "You are already building a green house!\ndo you want to build it?");
        } else {
            return new Result(false, "Needed resources to build the greenhouse: \n\t500 woods\n\t1000 G money");
        }
    }

    public String buildGreenhouse(Player player, Game game) {
        player.buildGreenHouse();
        (player.getBuildingByName("greenhouse")).updateMap(game.getMap());
        player.getBuildingByName("greenhouse").updateMap(game.getMap());
        return "your green house was built!";
    }

    public void cook(FoodRecipe recipe) {
        Result result = FoodManager.cook(recipe.name(), App.getApp().getCurrentGame().getPlayerInTurn());
        System.out.println(result.message());
    }

    public void craft(CraftingRecipe recipe, Player player) {
        System.out.println(CraftingManager.craft(recipe.getName(), player));
    }

    // Animal
    public void sellAnimal(Animal animal) {
        screen.showPopup("Do you REALLy want to sell " + animal.getName() + " ?", () -> {
            return;
        });
    }

    public void shepherdAnimal(Animal animal) {
        Player player = screen.getGame().getPlayerInTurn();
        AnimalBuilding building = player.getAnimalLivingPlaceBuilding(animal);
        Location buildingLocation = building.getLocation();
        animal.sendOutside(buildingLocation.x() + 1, buildingLocation.y() + building.getHeight() + 1);

        screen.showError("You shepherd animal! " + animal.getName() + " goes outside");
    }

    public String processCommand(String command) {
        Matcher matcher;
        Result result = null;

        String message = "";
        GameController gameController = AppControllers.gameController;
        NPCGameController npcController = new NPCGameController();
        PlayersInteractionController interactionsController = AppControllers.playersInteractionController;
        if ((GameCommands.SHOW_CURRENT_MENU.getMatcher(command)) != null) {
            message = (gameController.showCurrentMenu());
        } else if ((GameCommands.EXIT_GAME.getMatcher(command)) != null) {
            message = (gameController.exitGame());
        } else if ((GameCommands.EXIT_APP.getMatcher(command)) != null) {
            gameController.exitApp();
        } else if ((GameCommands.NEXT_TURN.getMatcher(command)) != null) {
            message = (gameController.goNextTurn().message());
        } else if ((GameCommands.TIME.getMatcher(command)) != null) {
            message = (gameController.showTime());
        } else if ((GameCommands.DATE.getMatcher(command)) != null) {
            message = (gameController.showDate());
        } else if ((GameCommands.DATE_AND_TIME.getMatcher(command)) != null) {
            message = (gameController.showDateTime());
        } else if ((GameCommands.DAY_OF_WEEK.getMatcher(command)) != null) {
            message = (gameController.showDayOfWeek());
        } else if ((matcher = GameCommands.CHEAT_ADVANCE_TIME.getMatcher(command)) != null) {
            message = (gameController.cheatAdvanceTime(matcher).message());
        } else if ((matcher = GameCommands.CHEAT_ADVANCE_DATE.getMatcher(command)) != null) {
            message = (gameController.cheatAdvanceDate(matcher).message());
        } else if ((GameCommands.SHOW_SEASON.getMatcher(command)) != null) {
            message = (gameController.showSeason());
        } else if ((matcher = GameCommands.PRINT_MAP.getMatcher(command)) != null) {
            message = (gameController.printMap(matcher).message());
        } else if (GameCommands.HELP_READING_MAP.getMatcher(command) != null) {
            message = (gameController.helpReadingMap().message());
//        } else if ((matcher = GameCommands.WALK.getMatcher(command)) != null) {
//            int x = Integer.parseInt(matcher.group("x"));
//            int y = Integer.parseInt(matcher.group("y"));
//
//            result = gameController.walkToCheck(x, y);
//            message = (result.message());
//            if (result.success()) {
//                message = ("do you want to go? (y / n)");
//                String str = Input.getNextLine();
//                while (str.isEmpty()) {
//                    str = Input.getNextLine();
//                }
//                char character = str.charAt(0);
//                if (character == 'y' || character == 'Y') {
//                    message = (gameController.walkTo().message());
//                }
//            }
        } else if ((matcher = GameCommands.SET_LOCATION.getMatcher(command)) != null) {
            message = (gameController.setLocation(matcher).message());
        } else if ((GameCommands.SHOW_ENERGY.getMatcher(command)) != null) {
            message = (gameController.showEnergy());
        } else if ((matcher = GameCommands.CHEAT_SET_ENERGY.getMatcher(command)) != null) {
            message = (gameController.cheatSetEnergy(matcher));
        } else if ((GameCommands.CHEAT_ENERGY_UNLIMITED.getMatcher(command)) != null) {
            message = (gameController.cheatEnergyUnlimited());
        } else if ((GameCommands.WEATHER.getMatcher(command)) != null) {
            message = (gameController.showWeather());
        } else if ((GameCommands.WEATHER_FORECAST.getMatcher(command)) != null) {
            message = (gameController.forecastWeather());
        } else if ((matcher = GameCommands.CHEAT_WEATHER_SET.getMatcher(command)) != null) {
            message = (gameController.cheatWeather(matcher).message());
        } else if ((matcher = GameCommands.CHEAT_THOR.getMatcher(command)) != null) {
            message = (gameController.cheatThor(matcher));
        } else if ((GameCommands.BUILD_GREENHOUSE.getMatcher(command)) != null) {
            result = gameController.buildGreenHouseRequest();
            if (result.success()) {
                message = (gameController.buildGreenHouse());
            } else {
                message = result.message();
            }
        } else if ((GameCommands.INVENTORY_SHOW.getMatcher(command)) != null) {
            message = (gameController.showInventory());
        } else if ((matcher = GameCommands.INVENTORY_TRASH.getMatcher(command)) != null) {
            message = (gameController.throwToInventoryTrash(matcher).message());
        } else if ((matcher = GameCommands.TOOLS_EQUIP.getMatcher(command)) != null) {
            message = (gameController.equipTool(matcher).message());
        } else if ((GameCommands.TOOL_SHOW_CURRENT.getMatcher(command)) != null) {
            message = (gameController.showCurrentTool().message());
        } else if ((GameCommands.TOOL_SHOW_AVAILABLE.getMatcher(command)) != null) {
            message = (gameController.showAvailableTools());
        } else if ((matcher = GameCommands.TOOL_USE.getMatcher(command)) != null) {
            message = (gameController.useTool(matcher).message());
        } else if ((matcher = GameCommands.TOOLS_UPGRADE.getMatcher(command)) != null) {
            message = (gameController.upgradeTool(matcher).message());
        } else if ((GameCommands.HOWMUCH_WATER.getMatcher(command)) != null) {
            message = (gameController.howMuchWater());
        } else if ((matcher = GameCommands.GO_FISHING.getMatcher(command)) != null) {
            message = (gameController.fishing(matcher).message());
        } else if ((matcher = GameCommands.SELL_PRODUCTS.getMatcher(command)) != null) {
            message = (gameController.sellProduct(matcher).message());
        }
        // friendship
        else if ((matcher = GameCommands.ASK_MARRIAGE.getMatcher(command)) != null) {
            message = (gameController.askMarriage(matcher).message());
        } else if ((matcher = GameCommands.RESPOND_MARRIAGE.getMatcher(command)) != null) {
            message = (gameController.respondToMarriage(matcher).message());
        }

        // NPC
        else if ((matcher = NPCGameCommand.MeetNPC.getMatcher(command)) != null) {
            result = npcController.meetNPC(matcher);
            message = (result.message());
        } else if ((matcher = NPCGameCommand.GiveGiftToNPC.getMatcher(command)) != null) {
            result = npcController.giveGift(matcher);
            message = (result.message());
        } else if ((matcher = NPCGameCommand.ShowFriendShipList.getMatcher(command)) != null) {
            result = npcController.showFriendship();
            message = (result.message());
        } else if ((matcher = NPCGameCommand.ShowQuestsList.getMatcher(command)) != null) {
            result = npcController.showQuestsList();
            message = (result.message());
        } else if ((matcher = NPCGameCommand.QuestsFinish.getMatcher(command)) != null) {
            result = npcController.finishQuests(matcher);
            message = (result.message());
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
//                        message = ("next player enter yes or no :");
//                        input = Input.getNextLine();
//                        count++;
//                        accepted++;
//                    } else if (input.equalsIgnoreCase("no")) {
//                        message = ("next player enter yes or no :");
//                        input = Input.getNextLine();
//                        count++;
//                    } else {
//                        message = ("enter yes or no please :");
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
//                    message = ("The game has been successfully deleted. You're now back at the main menu!");
//                } else {
//                    message = ("The game cannot be deleted because not all players agreed to the removal.");
//                }
//            }
        } else if ((GameCommands.SHOW_MONEY.getMatcher(command)) != null) {
            message = (gameController.showMoney());
        } else {
            message = ("invalid command.");
        }

        if (result != null) message = result.message();

        return stripAnsiCodes(message);
    }

    public static String stripAnsiCodes(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("\\u001B\\[[;\\d]*m", "");
    }
}
