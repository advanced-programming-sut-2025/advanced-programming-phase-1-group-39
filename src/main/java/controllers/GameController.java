package controllers;

import models.*;
import models.Enums.Direction;
import models.Enums.WeatherStatus;
import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.TreeManager;
import models.inventory.Inventory;
import models.inventory.TrashType;
import models.map.AnsiColors;
import models.map.MapMinPathFinder;
import models.map.Tile;
import models.tools.Axe;
import models.tools.Pickaxe;
import models.tools.Tool;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class GameController {
    public ArrayList<MapMinPathFinder.Node> path;

    public Result saveGame() {return null;}
    public Result exitGame() {return null;}
    public Result deleteGame() {return null;}

    public Result goNextTurn() {
        Game game = App.getApp().getCurrentGame();

        if (!game.nextTurn()) {
            return new Result(false, "All players are not conscious!\n"
                    + AnsiColors.ANSI_GREEN_BOLD + "Going next day!" + AnsiColors.ANSI_RESET
            + game.goToNextDay());
        }

        Player player = game.getPlayerInTurn();
        return new Result(true, AnsiColors.ANSI_CYAN_BOLD +
                "Next turn: " + App.getApp().getUserByPlayer(player).getNickname() + AnsiColors.ANSI_RESET);
    }

    public String showTime() {
        return App.getApp().getCurrentGame().getTime().getHourText();
    }
    public String showDate() {
        Time time = App.getApp().getCurrentGame().getTime();
        return time.getDateDetail() + "\n" + time.getDayDetail();
    }
    public String showDateTime() {
        return showDate() + "\n" + "Hour: " + showTime();
    }
    public String showDayOfWeek() {
        return App.getApp().getCurrentGame().getTime().getDayOfWeek().name();
    }
    public Result cheatAdvanceTime(Matcher matcher) {
        int h = Integer.parseInt(matcher.group("h"));
        if (h < 0) {
            return new Result(false, "hour can't be negative");
        }

        Game game = App.getApp().getCurrentGame();
        game.addToHour(h);
        return new Result(true, "hour cheated. now hour: " + game.getTime().getHour());
    }
    public Result cheatAdvanceDate(Matcher matcher) {
        int d = Integer.parseInt(matcher.group("d"));
        if (d < 0) {
            return new Result(false, "day can't be negative");
        }

        Game game = App.getApp().getCurrentGame();
        game.addToDay(d);
        return new Result(true, "day cheated. now Date: \n" + showDate());

    }

    public String showSeason() {
        Time time = App.getApp().getCurrentGame().getTime();
        return time.getSeason().name();
    }

    public String cheatThor(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));

        Game game = App.getApp().getCurrentGame();
        game.getTodayWeather().cheatThor(game.getMap().getTile(x, y));
        return "There was a thunderstorm to tile at (" + x + "," + y + ")";
    }

    public String showWeather() {
        Game game = App.getApp().getCurrentGame();
        return "Weather of Today: " +
                AnsiColors.ANSI_CYAN_BOLD +  game.getTodayWeather() + AnsiColors.ANSI_RESET;
    }
    public String forecastWeather() {
        Game game = App.getApp().getCurrentGame();
        return "Let's forecast the weather...\n" + "Weather of Tommorow: " +
                AnsiColors.ANSI_CYAN_BOLD +  game.getTomorrowWeather() + AnsiColors.ANSI_RESET;
    }
    public Result cheatWeather(Matcher matcher) {
        Game game = App.getApp().getCurrentGame();
        String type = matcher.group("type");
        WeatherStatus status = WeatherStatus.getWeatherStatusByName(type);
        if (status == null) return new Result(false, "Wrong Weather type!");
        game.setTomorrowWeather(status);
        return new Result(true, "You cheated the weather! Tomorrow weather is " +
                AnsiColors.ANSI_CYAN_BOLD + type + AnsiColors.ANSI_RESET);
    }

    public Result buildGreenHouseRequest() {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        if (player.canBuildGreenHouse()) {
            return new Result(true, "You are already building a green house!\ndo you want to build it? (y / n)");
        } else {
            return new Result(false, AnsiColors.ANSI_RED +  "You don't have enough resources to build a green house!\n" + AnsiColors.ANSI_RESET
             + "Needed resources: \n\t500 woods\n\t1000 G money");
        }
    }
    public String buildGreenHouse() {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();
        player.buildGreenHouse();
        player.getBuildingByName("greenhouse").updateMap(game.getMap());
        return "your green house was built!";
    }

    public Result walkToCheck(int x, int y) {
        Location end = new Location(x, y);

        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();
        Location start = player.getLocation();
        Result result;
        if (!(result = game.getMap().canWalkTo(start, end, player)).success()) {
            return result;
        }

        this.path = game.getMap().findWalkingPath(start, end, player);

        StringBuilder text = new StringBuilder();
        if(path.isEmpty()) return new Result(false, "No path found to "+end+"!");

        path.forEach(
                (n)-> {
                    text.append(n.location + " ");
                }
        );
        double energy = path.get(path.size() - 1).getEnergyCost();
        return new Result(true, "path: " + text
                + "\nEnergy needed: " + AnsiColors.ANSI_ORANGE_BOLD + energy + AnsiColors.ANSI_RESET
                + "\nYour energy: " + player.getColoredEnergy());
    }
    public Result walkTo() {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        MapMinPathFinder.Node endNode = path.get(path.size() - 1);

        double energyNeeded = endNode.getEnergyCost();
        Location end = endNode.location;
        if (player.getEnergy() < energyNeeded) {
            energyNeeded = player.getEnergy();
            // find how many of path player can go
            for (int i = 0; i < path.size(); i++) {
                MapMinPathFinder.Node node = path.get(i);

                if (node.getEnergyCost() > player.getEnergy()) {
                    if (i == 0) end = node.location;
                    else end = path.get(i-1).location;
                    break;
                }
            }
        }

        player.setLocationAbsolut(end.x(), end.y());
        player.changeEnergy(-energyNeeded);

        StringBuilder text = new StringBuilder();
        text.append("You walked to " + end);
        if (!player.isConscious()) return new Result(false, text + "\n" + AnsiColors.ANSI_RED + "Now you are not conscious!" + AnsiColors.ANSI_RESET);
        return new Result(true, text.toString());
    }

    public Result printMap(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        int size = Integer.parseInt(matcher.group("size"));

        Game game = App.getApp().getCurrentGame();
        return new Result(true, game.getMap().printMapBySize(x, y, size, game.getPlayers()));
    }
    public Result helpReadingMap() {
        Game game = App.getApp().getCurrentGame();
        return new Result(true, game.getMap().helpReadingMap());
    }


    public String showEnergy() {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        return "Your Energy: " + player.getColoredEnergy();
    }
    public String cheatSetEnergy(Matcher matcher) {
        int value = Integer.parseInt(matcher.group("value"));
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        player.setCheatedEnergy();
        player.resetEnergyUnlimited();
        player.setEnergy(value);
        return "now your energy is " + player.getColoredEnergy();
    }
    public String cheatEnergyUnlimited() {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        player.setEnergyUnlimited();
        return "now your energy is " + AnsiColors.ANSI_GREEN_BOLD + "UNLIMITED" + AnsiColors.ANSI_RESET;
    }

    public String showInventory() {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        return player.getInventory().showInventory();
    }
    public Result throwToInventoryTrash(Matcher matcher) {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        String itemName = matcher.group("itemName");
        int number = Integer.parseInt(matcher.group("number"));

        Inventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItemByName(itemName);
        if (itemStack == null)
            return new Result(false, "No item found with name " + itemName);

        int priceBacked = inventory.trashItem(itemStack, number);
        player.changeMoney(priceBacked);

        StringBuilder text = new StringBuilder("You trashed item " + itemName + " x" + number);
        if (inventory.getTrashType() != TrashType.BASIC)
           text.append(text + AnsiColors.ANSI_YELLOW + "\nYour trash is " + inventory.getTrashTypeName() + " and you saved " + priceBacked + "G !!");

        return new Result(true, text.toString());
    }

    public Result equipTool(Matcher matcher) {
        String toolName = matcher.group("toolName");

        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        ItemStack item = player.getInventory().getItemByName(toolName);
        if (item == null || !(item.getItem() instanceof Tool))
            return new Result(false, "No Tool found with name " + toolName);
        player.getInventory().setInHand(item);
        return new Result(true, "You equipped " + toolName);
    }
    public Result showCurrentTool() {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        ItemStack item = player.getInventory().getInHand();
        if (item == null || !(item.getItem() instanceof Tool))
            return new Result(false, "You did not equip any tool!");

        return new Result(true, "Your tool in hand is :" + item.getItem().getName());
    }
    public String showAvailableTools() {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        return player.getInventory().showTools();
    }
    public Result upgradeTool(Matcher matcher) {
        return null;
    }
    public Result useTool(Matcher matcher) {
        String dir = matcher.group("direction");
        Direction direction = Direction.getDirection(dir);
        if (direction == null)
            return new Result(false, "Invalid direction!");

        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();
        Tile tile = game.getMap().getTile(player.getLocation().x() + direction.dx,
                player.getLocation().y() + direction.dy);
        if (tile == null) return new Result(false, "Use on which tile?!");

        ItemStack itemStack = player.getInventory().getInHand();
        if (itemStack == null || !(itemStack.getItem() instanceof Tool))
            return new Result(false, "You did not equip any tool!");

        Tool tool = (Tool) itemStack.getItem();
        Result toolResult = tool.useTool(tile, player);

        int energyConsumed = tool.getUsingEnergy(player.getSkills(), game.getTodayWeather());
        if (!toolResult.success()) {
            if (tool instanceof Pickaxe || tool instanceof Axe)
                energyConsumed -= 1;
            if (player.getTurnEnergy() <= energyConsumed) {
                return new Result(false, AnsiColors.ANSI_ORANGE_BOLD + "You don't have enough energy!😓" + AnsiColors.ANSI_RESET);
            }
            player.changeEnergy(energyConsumed);
            return new Result(true, toolResult +
                    AnsiColors.ANSI_ORANGE_BOLD + "\nConsumed Energy: " + energyConsumed + AnsiColors.ANSI_RESET);
        } else {
            if (player.getTurnEnergy() <= energyConsumed) {
                return new Result(false, "You don't have enough energy!");
            }
            player.changeEnergy(energyConsumed);

            if (tool instanceof Pickaxe) {

            }
            return new Result(true, "");
        }
    }

    public Result showCraftInfo(Matcher matcher) {
        return new Result(true, CropManager.getCropInfo(matcher.group(1)));
    }
    public Result showTreeInfo(Matcher matcher) {
        return new Result(true, TreeManager.getTreeInfo(matcher.group(1)));
    }

    public Result plant(Matcher matcher) {return null;}
    public Result showPlant(Matcher matcher) {return null;}
    public Result fertilize(Matcher matcher) {return null;}
    public Result howMuchWater() {return null;}

    public Result showCraftingRecipes() {return null;}
    public Result Craft(Matcher matcher) {return null;}
    public Result placeItem(Matcher matcher) {return null;}
    public Result cheatAddToInventory(Matcher matcher) {return null;}
    public Result manageRefrigerator(Matcher matcher) {return null;}
    private Result putInRefrigerator(Item item, int amount) {return null;}
    private Result pickFromRefrigerator(Item item, int amount) {return null;}
    public Result showCookingRecipes() {return null;}
    public Result cook(Matcher matcher) {return null;}

    public Result eatFood(Matcher matcher) {return null;}

    public Result build(Matcher matcher) {return null;}
    public Result buyAnimal(Matcher matcher) {return null;}
    public Result petAnimal(Matcher matcher) {return null;}
    public Result showAnimalsInfo(Matcher matcher) {return null;}
    public Result shepherdAnimals(Matcher matcher) {return null;}
    public Result feedHayAnimal (Matcher matcher) {return null;}
    public Result showAnimalsProducts(Matcher matcher) {return null;}
    public Result collectProducts(Matcher matcher) {return null;}
    public Result sellAnimal(Matcher matcher) {return null;}

    public Result fishing(Matcher matcher) {return null;}

    public Result artisanUse(Matcher matcher) {return null;}
    public Result artisanGet(Matcher matcher) {return null;}

    public Result showAllProducts() {return null;}
    public Result showAllAvailableProducts() {return null;}
    public Result purchaseProduct(Matcher matcher) {return null;}
    public void cheatAddToShopStock(Matcher matcher) {}
    public Result sellProduct(Matcher matcher) {return null;}

    public Result showFriendShip() {return null;}
    public Result talk(Matcher matcher) {return null;}
    public Result showTalkHistory(Matcher matcher) {return null;}
    public Result byGiftForNPC(Matcher matcher) {return null;}
    public Result ShowReceiveGiftList() {return null;}
    public Result rateToGift(Matcher matcher) {return null;}
    public Result showGiftHistory(Matcher matcher) {return null;}
    public Result hug(Matcher matcher) {return null;}
    public Result buyFlower(Matcher matcher) {return null;}
    public Result askMarriage(Matcher matcher) {return null;}
    public Result respond(Matcher matcher) {return null;}

    public Result startTrade(Matcher matcher) {return null;}


    public Result meetNPC(Matcher matcher) {return null;}
    public Result giftNPC(Matcher matcher) {return null;}
    public Result showFriendshipNPCList() {return null;}
    public Result finishQuest(Matcher matcher) {return null;}


    // Time checking and handling
    public Result checkTime() {
        Game game = App.getApp().getCurrentGame();
        if (game.shouldGoToNextDay())
            return new Result(true, "The hour is 22!\nAll players should go home... .");
        else return new Result(false, "");
    }

    public String goNextDay() {
        // TODO : complete
        Game game = App.getApp().getCurrentGame();

        return "Next day\n" + game.goToNextDay();
    }
}
