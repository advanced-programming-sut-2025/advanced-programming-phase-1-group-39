package controllers;

import models.*;
import models.Enums.Direction;
import models.Shops.Shop;
import models.animals.Animal;
import models.animals.AnimalProduct;
import models.animals.Fish;
import models.artisan.ArtisanGood;
import models.artisan.ArtisanMachine;
import models.buildings.Building;
import models.cooking.FoodManager;
import models.crafting.CraftingManager;
import models.cropsAndFarming.*;
import models.inventory.Inventory;
import models.map.Tile;
import models.tools.FishingPole;
import models.trading.*;

import java.awt.*;
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
    public Result howMuchWater() {return null;}

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

    public Result fishing(Matcher matcher) {return null;}

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
            return new Result(false, "You should be in a building");
        }
        return new Result(true, shop.showAllProducts());
    }
    public Result showAllAvailableProducts() {
        Game game = App.getApp().getCurrentGame();
        Player player = game.getPlayerInTurn();

        Shop shop = game.getShopPlayerIsIn(player);
        if (shop == null) {
            return new Result(false, "You should be in a building");
        }
        return new Result(true, shop.showAvailableProducts());
    }
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
        Player targetPlayer = App.getApp().getCurrentGame().getPlayerByName(targetUsername);
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
}
