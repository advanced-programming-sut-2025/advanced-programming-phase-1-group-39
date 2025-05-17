package models;

import controllers.AppControllers;
import models.Enums.Season;
import models.Enums.WeatherStatus;
import models.NPC.*;
import models.Shops.*;
import models.animals.Animal;
import models.PlayerInteraction.Friendship;
import models.PlayerInteraction.Message;
import models.buildings.Building;
import models.map.FarmType;
import models.map.Map;
import models.map.Tile;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.List;

public class Game {
    // TODO : remove currentId from APP
    public static int lastGameId = 101;
    private int id;

    private ArrayList<Player> players;
    private String loadedPlayerUsername;
    private Player mainPlayer;
    private Player playerInTurn;


    private transient Map gameMap;
    private ArrayList<Building> buildings = new ArrayList<>();

    private ArrayList<NPC> npcs = new ArrayList<>();
    private ArrayList<Friendship> friendships = new ArrayList<>();

    private Time time = new Time();

    private Weather todayWeather = new Weather();
    private Weather tomorrowWeather = new Weather();

    // TODO: change place of this
    private int currentGiftNumber = 101;

    // first player should be the mainPlayer of game
    public Game(int gameId, Player one, Player two, Player three, Player four) {
        this.id = lastGameId++;
        this.players = new ArrayList<>(List.of(one, two, three, four));
        this.mainPlayer = one;
        this.playerInTurn = one;
        this.gameMap = new Map();

        todayWeather.setStatus(WeatherStatus.SUNNY);
        tomorrowWeather.setWeatherRandom(Season.SPRING);
    }

    public void startGame() {
        initializeNPCs();
        makeNPCBuildings();

        gameMap.loadMap(getNpcShops());

        for (Player player : players) {
            resetPlayerLocation(player);
        }

        initializeFriendships();
    }

    public int getId() {
        return id;
    }

    // NPC
    public ArrayList<NPC> getNpcs() {
        return npcs;
    }

    public void makeNPCBuildings() {
        ArrayList<Shop> gameShops = new ArrayList<>();
        gameShops.add(new BlackSmithShop("Blacksmith Shop", new Location(100 + Constants.FARM_WIDTH, 50 + Constants.DISABLED_HEIGHT),
                11, 8, "src/main/resources/data/BlackSmithShop.json",
                9, 16,
                getNPC("clint")
        ));
        gameShops.add(new JojaMartShop("JojaMart Shop", new Location(93 + Constants.FARM_WIDTH, 12 + Constants.DISABLED_HEIGHT),
                25, 20, "src/main/resources/data/JojaMartShop.json",
                9, 23,
                getNPC("morris")
        ));
        gameShops.add(new PierresGeneralStore("Pierre's General Store", new Location(27 + Constants.FARM_WIDTH, 22 + Constants.DISABLED_HEIGHT),
                12, 11, "src/main/resources/data/PierresGeneralStore.json",
                9, 17,
                getNPC("pierre")
        ));
        gameShops.add(new CarpentersShop("Carpenter's Shop", new Location(23 + Constants.FARM_WIDTH, 46 + Constants.DISABLED_HEIGHT),
                18, 12, "src/main/resources/data/CarpentersShop.json",
                9, 20,
                getNPC("robin")
        ));
        gameShops.add(new FishingShop("Fishing Shop", new Location(96 + Constants.FARM_WIDTH, 76 + Constants.DISABLED_HEIGHT),
                18, 12, "src/main/resources/data/FishingShop.json",
                9, 17,
                getNPC("willy")
        ));
        gameShops.add(new MarniesRanch("Marnie's Ranch", new Location(22 + Constants.FARM_WIDTH, 76 + Constants.DISABLED_HEIGHT),
                18, 12, "src/main/resources/data/MarniesRanch.json",
                9, 16,
                getNPC("marnie")
        ));
        gameShops.add(new StardropSaloon("The Stardrop Saloon", new Location(64 + Constants.FARM_WIDTH, 46 + Constants.DISABLED_HEIGHT),
                18, 12, "src/main/resources/data/StardropSaloon.json",
                12, 24,
                getNPC("gus")
        ));

        buildings.addAll(gameShops);
    }

    public ArrayList<Shop> getNpcShops() {
        ArrayList<Shop> shops = new ArrayList<>();
        for (Building building : buildings) {
            if (building instanceof Shop) shops.add((Shop) building);
        }

        return shops;
    }

    private void initializeNPCs() {
        npcs.add(new SebastianNPC());
        npcs.add(new AbigailNPC());
        npcs.add(new HarveyNPC());
        npcs.add(new LeahNPC());
        npcs.add(new RobinNPC());

        npcs.add(new Clint());
        npcs.add(new Gus());
        npcs.add(new Marnie());
        npcs.add(new Morris());
        npcs.add(new Pierre());
        npcs.add(new Willy());
    }

    public NPC getNPC(String NPCName) {
        for (NPC npc : npcs) {
            if (npc.getName().equalsIgnoreCase(NPCName)) {
                return npc;
            }
        }
        return null;
    }

    private void resetFriendship() {
        for (Player player : players) {
            for (PlayerNPCInteraction friendship : player.getAllFriendships()) {
                friendship.setFirstGift(true);
                friendship.setFirstTalking(true);
            }
        }
    }

    // Map
    public Map getMap() {
        return gameMap;
    }


    public void addRandomFarmForPlayer(Player player, FarmType farmType) {
        int number = players.indexOf(player);
        if (number == -1) {
            return;
        }

        player.setFarmBound(Map.getStartOfFarm(number));
        player.addFirstBuildingObjects(this);
        gameMap.addRandomFarm(farmType, number, player);
    }

    // time
    public void addToHour(int amount) {
        time.addToHour(amount);
    }

    public void goToNextHour() {
        addToHour(1);
        //TODO : food buffs

        // reset players energiesHourLimit
        resetPlayersHourlyEnergyLimit();
    }

    public void resetPlayersHourlyEnergyLimit() {
        for (Player player : players) {
            player.resetHourlyEnergyLimit();
        }
    }

    public void addToDay(int amount) {
        for (int i = 0; i < amount; i++)
            goToNextDay();
    }

    public boolean shouldGoToNextDay() {
        if (time.getHour() >= 22) return true;
        return false;
    }

    public String goToNextDay() {
        StringBuilder lastDayReport = new StringBuilder("Last day Report");
//         TODO (OPTIONAL) : complete report

        playersGoHome();
        // reset players energy
        resetPlayersEnergies();

        // adding money
        addPlayersRevenueToMoney();

        // grow plants and trees
        gameMap.growWateredPlantsAndTrees();

        // random foragings + materials and minerals and stone
        fillFarmsWithRandoms();

        // change time
        time.goToNextDay();

        // change weather random
        todayWeather.setStatus(tomorrowWeather.getStatus());
        tomorrowWeather.setWeatherRandom(time.getSeason());

        if (todayWeather.getStatus().equals(WeatherStatus.STORM))
            thorTiles();

        // if weather rain or storm should water all tiles
        if (todayWeather.getStatus().equals(WeatherStatus.STORM)
                || todayWeather.getStatus().equals(WeatherStatus.RAIN)) {
            gameMap.setWaterAllTiles(true);
        } else {
            gameMap.setWaterAllTiles(false);
        }

        // animals
        generateAllAnimalsProduct();

        // friendships
        resetFriendshipNPCs();
        sendGiftToPlayers();
        hasNoInteraction();
        resetFriendships();

        return lastDayReport.toString();
    }

    private void playersGoHome() {
        for (Player player : players) {
            Location homeLoc = player.getHomeLocation();
            Result res = AppControllers.gameController.walkToCheck(homeLoc.x(), homeLoc.y());
            if (res.success())
                AppControllers.gameController.walkTo();
            else
                player.setEnergy(0);
        }
    }

    private void thorTiles() {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Player player : players) {
            for (int i = 0; i < 3; i++) {
                int tileX = player.getStartOfFarm().x() + (int) (Math.random() * Constants.FARM_WIDTH);
                int tileY = player.getStartOfFarm().y() + (int) (Math.random() * Constants.FARM_HEIGHT);

                Tile tile = gameMap.getTile(tileX, tileY);
                if (tiles.contains(tile)) {
                    i--;
                    continue;
                }

                todayWeather.thor(tile);
                tiles.add(tile);
            }
        }
    }

    private void fillFarmsWithRandoms() {
        for (Player player : players) {
            boolean haveTrees = Math.random() > 0.8;
            gameMap.fillFarmWithRandoms(player.getStartOfFarm().x(), player.getStartOfFarm().y(),
                    0.01, 0.05, time.getSeason(), haveTrees,
                    buildings);
        }
    }

    private void resetPlayersEnergies() {
        for (Player player : players) {
            if (!player.isConscious()) player.setEnergy(0.75 * Constants.MAX_ENERGY);
            else if (player.haveBadDay()) {
                player.setEnergy(0.5 * Constants.MAX_ENERGY);
                player.changeBadDays(-1);
            } else player.setEnergy(Constants.MAX_ENERGY);

            player.resetEnergyUnlimited();
            player.resetCheatedEnergy();
        }
    }

    public void addPlayersRevenueToMoney() {
        for (Player player : players) {
            player.addNightRevenueToMoney();
        }
    }

    public void generateAllAnimalsProduct() {
        for (Player player : players) {
            for (Animal animal : player.getAnimals()) {
                animal.endDay();
                animal.generateProductForNextDay();
            }
        }
    }


    public Time getTime() {
        return time;
    }

    // player
    public void resetPlayerLocation(Player player) {
        player.setLocationRelative(74, 8);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getMoneyOfPlayer(Player player) {
        if (!players.contains(player)) return 0;
        //TODO : complete
        if (player.getSpouseName() != null) {
            int money = player.getMoney() + ;
        }
        return player.getMoney();
    }

    //turn
    public Player getPlayerInTurn() {
        return playerInTurn;
    }

    public void setPlayerInTurn(Player playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    public boolean areAllNotConscious() {
        boolean areAllNotConscious = true;
        for (Player player : players) {
            if (player.isConscious()) {
                areAllNotConscious = false;
                break;
            }
        }
        return areAllNotConscious;
    }

    public boolean nextTurn() {
        if (areAllNotConscious()) return false;

        int index = players.indexOf(playerInTurn);
        int newIndex = index + 1;
        if (newIndex == players.size()) {
            newIndex = 0;
            // adding to hour
            goToNextHour();
        }

        playerInTurn = players.get(newIndex);
        if (!playerInTurn.isConscious()) return nextTurn();

        // TODO : show message!!!
//        showMessages(playerInTurn);

        return true;
    }

    // weather
    public Weather getTodayWeather() {
        return todayWeather;
    }

    public Weather getTomorrowWeather() {
        return tomorrowWeather;
    }

    public void setTomorrowWeather(WeatherStatus status) {
        tomorrowWeather.setStatus(status);
    }

    public void setTomorrowWeatherRandom() {
        tomorrowWeather.setWeatherRandom(this.time.getSeason());
    }

    public void setLoadedPlayerUsername(String PlayerUsername) {
        this.loadedPlayerUsername = PlayerUsername;
    }

    public String getLoadedPlayerUsername() {
        return loadedPlayerUsername;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public Shop getShopPlayerIsIn(Player player) {
        for (Shop shop : getNpcShops()) {
            if (gameMap.isInBuilding(shop, player)) return shop;
        }
        return null;
    }

    public Player getPlayerByUsername(String username) {
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    // Friendships
    public ArrayList<Friendship> getFriendships() {
        return friendships;
    }

    private void resetFriendshipNPCs() {
        for (Player player : players) {
            for (PlayerNPCInteraction friendship : player.getAllFriendships()) {
                friendship.setFirstGift(true);
                friendship.setFirstTalking(true);
            }
        }
    }

    public static int randomZeroOrOne() {
        return (int) (Math.random() * 2);
    }

    public static int randomZeroToThree() {
        return (int) (Math.random() * 4);
    }

    private void sendGiftToPlayers() {
        int x;
        int y;
        for (Player player : players) {
            for (PlayerNPCInteraction friendship : player.getAllFriendships()) {
                if (friendship.getFriendshipLevel() == 3) {
                    x = randomZeroOrOne();
                    if (x == 1) {
                        y = randomZeroToThree();
                        ItemStack itemStack = getNPC(friendship.getNPCName()).getGifts().get(y);
                        if (player.getInventory().hasSpace(itemStack)) {
                            player.getInventory().addItem(itemStack.getItem(), itemStack.getAmount());
                        }
                    }
                }
            }

        }
    }

    // interactions

    private ArrayList<Friendship> initializeFriendships() {
        ArrayList<Friendship> friendships = new ArrayList<>();
        friendships.add(new Friendship(players.get(0).getUsername(), players.get(1).getUsername()));
        friendships.add(new Friendship(players.get(0).getUsername(), players.get(2).getUsername()));
        friendships.add(new Friendship(players.get(0).getUsername(), players.get(3).getUsername()));
        friendships.add(new Friendship(players.get(1).getUsername(), players.get(2).getUsername()));
        friendships.add(new Friendship(players.get(1).getUsername(), players.get(3).getUsername()));
        friendships.add(new Friendship(players.get(2).getUsername(), players.get(3).getUsername()));
        return friendships;
    }

    public Friendship getFriendship(Player player1, Player player2) {
        for (Friendship friendship : getFriendships()) {
            if ((friendship.getUser1().equals(player1.getUsername()) && friendship.getUser2().equals(player2.getUsername())) ||
                    (friendship.getUser1().equals(player2.getUsername()) && friendship.getUser2().equals(player1.getUsername()))) {
                return friendship;
            }
        }
        return null;
    }

    public ArrayList<Player> getOtherPlayers(String playerName) {
        ArrayList<Player> otherPlayers = new ArrayList<>();
        for (Player player : players) {
            if (!player.getUsername().equals(playerName)) {
                otherPlayers.add(player);
            }
        }
        return otherPlayers;
    }

    private String showMessages(Player player) {
        StringBuilder messages = new StringBuilder();
        ArrayList<Player> otherPlayers = getOtherPlayers(player.getUsername());
        for (Player otherPlayer : otherPlayers) {
            int count = 0;
            messages.append("Messages received from player ").append(otherPlayer.getUsername()).append(" :\n");
            for (Message message : getFriendship(player, otherPlayer).getMessages()) {
                if (message.getSender().equals(otherPlayer.getUsername()) && message.isNew()) {
                    count++;
                    messages.append("\t").append(message.getMessage()).append("\n");
                    message.setNew(false);
                }
            }
            if (count == 0) {
                messages.append("\tYou have no messages from player ").append(otherPlayer.getUsername()).append(" .\n");
            }
        }
        messages.deleteCharAt(messages.length() - 1);
        return messages.toString();
    }

    private void resetFriendships() {
        for (Friendship friendship : friendships) {
            friendship.setFirstHug(true);
            friendship.setFirstTalking(true);
            friendship.setHasGiftedEachOther(true);
        }
    }

    private void hasNoInteraction() {
        for (Friendship friendship : getFriendships()) {
            if (friendship.isFirstTalking() && friendship.isFirstHug() && friendship.isHasGiftedEachOther() && !friendship.isMarried()) {
                int currentXP = friendship.getXp();
                if (currentXP - 10 < 0) {
                    friendship.setXp(0);
                } else if (currentXP < 100) {
                    friendship.setXp(currentXP - 10);
                } else if (currentXP > 100 && currentXP - 10 < 100) {
                    friendship.setXp(90);
                } else if (currentXP < 200 && currentXP - 10 >= 100) {
                    friendship.setXp(currentXP - 10);
                } else if (currentXP > 200 && currentXP - 10 < 200) {
                    friendship.setXp(190);
                } else if (currentXP < 300 && currentXP - 10 >= 200) {
                    friendship.setXp(currentXP - 10);
                } else if (currentXP > 300 && currentXP - 10 < 300) {
                    friendship.setXp(290);
                } else if (currentXP < 400 && currentXP - 10 >= 300) {
                    friendship.setXp(currentXP - 10);
                } else {
                    friendship.setXp(currentXP);
                }
            }
        }
    }

    // gift
    public void setCurrentGiftNumber() {
        this.currentGiftNumber += 1;
    }

}