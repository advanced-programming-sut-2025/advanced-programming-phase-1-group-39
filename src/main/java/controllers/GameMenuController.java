package controllers;

import models.Item;
import models.cropsAndFarming.CropManager;
import models.map.Map;
import models.Result;

import java.util.regex.Matcher;

public class GameMenuController {
    public static Result startNewGame(Matcher matcher) {return null;}
    private static Map createRandomMap() {return null;}
    public static Result chooseMap() {return null;}
    public static Result loadGame() {return null;}
    public static Result saveGame() {return null;}
    public static Result exitGame() {return null;}
    public static Result deleteGame() {return null;}
    public static Result goNextTurn() {return null;}
    public static Result goToNextDay() {return null;}

    public static Result showTime() {return null;}
    public static Result showDate() {return null;}
    public static Result showDateTime() {return null;}
    public static Result showDayOfWeek() {return null;}
    public static void cheatAdvanceTime(Matcher matcher) {}
    public static void cheatAdvanceDate(Matcher matcher) {}

    public static Result showSeason() {return null;}

    public static Result cheatThor(Matcher matcher) {return null;}
    public static Result showWeather() {return null;}
    public static Result forecastWeather() {return null;}
    public static Result cheatWeather() {return null;}
    public static void buildGreenHouse() {}

    public static Result walkTo(Matcher matcher) {return null;}
    public static Result printMap(Matcher matcher) {return null;}
    public static Result helpReadingMap() {return null;}


    public static Result showEnergy() {return null;}
    public static Result cheatSetEnergy(Matcher matcher) {return null;}
    public static Result cheatEnergyUnlimited(Matcher matcher) {return null;}

    public static Result showInventory(Matcher matcher) {return null;}
    public static Result throwToInventoryTrash(Matcher matcher) {return null;}

    public static Result equipTool(Matcher matcher) {return null;}
    public static Result showCurrentTool() {return null;}
    public static Result showAvailableTools() {return null;}
    public static Result upgradeTool(Matcher matcher) {return null;}
    public static Result useTool(Matcher matcher) {return null;}

    public static Result showCraftInfo(Matcher matcher) {
        if (matcher.matches()) {
            return new Result(true, CropManager.getCropInfo(matcher.group(1)));
        } else {
            return new Result(false, "invalid command");
        }
    }

    public static Result plant(Matcher matcher) {return null;}
    public static Result showPlant(Matcher matcher) {return null;}
    public static Result fertilize(Matcher matcher) {return null;}
    public static Result howMuchWater() {return null;}

    public static Result showCraftingRecipes() {return null;}
    public static Result Craft(Matcher matcher) {return null;}
    public static Result placeItem(Matcher matcher) {return null;}
    public static Result cheatAddToInventory(Matcher matcher) {return null;}
    public static Result manageRefrigerator(Matcher matcher) {return null;}
    private static Result putInRefrigerator(Item item, int amount) {return null;}
    private static Result pickFromRefrigerator(Item item, int amount) {return null;}
    public static Result showCookingRecipes() {return null;}
    public static Result cook(Matcher matcher) {return null;}

    public static Result eatFood(Matcher matcher) {return null;}

    public static Result build(Matcher matcher) {return null;}
    public static Result buyAnimal(Matcher matcher) {return null;}
    public static Result petAnimal(Matcher matcher) {return null;}
    public static Result showAnimalsInfo(Matcher matcher) {return null;}
    public static Result shepherdAnimals(Matcher matcher) {return null;}
    public static Result feedHayAnimal (Matcher matcher) {return null;}
    public static Result showAnimalsProducts(Matcher matcher) {return null;}
    public static Result collectProducts(Matcher matcher) {return null;}
    public static Result sellAnimal(Matcher matcher) {return null;}

    public static Result fishing(Matcher matcher) {return null;}

    public static Result artisanUse(Matcher matcher) {return null;}
    public static Result artisanGet(Matcher matcher) {return null;}

    public static Result showAllProducts() {return null;}
    public static Result showAllAvailableProducts() {return null;}
    public static Result purchaseProduct(Matcher matcher) {return null;}
    public static void cheatAddToShopStock(Matcher matcher) {}
    public static Result sellProduct(Matcher matcher) {return null;}

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
