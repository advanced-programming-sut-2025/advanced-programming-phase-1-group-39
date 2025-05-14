package models;

import models.NPC.*;
import models.buildings.Building;
import models.map.FarmType;
import models.map.Map;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final int id;
    private ArrayList<Player> players;
    private String loadedPlayerUsername;
    private Player mainPlayer;
    private ArrayList<TradeItem> trades = new ArrayList<>();
    private Map gameMap;
    private ArrayList<Building> buildings = new ArrayList<>();
    ArrayList<NPC> npcs = initializeNPCs();

    private Time time = new Time();

    private Weather todayWeather = new Weather();
    private Weather tomorrowWeather = new Weather();

    private Player currentPlayer;

    // first player should be the mainPlayer of game
    public Game(int gameId, Player one, Player two, Player three, Player four) {
        this.players = new ArrayList<>(List.of(one, two, three, four));
        this.mainPlayer = one;
        this.gameMap = new Map();
        this.id = gameId;
        todayWeather.setWeatherSunny();
    }


    public void startGame() {
        gameMap.loadMap();

        for (Player player : players) {
            resetPlayerLocation(player);
        }
    }

    public int getId() {
        return id;
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
        if (number == -1) {
            return;
        }

        player.setFarmBound(Map.getStartOfFarm(number));
        gameMap.addRandomFarm(farmType, number);
    }
    // weather


    public Weather getTodayWeather() {
        return todayWeather;
    }

    // time
    public void addToHour(int amount) {
        time.addToHour(amount);
        //TODO : Update of map and plants
    }

    public void addToDay(int amount) {
        time.addToDay(amount);
        // TODO : changing weather
        // TODO : update plants
    }

    public void goToNextDay() {
        time.goToNextDay();
        resetFriendship();
        sendGiftToPlayers();
        // TODO : changing weather
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

    public void setLoadedPlayerUsername(String PlayerUsername) {
        this.loadedPlayerUsername = PlayerUsername;
    }

    public String getLoadedPlayerUsername() {
        return loadedPlayerUsername;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    // NPC

    private ArrayList<NPC> initializeNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        npcs.add(new SebastianNPC());
        npcs.add(new AbigailNPC());
        npcs.add(new HarveyNPC());
        npcs.add(new LeahNPC());
        npcs.add(new RobinNPC());
        npcs.add(new Clint());
        npcs.add(new Willy());
        npcs.add(new Marnie());
        npcs.add(new Gus());
        npcs.add(new Morris());
        npcs.add(new Pierre());
        return npcs;
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
                    if ((x = randomZeroOrOne()) == 1 ) {
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


}