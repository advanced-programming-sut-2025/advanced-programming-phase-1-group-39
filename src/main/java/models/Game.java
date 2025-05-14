package models;

import models.NPC.*;
import models.PlayerInteraction.Friendship;
import models.PlayerInteraction.Message;
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

    ArrayList<NPC> NPCs = initializeNPCs();
    ArrayList<Friendship> friendships = initializeFriendships();

    private Time time = new Time();

    private Weather todayWeather = new Weather();
    private Weather tomorrowWeather = new Weather();

    private Player playerInTurn;

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

        showMessages(playerInTurn);

        // TODO : add talk
        return true;
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

    public Player getPlayerInTurn() {
        return playerInTurn;
    }

    public Player getPlayerByUsername(String username) {
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    public void setPlayerInTurn(Player playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    public ArrayList<Friendship> getFriendships() { return friendships; }

    // NPC

    private ArrayList<NPC> initializeNPCs() {
        ArrayList<NPC> NPCs = new ArrayList<>();
        NPCs.add(new SebastianNPC());
        NPCs.add(new AbigailNPC());
        NPCs.add(new HarveyNPC());
        NPCs.add(new LeahNPC());
        NPCs.add(new RobinNPC());
        NPCs.add(new Clint());
        NPCs.add(new Willy());
        NPCs.add(new Marnie());
        NPCs.add(new Gus());
        NPCs.add(new Morris());
        NPCs.add(new Pierre());
        return NPCs;
    }

    public NPC getNPC(String NPCName) {
        for (NPC npc : NPCs) {
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

    private ArrayList<Player> getOtherPlayers(String playerName) {
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
                }
            }
            if (count == 0) {
                messages.append("\tYou have no messages from player ").append(otherPlayer.getUsername()).append(" .\n");
            }
        }
        messages.deleteCharAt(messages.length() - 1);
        return messages.toString();
    }





}