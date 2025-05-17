package controllers;

import models.*;
import models.Enums.Direction;
import models.Enums.Menu;
import models.Enums.WeatherStatus;
import models.animals.Fish;
import models.buildings.ShippingBin;
import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.TreeManager;
import models.inventory.Inventory;
import models.inventory.TrashType;
import models.map.AnsiColors;
import models.map.MapMinPathFinder;
import models.map.Tile;
import models.tools.*;

import models.Shops.Shop;
import models.animals.Animal;
import models.animals.AnimalProduct;
import models.artisan.ArtisanGood;
import models.artisan.ArtisanMachine;
import models.cooking.FoodManager;
import models.crafting.CraftingManager;
import models.cropsAndFarming.*;
import models.tools.FishingPole;
import models.trading.*;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class GameController {
    public ArrayList<MapMinPathFinder.Node> path;

    public Result saveGame() {return null;}

    public String exitGame() {
        if (!App.getApp().getCurrentGame().getMainPlayer().getUsername().equals(App.getApp().getCurrentGame().getPlayerInTurn().getUsername())) {
            return "Only main player is allowed to request to leave the game.";
        } else {
            App.getApp().setCurrentGame(null);
            App.getApp().setCurrentMenu(Menu.MAIN_MENU);
            return "You exited from the game! game saved.\nNow you are in main menu.";
        }
    }

    public void exitApp() {
        App.getApp().setCurrentMenu(Menu.ExitMenu);
    }

    public Result deleteGame() {

        App app = App.getApp();
        Game game = app.getCurrentGame();
        if (!game.getPlayerInTurn().getUsername().equals(game.getMainPlayer().getUsername())) {
            return new Result(false, "You are not allowed to delete the game.");
        } else {
            return new Result(true, "To delete the game, all players must agree. Remaining players need to confirm their approval one by one.");
        }






    }

    public String showCurrentMenu() {
        return "Now you are in game!";
    }

    public Result goNextTurn() {
        Game game = App.getApp().getCurrentGame();

        if (!game.nextTurn()) {
            return new Result(false, "All players are not conscious!\n"
                    + AnsiColors.ANSI_LIGHT_GREEN_BOLD + "Going next day!\n" + AnsiColors.ANSI_RESET
                    + game.goToNextDay());
        }

        Player player = game.getPlayerInTurn();
        return new Result(true, AnsiColors.ANSI_CYAN_BOLD +
                "Next turn: " + player.getUsername() + "\n" +game.showMessages(game.getPlayerInTurn()) + "\n" +
                game.showGiftMessages(game.getPlayerInTurn()) + AnsiColors.ANSI_RESET);
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

        return new Result(true, game.getMap().printMapBySize(x, y, size, game.getPlayers(), game.getNpcs()));
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
        return "now your energy is " + AnsiColors.ANSI_LIGHT_GREEN_BOLD + "UNLIMITED" + AnsiColors.ANSI_RESET;
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

        return new Result(true, "Your tool in hand is: " +
                AnsiColors.ANSI_YELLOW + item.getItem().getName() + AnsiColors.ANSI_RESET);
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
        if (tool instanceof FishingPole)
            return new Result(false, "You can't use Fishing Pole by this command!");

        int energyConsumed = tool.getUsingEnergy(player.getSkills(), game.getTodayWeather());
        if (player.getTurnEnergy() <= energyConsumed) {
            return new Result(false, AnsiColors.ANSI_ORANGE_BOLD + "You don't have enough energy!😓" + AnsiColors.ANSI_RESET);
        }

        Result toolResult = tool.useTool(tile, player);

        if (!toolResult.success()) {
            if (tool instanceof Pickaxe || tool instanceof Axe)
                energyConsumed -= 1;
            player.changeEnergy(-energyConsumed);
            return new Result(true, toolResult +
                    AnsiColors.ANSI_ORANGE_BOLD + "\nConsumed Energy: " + energyConsumed + AnsiColors.ANSI_RESET);
        } else {
            player.changeEnergy(-energyConsumed);
            return new Result(true, toolResult +
                    AnsiColors.ANSI_ORANGE_BOLD + "\nConsumed Energy: " + energyConsumed + AnsiColors.ANSI_RESET);
        }
    }

    public Result showCraftInfo(Matcher matcher) {
        return new Result(true, CropManager.getCropInfo(matcher.group(1)));
    }
    public Result showTreeInfo(Matcher matcher) {
        return new Result(true, TreeManager.getTreeInfo(matcher.group(1)));
    }

    public Result plant(Matcher matcher) {
        String seedName = matcher.group(1);
        String dir = matcher.group(2);
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        Direction direction = Direction.getDirection(dir);
        if (direction == null) {
            return new Result(false, "Wrong direction");
        }
        Tile tile = App.getApp().getCurrentGame().getMap().getTile(player.getLocation().x() + direction.dx,
                player.getLocation().y() + direction.dy);
        if (tile  == null) {
            return new Result(false, "tile doesn't exist");
        }
        if(!tile.canPlant()) {
            return new Result(false, "You can't plant anything on this tile!");
        }
        if (!player.getInventory().hasItem(seedName)) {
            return new Result(false, "You don't have " + seedName);
        }
        tile.plantSeed(seedName);
        return new Result(false, "You have successfully planted " + seedName);
    }
    public Result showPlant(Matcher matcher) {
        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));

        Tile tile = App.getApp().getCurrentGame().getMap().getTile(x, y);
        if (tile  == null) {
            return new Result(false, "tile doesn't exist");
        }
        Plant plant = tile.getPlant();
        if (plant == null) {
            return new Result(false, "There is no plant here");
        }
        return new Result(true, plant.toString());
    }
    public Result showTree(Matcher matcher) {
        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));

        Tile tile = App.getApp().getCurrentGame().getMap().getTile(x, y);
        if (tile  == null) {
            return new Result(false, "tile doesn't exist");
        }
        Tree tree = tile.getTree();
        if (tree == null) {
            return new Result(false, "There is no tree here");
        }
        return new Result(true, tree.toString());
    }
    public Result fertilize(Matcher matcher) {
        String fertilizerName = matcher.group(1);
        String dir = matcher.group(2);

        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        Direction direction = Direction.getDirection(dir);
        if (direction == null) {
            return new Result(false, "Wrong direction");
        }
        Tile tile = App.getApp().getCurrentGame().getMap().getTile(player.getLocation().x() + direction.dx,
                player.getLocation().y() + direction.dy);
        if (tile  == null) {
            return new Result(false, "tile doesn't exist");
        }
        if (!tile.isPlowed()) {
            return new Result(false, "You must plow tile first!");
        }
        if (!player.getInventory().hasItem(fertilizerName)) {
            return new Result(false, "You don't have any " + fertilizerName);
        }
        FertilizerType fertilizerType = FertilizerType.getType(fertilizerName);
        if (fertilizerType == null) {
            return new Result(false, fertilizerName + " isn't a fertilizer!");
        }
        tile.setFertilizer(fertilizerType);
        return new Result(true, fertilizerName + " has set as tile fertilizer");
    }
    public String howMuchWater() {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        ItemStack itemStack = player.getInventory().getItemByName("watering can");
        if (itemStack != null) {
            WateringCan wateringCan = (WateringCan) itemStack.getItem();
            int waterValue = wateringCan.getHowmuchWater();
            if (waterValue == 0)
                return "The Water of your Watering Can : " + AnsiColors.ANSI_RED_BOLD + waterValue +
                        " / " + wateringCan.getMaxWaterSize() + AnsiColors.ANSI_RESET;
            return "The Water of your Watering Can : " + AnsiColors.ANSI_CYAN_BOLD + waterValue +
                    " / " + wateringCan.getMaxWaterSize() + AnsiColors.ANSI_RESET;
        } else {
            return "Watering can not found!";
        }
    }

    public Result showCraftingRecipes() {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        return new Result(true, player.showCraftingRecipes());
    }
    public Result Craft(Matcher matcher) {
        String craftName = matcher.group(1);
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        return CraftingManager.craft(craftName, player);
    }
    public Result placeItem(Matcher matcher) {
        String itemName = matcher.group(1);
        String dir = matcher.group(2);

        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        Inventory inv = player.getInventory();
        if (!inv.hasItem(itemName)) {
            return new Result(false, "You don't have this item in your inventory");
        }
        Direction direction = Direction.getDirection(dir);
        if (direction == null) {
            return new Result(false, "This direction is unavailable");
        }
        Location playerLocation = player.getLocation();
        Tile tile = App.getApp().getCurrentGame().getMap().getTile(playerLocation.x() + direction.dx,
                playerLocation.y() + direction.dy);
        if (tile  == null) {
            return new Result(false, "tile doesn't exist");
        }
        if (!tile.canAddItemToTile()) {
            return new Result(false, "You can't add item to this tile");
        }
        inv.placeItem(itemName, tile);
        return new Result(true, "You have placed this item to tile successfully!");
    }
    public Result cheatAddToInventory(Matcher matcher) {
        String name = matcher.group(1);
        int count = Integer.parseInt(matcher.group(2));

        Inventory inv = App.getApp().getCurrentGame().getPlayerInTurn().getInventory();
        Item itemT0oAdd = ItemManager.getItemByName(name);
        if (!inv.hasSpace(new ItemStack(itemT0oAdd, count))) {
            return new Result(false, "Your inventory has not space anymore");
        }

        inv.addItem(itemT0oAdd, count);
        return new Result(true, "You have successfully add " + name + " " + count + "x to your inventory");
    }
    public Result manageRefrigerator(Matcher matcher) {
        String method = matcher.group(1);
        String itemName = matcher.group(2);
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        // Todo: fridge bayad az khoone biad
        Refrigerator refrigerator = new Refrigerator();
        Inventory inv = player.getInventory();
        Item item = ItemManager.getItemByName(itemName);
        if (method.equalsIgnoreCase("pick")) {
            if (!refrigerator.contains(itemName)) {
                return new Result(false, "You don't have this item");
            }
            if (!inv.hasSpace(new ItemStack(item, 1))) {
                return new Result(false, "Your inventory has not space anymore");
            }
            ItemStack itemToPick = refrigerator.pickItem(itemName, 1);
            inv.addItem(itemToPick.getItem(), itemToPick.getAmount());
            return new Result(true, "You picked up " + itemName + " from refrigerator");
        } else if (method.equalsIgnoreCase("put")) {
            if (!inv.hasItem(itemName)) {
                return new Result(false, "You don't have this item");
            }
            refrigerator.addItem(item, 1);
            return new Result(true, "You put " + itemName + " from your inventory to refrigerator");
        } else {
            return new Result(false, "invalid command");
        }
    }
    public Result showCookingRecipes() {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        return new Result(true, player.showFoodRecipes());
    }
    public Result cook(Matcher matcher) {
        String recipeName = matcher.group(1);

        return FoodManager.cook(recipeName, App.getApp().getCurrentGame().getPlayerInTurn());
    }

    public Result eatFood(Matcher matcher) {
        String foodName = matcher.group(1);
        return FoodManager.eat(foodName, App.getApp().getCurrentGame().getPlayerInTurn());
    }

    // TODO : complete these
    public Result build(Matcher matcher) {return null;}
    public Result buyAnimal(Matcher matcher) {return null;}
    public Result petAnimal(Matcher matcher) {
        String name = matcher.group(1);
        Animal animal = App.getApp().getCurrentGame().getPlayerInTurn().getAnimal(name);

        if (animal == null) {
            return new Result(false, "You don't have this animal ):");
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
            return new Result(false, "You don't have this animal ):");
        }

        animal.changeFriendship(amount);
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
            return new Result(false, "You don't have this animal");
        }
        animal.sendOutside(x, y);

        return new Result(true, "You shepherd your animal, congrats!");
    }
    public Result feedHayAnimal (Matcher matcher) {
        String animalName = matcher.group(1);
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        Inventory inv = player.getInventory();
        if (!inv.hasItem("Hay")) {
            return new Result(false, "You don't have any hay to feed animal");
        }
        Animal animal = player.getAnimal(animalName);
        if (animal == null) {
            return new Result(false, "You don't have this animal");
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
            return new Result(false, "You don't have this animal");
        }

        AnimalProduct product = animal.collectProduct();
        if (!player.getInventory().hasSpace(new ItemStack(product, 1))) {
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
            return new Result(false, "You don't have this animal");
        }

        int money = player.sellAnimal(animal);
        return new Result(true, "You have sold the animal. You have got " + money + "g");
    }

    public Result fishing(Matcher matcher) {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        String pole = matcher.group("pole");
        if (player.getInventory().getItemByName(pole) == null) {
            return new Result(false, "You don't have a fishing pole by this name!");
        }
        FishingPole fishingPole = (FishingPole) player.getInventory().getItemByName(pole).getItem();

        int usingEnergy = fishingPole.getUsingEnergy(player.getSkills(), game.getTodayWeather());
        if (player.getTurnEnergy() <= usingEnergy) {
            return new Result(false, "You don't have enough energy to use fishing pole!");
        }

        if (game.getMap().isNearWater(player)) {
            ArrayList<Fish> caughtFishes = player.goFishing(fishingPole, game.getTodayWeather(), game.getTime().getSeason());
            player.changeEnergy(-usingEnergy);
            if (caughtFishes.size() == 0) {
                return new Result(true, "You didn't got any fish!" + "Energy consumed: " + usingEnergy);
            }
            int num = 0;
            StringBuilder text = new StringBuilder("Fishes:\n");
            System.out.println(caughtFishes);

            for (Fish caughtFish : caughtFishes) {
                ItemStack fishItem = new ItemStack(caughtFish, 1);

                if (!player.getInventory().hasSpace(fishItem)) {
                    if (num == 0)
                        return new Result(true, "You don't have enough space to add fish!");
                    else
                        return new Result(true, "You caught " + num + " fishes!\n" + text);
                }
                num ++;
                text.append(caughtFish.getType().name().toLowerCase() + "\n");
                player.getInventory().addItem(fishItem.getItem(), fishItem.getAmount());
            }
            return new Result(true, "You caught " + num + " fishes!\n" + text);
        } else {
            return new Result(false, "You need to be near water to get Fish!");
        }
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
        if (!App.getApp().getCurrentGame().getPlayerInTurn().getInventory().hasSpace(new ItemStack(good, 1))) {
            return new Result(false, "Your inventory has not space anymore!");
        }
        App.getApp().getCurrentGame().getPlayerInTurn().getInventory().addItem(good, 1);

        return new Result(true, "You have got " + itemName + " successfully");
    }

    public Result showAllProducts() {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        Shop shop = game.getShopPlayerIsIn(player);
        if (shop == null) {
            return new Result(false, "You should be in a shop");
        }
        return new Result(true, shop.showAllProducts());
    }
    public Result showAllAvailableProducts() {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        Shop shop = game.getShopPlayerIsIn(player);
        if (shop == null) {
            return new Result(false, "You should be in a shop");
        }
        return new Result(true, shop.showAvailableProducts());
    }
    public Result purchaseProduct(Matcher matcher) {
        String productName = matcher.group(1);
        int count = Integer.parseInt(matcher.group(2));

        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        Shop shop = game.getShopPlayerIsIn(player);
        if (shop == null) {
            return new Result(false, "You should be in a shop");
        }
        return shop.purchase(productName, count);
    }

    // TODO : complete
    public void cheatAddToShopStock(Matcher matcher) {}

    public Result sellProduct(Matcher matcher) {
        String productName = matcher.group("product");
        int num = Integer.parseInt(matcher.group("count"));


        Game currentGame = App.getApp().getCurrentGame();
        Player player = currentGame.getPlayerInTurn();
        ItemStack item = player.getInventory().getItemByName(productName);

        ShippingBin bin = (ShippingBin) player.getBuildingByName("Shipping Bin");
        if (!currentGame.getMap().isNearBuilding(player, bin)) {
            return new Result(false, "You should be near you shippingBin to sell your products!");
        }

        if (item == null) {
            return new Result(false, "You don't have any of product " + productName);
        }
        else if (item.getAmount() < num) {
            return new Result(false, "You don't have enough product " + productName + "\nStock: " + item.getAmount());
        }
        if (!item.getItem().isSellable()) {
            return new Result(false, "You can't sell this product!");
        }
        player.getInventory().addItem(item.getItem(), -num);
        int price = num * item.getItem().getItemPrice();
        player.addToRevenue(price);
        return new Result(true, "You successfully sold your product " + productName + "!\nPrice: " + price);
    }

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

    public Result startTrade(Matcher matcher) {
        ArrayList<Player> players = App.getApp().getCurrentGame().getPlayers();

        StringBuilder sb = new StringBuilder();
        sb.append("Players in game: \n");
        for (Player player : players) {
            sb.append(player.getUsername()).append("\n");
        }

        return new Result(true, sb.toString());
    }
    public Result trade(Matcher matcher) {
        String targetUsername = matcher.group(1);
        String typeString = matcher.group(2);
        String itemName = matcher.group(3);
        int amount;

        try {
            amount = Integer.parseInt(matcher.group(4));
        } catch (NumberFormatException e) {
            return new Result(false, "Amount must be a valid number.");
        }

        String priceStr = matcher.group("money");
        String targetItem = matcher.group("item");
        String targetAmountStr = matcher.group(7);

        Player currentPlayer = App.getApp().getCurrentGame().getPlayerInTurn();
        Player targetPlayer = App.getApp().getCurrentGame().getPlayerByUsername(targetUsername);
        if (targetPlayer == null) return new Result(false, "Player not found.");


        TradeType type = TradeType.getTypeByString(typeString);
        if (type == null) {
            return new Result(false, "Invalid type");
        }

        Inventory currentInv = currentPlayer.getInventory();
        Inventory targetInv = targetPlayer.getInventory();

        if (type == TradeType.OFFER) {
            if (!currentInv.hasEnoughStack(itemName, amount)) {
                return new Result(false, "You don’t have enough of this item.");
            }
        } else {
            if (!targetInv.hasEnoughStack(itemName, amount)) {
                return new Result(false, "The player doesn’t have enough of this item.");
            }
        }

        boolean isMoneyTrade = priceStr != null;
        boolean isItemTrade = targetItem != null && targetAmountStr != null;

        Trade trade;
        if (isMoneyTrade) {
            int price = Integer.parseInt(priceStr);
            if (price <= 0) return new Result(false, "Price must be positive.");
            trade = new Trade(currentPlayer, targetPlayer, type,
                    new TradeItem(itemName, amount), null, price);
        } else {
            int targetAmount;
            try {
                targetAmount = Integer.parseInt(targetAmountStr);
            } catch (NumberFormatException e) {
                return new Result(false, "Invalid target amount.");
            }

            trade = new Trade(currentPlayer, targetPlayer, type,
                    new TradeItem(itemName, amount),
                    new TradeItem(targetItem, targetAmount), 0);
        }

        TradeManager.addTrade(trade);

        return new Result(true, "Trade request sent to " + targetUsername + " (id: " + trade.getId() + ").");
    }
    public Result ShowTradeList(Matcher matcher) {
        ArrayList<Trade> trades = TradeManager.getTradesForUser(App.getApp().getCurrentGame()
                .getPlayerInTurn().getUsername());
        StringBuilder sb = new StringBuilder();
        sb.append("Your available trades: \n");
        for (Trade trade : trades) {
            sb.append(trade.toString());
        }
        return new Result(true, sb.toString());
    }
    public Result responseToTrade(Matcher matcher) {
        String action = matcher.group(1);
        int tradeId = Integer.parseInt(matcher.group(2));

        Player currentPlayer = App.getApp().getCurrentGame().getPlayerInTurn();
        Trade trade = TradeManager.getTradeById(currentPlayer.getUsername(), tradeId);

        if (trade == null) {
            return new Result(false, "No trade request found for you with this ID.");
        }
        if (trade.getReceiver() != currentPlayer) {
            return new Result(false, "You must be the receiver of the trade");
        }

        if (trade.getStatus() != TradeStatus.PENDING) {
            return new Result(false, "This trade has already been " +
                    trade.getStatus().name().toLowerCase() + ".");
        }

        Player sender = trade.getSender();

        if (action.equals("-reject")) {
            trade.reject();
            //FriendshipManager.decreaseXP(currentPlayer, sender, 30);
            return new Result(true, "You rejected trade request #" + tradeId);
        }

        if (trade.getType() == TradeType.REQUEST) {
            if (!currentPlayer.getInventory().hasEnoughStack(trade.getRequestedItem().getItemName(),
                    trade.getRequestedItem().getAmount())) {
                return new Result(false, "You don’t have enough "
                        + trade.getRequestedItem().getItemName() + " to complete the trade.");
            }
        } else if (trade.getType() == TradeType.OFFER) {
            if (!sender.getInventory().hasEnoughStack(trade.getRequestedItem().getItemName(), trade.getRequestedItem().getAmount())) {
                return new Result(false, "Sender no longer has the required items.");
            }
        }

        if (trade.isMoneyTrade()) {
            int money = trade.getPrice();

            if (trade.getType() == TradeType.REQUEST) {
                if (!sender.hasEnoughMoney(money)) {
                    return new Result(false, "Sender doesn’t have enough money.");
                }
                currentPlayer.getInventory().pickItem(trade.getOfferedItem().getItemName(), trade.getOfferedItem().getAmount());
                sender.getInventory().addItem(ItemManager.getItemByName(trade.getOfferedItem().getItemName()),
                        trade.getOfferedItem().getAmount());

                sender.changeMoney(-money);
                currentPlayer.changeMoney(money);
            } else {
                if (!currentPlayer.hasEnoughMoney(money)) {
                    return new Result(false, "You don’t have enough money.");
                }
                sender.getInventory().pickItem(trade.getOfferedItem().getItemName(), trade.getOfferedItem().getAmount());
                currentPlayer.getInventory().addItem(ItemManager.getItemByName(trade.getOfferedItem().getItemName()),
                        trade.getOfferedItem().getAmount());

                currentPlayer.changeMoney(-money);
                sender.changeMoney(money);
            }
        } else {
            TradeItem targetItem = trade.getRequestedItem() ;

            if (trade.getType() == TradeType.REQUEST) {
                if (!sender.getInventory().hasEnoughStack(targetItem.getItemName(), targetItem.getAmount())) {
                    return new Result(false, "Sender no longer has " + targetItem.getItemName());
                }

                currentPlayer.getInventory().pickItem(trade.getOfferedItem().getItemName(), trade.getOfferedItem().getAmount());
                sender.getInventory().addItem(ItemManager.getItemByName(trade.getOfferedItem().getItemName()),
                        trade.getOfferedItem().getAmount());

                sender.getInventory().pickItem(targetItem.getItemName(), targetItem.getAmount());
                currentPlayer.getInventory().addItem(ItemManager.getItemByName(targetItem.getItemName()),
                        targetItem.getAmount());

            } else {
                if (!currentPlayer.getInventory().hasEnoughStack(targetItem.getItemName(), targetItem.getAmount())) {
                    return new Result(false, "You don’t have enough " + targetItem.getItemName());
                }

                sender.getInventory().pickItem(trade.getRequestedItem().getItemName(), trade.getRequestedItem().getAmount());
                currentPlayer.getInventory().addItem(ItemManager.getItemByName(trade.getRequestedItem().getItemName()),
                        trade.getRequestedItem().getAmount());

                currentPlayer.getInventory().pickItem(targetItem.getItemName(), targetItem.getAmount());
                sender.getInventory().addItem(ItemManager.getItemByName(targetItem.getItemName()), targetItem.getAmount());
            }
        }

        trade.accept();
        //FriendshipManager.increaseXP(currentPlayer, sender, 50);

        return new Result(true, "Trade #" + tradeId + " accepted successfully.");
    }
    public Result showTradeHistory(Matcher matcher) {
        ArrayList<Trade> trades = TradeManager.getTradeHistory(App.getApp().getCurrentGame()
                .getPlayerInTurn().getUsername());
        StringBuilder sb = new StringBuilder();
        sb.append("Your trades history: \n");
        for (Trade trade : trades) {
            sb.append(trade.toString());
        }
        return new Result(true, sb.toString());
    }


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
