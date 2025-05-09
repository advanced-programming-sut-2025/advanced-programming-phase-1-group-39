package controllers;

import models.*;
import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.TreeManager;
import models.map.AnsiColors;
import models.map.MapMinPathFinder;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class GameController {
    public ArrayList<MapMinPathFinder.Node> path;

    public Result saveGame() {return null;}
    public Result exitGame() {return null;}
    public Result deleteGame() {return null;}

    public Result goNextTurn() {return null;}

    public int showTime() {
        int h = App.getApp().getCurrentGame().getTime().getHour();
        return h;
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

    public Result cheatThor(Matcher matcher) {return null;}
    public Result showWeather() {return null;}
    public Result forecastWeather() {return null;}
    public Result cheatWeather() {return null;}
    public void buildGreenHouse() {}

    public Result walkToCheck(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
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
                + "\nEnergy needed: " + energy
                + "\nYour energy: " + player.getEnergy());
    }
    public Result walkTo() {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        int energyCost = path.size()/Constants.EACH_TILE_ENERGY_COST;
        Location end = path.get(path.size() - 1).location;
        if (player.getEnergy() < energyCost) {
            int numOfTilesCanGo = (int)(player.getEnergy() * Constants.EACH_TILE_ENERGY_COST);
            end = path.get(numOfTilesCanGo).location;
        }
        player.setLocationAbsolut(end.x(), end.y());
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


    public Result showEnergy() {return null;}
    public Result cheatSetEnergy(Matcher matcher) {return null;}
    public Result cheatEnergyUnlimited(Matcher matcher) {return null;}

    public Result showInventory(Matcher matcher) {return null;}
    public Result throwToInventoryTrash(Matcher matcher) {return null;}

    public Result equipTool(Matcher matcher) {return null;}
    public Result showCurrentTool() {return null;}
    public Result showAvailableTools() {return null;}
    public Result upgradeTool(Matcher matcher) {return null;}
    public Result useTool(Matcher matcher) {return null;}

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
}
