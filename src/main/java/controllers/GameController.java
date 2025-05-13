package controllers;

import models.*;
import models.animals.Animal;
import models.animals.AnimalProduct;
import models.animals.Fish;
import models.artisan.ArtisanGood;
import models.artisan.ArtisanMachine;
import models.artisan.Furnace;
import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.TreeManager;
import models.inventory.Inventory;
import models.tools.FishingPole;

import java.util.ArrayList;
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
    public Result printMap(Matcher matcher) {return null;}
    public Result helpReadingMap() {return null;}


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

    public Result showCraftingRecipes() {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        return new Result(true, player.showCraftingRecipes());
    }
    public Result Craft(Matcher matcher) {return null;}
    public Result placeItem(Matcher matcher) {return null;}
    public Result cheatAddToInventory(Matcher matcher) {return null;}
    public Result manageRefrigerator(Matcher matcher) {return null;}
    private Result putInRefrigerator(Item item, int amount) {return null;}
    private Result pickFromRefrigerator(Item item, int amount) {return null;}
    public Result showCookingRecipes() {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        return new Result(true, player.showFoodRecipes());
    }
    public Result cook(Matcher matcher) {return null;}

    public Result eatFood(Matcher matcher) {return null;}

    public Result build(Matcher matcher) {return null;}
    public Result buyAnimal(Matcher matcher) {return null;}
    public Result petAnimal(Matcher matcher) {
        String name = matcher.group(1);
        Animal animal = App.getApp().getCurrentGame().getPlayerInTurn().getAnimal(name);

        if (animal == null) {
            return new Result(false, "You doesn't have this animal ):");
        }
        //Todo: check is near animal
        animal.pet();

        return new Result(false, "\"Thank you (:\" said " + name +
                ". Your frienship: " + animal.getFriendship());
    }
    public Result cheatFriendshipAnimal(Matcher matcher) {
        String animalName = matcher.group(1);
        int amount = Integer.parseInt(matcher.group(2));
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        Animal animal = player.getAnimal(animalName);
        if (animal == null) {
            return new Result(false, "You doesn't have this animal ):");
        }

        animal.setFriendship(amount);
        return new Result(true, "You changed friendship with " + animalName + " by amount " + amount);
    }
    public Result showAnimalsInfo(Matcher matcher) {
        ArrayList<Animal> animals = App.getApp().getCurrentGame().getPlayerInTurn().getAnimals();
        StringBuilder sb = new StringBuilder();
        sb.append("Your Animals: \n");
        for (Animal animal : animals) {
            sb.append(animal.toString());
        }

        return new Result(true, sb.toString());
    }
    public Result shepherdAnimals(Matcher matcher) {
        String animalName = matcher.group(1);
        int x = Integer.parseInt(matcher.group(2));
        int y = Integer.parseInt(matcher.group(3));

        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        //Todo: check x and y validate
        Animal animal = player.getAnimal(animalName);
        if (animal == null) {
            return new Result(false, "You doesn't have this animal");
        }
        animal.sendOutside(x, y);

        return new Result(true, "You shepherd your animal, congrats!");
    }
    public Result feedHayAnimal (Matcher matcher) {
        String animalName = matcher.group(1);
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        Inventory inv = player.getInventory();
        if (!inv.hasItem("Hay")) {
            return new Result(false, "You doesn't have any hay to feed animal");
        }
        Animal animal = player.getAnimal(animalName);
        if (animal == null) {
            return new Result(false, "You doesn't have this animal");
        }

        inv.pickItem("Hay", 1);
        animal.feedHay();
        return new Result(true, "You have successfully fed the " + animalName);
    }
    public Result showAnimalsProducts(Matcher matcher) {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        ArrayList<Animal> animals = player.getAnimals();

        StringBuilder sb = new StringBuilder();
        sb.append("Your animals and their products: \n");
        for (Animal animal : animals) {
            sb.append(animal.listProductStatus()).append("\n");
        }
        return new Result(true, sb.toString());
    }
    public Result collectProducts(Matcher matcher) {
        String animalName = matcher.group(1);
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        Animal animal = player.getAnimal(animalName);
        if (animal == null) {
            return new Result(false, "You doesn't have this animal");
        }

        AnimalProduct product = animal.collectProduct();
        if (!player.getInventory().hasSpace()) {
            return new Result(false, "Your inventory has not space anymore!");
        }
        player.getInventory().addItem(product, 1);
        return new Result(true, "You have collected " + product.getName() + " from " + animalName);
    }
    public Result sellAnimal(Matcher matcher) {
        String animalName = matcher.group(1);
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        Animal animal = player.getAnimal(animalName);
        if (animal == null) {
            return new Result(false, "You doesn't have this animal");
        }

        int money = player.sellAnimal(animal);
        return new Result(true, "You have sold the animal. You have got " + money + "g");
    }

    public Result fishing(Matcher matcher) {
        Game currentGame = App.getApp().getCurrentGame();
        String pole = matcher.group(1);
        //Todo: check karadan name pole to inv
        FishingPole fishingPole = null;
        ArrayList<Fish> gotFishes = currentGame.getPlayerInTurn().goFishing(fishingPole, currentGame.getTodayWeather(),
                currentGame.getTime().getSeason());
        if (gotFishes == null) {
            return new Result(false, "You didn't get any fishes ):");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("You have got this fishes:    \n");
        for (Fish fish : gotFishes) {
            sb.append(fish.toString());
        }

        return new Result(true, sb.toString());
    }

    public Result artisanUse(Matcher matcher) {
        String itemName = matcher.group(1);
        String ingredients = matcher.group(2);
        String[] ingredientsParts = ingredients.split(" ");

        ArtisanMachine machine = App.getApp().getCurrentGame().getMap()
                .getNearArtisanMachine(App.getApp().getCurrentGame().getPlayerInTurn(), ItemManager.getArtisanMachineByGood(itemName));
        if (machine == null) {
            return new Result(false, "You aren't near the machine now!");
        }

        Result result = machine.use(itemName, ingredientsParts, App.getApp().getCurrentGame().getTime(),
                App.getApp().getCurrentGame().getPlayerInTurn());

        return result;
    }
    public Result artisanGet(Matcher matcher) {
        String itemName = matcher.group(1);

        ArtisanMachine machine = App.getApp().getCurrentGame().getMap()
                .getNearArtisanMachine(App.getApp().getCurrentGame().getPlayerInTurn(), ItemManager.getArtisanMachineByGood(itemName));
        if (machine == null) {
            return new Result(false, "You aren't near the machine now!");
        }

        ArtisanGood good = machine.getReadyGoods(itemName, App.getApp().getCurrentGame().getTime());
        if (good == null) {
            return new Result(false, "The product has not produced by machine (yet or never)");
        }
        if (!App.getApp().getCurrentGame().getPlayerInTurn().getInventory().hasSpace()) {
            return new Result(false, "Your inventory has not space anymore!");
        }
        App.getApp().getCurrentGame().getPlayerInTurn().getInventory().addItem(good, 1);

        return new Result(true, "You have got " + itemName + " successfully");
    }

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
