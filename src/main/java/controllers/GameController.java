package controllers;

import models.App;
import models.Game;
import models.Item;
import models.Result;
import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.TreeManager;

import java.util.regex.Matcher;

public class GameController {
    public Result goNextTurn() {return null;}
    public Result goToNextDay() {return null;}

    public Result showTime() {return null;}
    public Result showDate() {return null;}
    public Result showDateTime() {return null;}
    public Result showDayOfWeek() {return null;}
    public void cheatAdvanceTime(Matcher matcher) {}
    public void cheatAdvanceDate(Matcher matcher) {}

    public Result showSeason() {return null;}

    public Result cheatThor(Matcher matcher) {return null;}
    public Result showWeather() {return null;}
    public Result forecastWeather() {return null;}
    public Result cheatWeather() {return null;}
    public void buildGreenHouse() {}

    public Result walkTo(Matcher matcher) {return null;}

    public Result printMap(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        int size = Integer.parseInt(matcher.group("size"));

        Game game = App.getApp().getNowGame();
        return new Result(true, game.getMap().printCharMapBySize(x, y, size));
    }
    public Result helpReadingMap() {
        Game game = App.getApp().getNowGame();
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
