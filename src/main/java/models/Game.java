package models;

import controllers.AppControllers;
import models.Enums.Season;
import models.Enums.WeatherStatus;
import models.NPC.*;
import models.Shops.*;
import models.animals.Animal;
import models.buildings.Building;
import models.map.FarmType;
import models.map.Map;
import models.map.Tile;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private ArrayList<Player> players;
    private Player mainPlayer;

    private Player playerInTurn;

    private ArrayList<TradeItem> trades = new ArrayList<>();
    private Map gameMap;
    private ArrayList<Building> buildings = new ArrayList<>();

    private Time time = new Time();

    private Weather todayWeather = new Weather();
    private Weather tomorrowWeather = new Weather();

    ArrayList<NPC> npcs;


    // first player should be the mainPlayer of game
    public Game(Player one, Player two, Player three, Player four) {
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
    }

    // NPC
    public void makeNPCBuildings() {
//        ArrayList<Shop> gameShops = new ArrayList<>();
//        gameShops.add(new BlackSmithShop("Blacksmith Shop",
//                9, 16, new Location(100, 50), 11, 8,
//                getNPC("clint")
//                ));
//        gameShops.add(new JojaMartShop("JojaMart Shop",
//                9, 23, new Location(93,12), 25, 20,
//                getNPC("morris")
//                ));
//        gameShops.add(new PierresGeneralStore("Pierre's General Store",
//                9, 17, new Location(27, 22), 12, 11,
//                getNPC("pierre")
//                ));
//        gameShops.add(new CarpentersShop("Carpenter's Shop",
//                9, 20, new Location(23,46), 18, 12,
//                getNPC("robin")
//                ));
//        gameShops.add(new FishingShop("Fishing Shop",
//                9, 17, new Location(96, 76), 18, 12,
//                getNPC("willy")
//                ));
//        gameShops.add(new MarniesRanch("Marnie's Ranch",
//                9, 16, new Location(22, 76), 18, 12,
//                getNPC("Marnie")
//                ));
//        gameShops.add(new StardropSaloon("The Stardrop Saloon",
//                12, 24, new Location(64, 46), 18, 12,
//                getNPC("pierre")
//                ));

//        buildings.addAll(gameShops);
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

        // TODO : add shop npcs
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

    public String printColorMap() {
        return gameMap.printColorMap(players);
    }

    public void addRandomFarmForPlayer(Player player, FarmType farmType) {
        int number = players.indexOf(player);
        if (number == -1) {return;}

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
        // TODO (OPTIONAL) : complete report

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
                int tileX = player.getStartOfFarm().x() + (int)(Math.random() * Constants.FARM_WIDTH);
                int tileY = player.getStartOfFarm().y() + (int)(Math.random() * Constants.FARM_HEIGHT);

                Tile tile = gameMap.getTile(tileX, tileY);
                if (tiles.contains(tile)){
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
            gameMap.fillFarmWithRandoms(player.getStartOfFarm().x(), player.getStartOfFarm().y(),
                    0.01, 0.05, time.getSeason(), false);
        }
    }
    private void resetPlayersEnergies() {
        for (Player player : players) {
            if (!player.isConscious()) player.setEnergy(0.75 * Constants.MAX_ENERGY);
            else if (player.haveBadDay()) {
                player.setEnergy(0.5 * Constants.MAX_ENERGY);
                player.changeBadDays(-1);
            }
            else player.setEnergy(Constants.MAX_ENERGY);

            player.resetEnergyUnlimited();
            player.resetCheatedEnergy();
        }
    } public void addPlayersRevenueToMoney() {
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
    // TODO: each day should be called
    public void setTomorrowWeatherRandom() {
        tomorrowWeather.setWeatherRandom(this.time.getSeason());
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }
}
