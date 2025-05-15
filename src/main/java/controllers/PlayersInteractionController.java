package controllers;

import models.*;
import models.PlayerInteraction.Friendship;
import models.PlayerInteraction.Message;

import java.util.regex.Matcher;

public class PlayersInteractionController {
    static App app = App.getApp();
    static Game game = App.getApp().getCurrentGame();
    static Player currentPlayer = app.getCurrentGame().getPlayerInTurn();


    public static Result ShowFriendshipsList() {
        StringBuilder output = new StringBuilder();
        for (Player otherPlayer : game.getOtherPlayers(currentPlayer.getUsername())) {
            output.append(printFriendship(game.getFriendship(currentPlayer, otherPlayer), otherPlayer.getUsername()));
        }
        output.deleteCharAt(output.length() - 1);
        return new Result(true, output.toString());
    }

    public static Result talk(Matcher matcher) {
        String playerName = matcher.group("username");
        String message = matcher.group("message");
        Player player2 = game.getPlayerByUsername(playerName);

        if (!isPlayerExists(playerName)) {
            return new Result(false, "Player " + playerName + " doesn't exist");
        } else if (!isPlayersNear(currentPlayer.getLocation(), game.getPlayerByUsername(playerName).getLocation())) {
            return new Result(false, "Get closer to the player  " + playerName + " if you want to start a conversation.");
        } else {
            Friendship friendship = game.getFriendship(currentPlayer, player2);
            friendship.addMessage(new Message(message, currentPlayer.getUsername(), playerName));
            if (friendship.isFirstHug()) {
                String output = increaseXP(friendship, 20, playerName);
                return new Result(true, "Your message has been sent to player " + playerName + ".\n" + output);
            }
            return new Result(true, "Your message has been sent to player " + playerName + ".");
        }
    }

    public static Result talkHistory(Matcher matcher) {
        String otherPlayer = matcher.group("username");
        StringBuilder output = new StringBuilder();

        for (Message message : game.getFriendship(currentPlayer, game.getPlayerByUsername(otherPlayer)).getMessages()) {
            output.append(printMessage(message));
        }
        output.deleteCharAt(output.length() - 1);
        return new Result(true, output.toString());
    }

    public static Result hug(Matcher matcher) {
        String otherPlayer = matcher.group("username");

        if (!isPlayerExists(otherPlayer)) {
            return new Result(false, "Player " + otherPlayer + " doesn't exist");
        } else if (!isPlayersNear(currentPlayer.getLocation(), game.getPlayerByUsername(otherPlayer).getLocation())) {
            return new Result(false, "to hug " + otherPlayer + " , you need to be standing right next to them.");
        } else if (game.getFriendship(game.getPlayerByUsername(otherPlayer), currentPlayer).getFriendshipLevel() < 2) {
            return new Result(false, "you need to reach friendship level 2 with " + otherPlayer + " before you can give them a hug!");
        } else {
            if (game.getFriendship(game.getPlayerByUsername(otherPlayer), currentPlayer).isFirstHug()) {
                String output = increaseXP(game.getFriendship(game.getPlayerByUsername(otherPlayer), currentPlayer), 60, otherPlayer);
                return new Result(true, "you gave " + otherPlayer + " a warm hug! your bond feels stronger already.\n" + output);
            }
            return new Result(true, "you gave " + otherPlayer + " a warm hug! your bond feels stronger already.");
        }
    }


    // Auxiliary functions :

    private static boolean isPlayerExists(String playerName) {
        for (Player player : game.getPlayers()) {
            if (player.getUsername().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPlayersNear(Location player1Location, Location player2Location) {

        int player1X = player1Location.x();
        int player1Y = player1Location.y();
        int npc2X = player2Location.x();
        int npc2Y = player2Location.y();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (npc2X == player1X + dx && npc2Y == player1Y + dy) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String increaseXP(Friendship friendship, int xp, String playerName) {
        int currentXP = friendship.getXp();
        if (friendship.getFriendshipLevel() == 0 && currentXP < 100 && currentXP + xp >= 100) {
            friendship.setXp(currentXP + xp);
            return "Congratulations! You've reached Friendship Level 1 with " + playerName + " !" + "XP gained: " + xp;
        } else if (friendship.getFriendshipLevel() == 0 && currentXP < 100 && currentXP + xp < 100) {
            friendship.setXp(currentXP + xp);
            return "Congratulations! Your friendship XP increased by " + xp + " !" + "XP gained: " + xp;
        } else if (friendship.getFriendshipLevel() == 1 && currentXP < 200 && currentXP + xp >= 200) {
            friendship.setXp(currentXP + xp);
            return "Congratulations! You've managed to grow your friendship with Player " + playerName + " and reached Friendship Level 2!" + "XP gained: " + xp;
        } else if (friendship.getFriendshipLevel() == 1 && currentXP < 200 && currentXP + xp < 200) {
            friendship.setXp(currentXP + xp);
            return "Congratulations! Your friendship XP increased by " + xp + " !" + "XP gained: " + xp;
        } else if (friendship.getFriendshipLevel() == 2 && currentXP < 300 && currentXP + xp >= 300) {
            friendship.setXp(299);
            return "You've built a great friendship with " + playerName + " ! To strengthen this bond and reach the next friendship level, consider buying your friend a flower!" + "XP gained: " + xp;
        } else if (friendship.getFriendshipLevel() == 2 && currentXP < 300 && currentXP + xp < 300) {
            friendship.setXp(currentXP + xp);
            return "Congratulations! Your friendship XP increased by " + xp + " !" + "XP gained: " + xp;
        } else if (friendship.getFriendshipLevel() == 3 && currentXP < 400 && currentXP + xp >= 400) {
            friendship.setXp(400);
            return "Your friendship has reached its peak! If you're of opposite genders, marriage is the only path to deepen your bond. Otherwise, you've reached the highest level of friendship possible! (:" + "XP gained: " + xp;
        } else if (friendship.getFriendshipLevel() == 3 && currentXP < 400 && currentXP + xp < 400) {
            friendship.setXp(currentXP + xp);
            return "Congratulations! Your friendship XP increased by " + xp + " !";
        } else {
            if (friendship.isMarried()) {
                return "You have married player " + playerName + " Your friendship level and XP will now remain constant.";
            } else {
                return "You have reached the maximum friendship level with player " + playerName + " . If your genders are different, you may choose to get married. In any case, your friendship XP and level cannot increase any further.";
            }
        }
    }

    public static String decreaseXP(Friendship friendship, int xp, String playerName) {
        int currentXP = friendship.getXp();

        if (friendship.getFriendshipLevel() == 0 && currentXP - xp < 0) {
            friendship.setXp(0);
            return "The connection with " + playerName + " feels a bit distant… Your friendship XP has dropped.";
        } else if (friendship.getFriendshipLevel() == 0) {
            friendship.setXp(currentXP - xp);
            return "The connection with " + playerName + " feels a bit distant… Your friendship XP has dropped.";
        } else if (friendship.getFriendshipLevel() == 1 && currentXP - xp < 100) {
            friendship.setFriendshipLevel(0);
            return "Unfortunately, your bond with " + playerName + " has weakened… You lost some friendship XP, and your friendship level has dropped by 1.";
        } else if (friendship.getFriendshipLevel() == 1 && currentXP - xp >= 100) {
            friendship.setXp(currentXP - xp);
            return "The connection with " + playerName + " feels a bit distant… Your friendship XP has dropped.";
        } else if (friendship.getFriendshipLevel() == 2 && currentXP - xp < 200) {
            friendship.setFriendshipLevel(1);
            return "Unfortunately, your bond with " + playerName + " has weakened… You lost some friendship XP, and your friendship level has dropped by 1.";
        } else if (friendship.getFriendshipLevel() == 2 && currentXP - xp >= 200) {
            friendship.setXp(currentXP - xp);
            return "The connection with " + playerName + " feels a bit distant… Your friendship XP has dropped.";
        } else if (friendship.getFriendshipLevel() == 3 && currentXP - xp < 300) {
            friendship.setXp(currentXP - xp);
            return "Unfortunately, your bond with " + playerName + " has weakened… You lost some friendship XP, and your friendship level has dropped by 1.";
        } else if (friendship.getFriendshipLevel() == 3 && currentXP - xp >= 300) {
            friendship.setXp(currentXP - xp);
            return "The connection with " + playerName + " feels a bit distant… Your friendship XP has dropped.";
        } else {
            return "Since your friendship level with player " + playerName + " is 4, your friendship XP will remain unchanged.";
        }
    }

    public static StringBuilder printFriendship(Friendship friendship, String otherPlayerName) {
        StringBuilder output = new StringBuilder();
        output.append("* ").append(otherPlayerName).append(" :\n");
        output.append("Friendship level: ").append(friendship.getFriendshipLevel()).append("\n");
        output.append("Friendship XP: ").append(friendship.getXp()).append("\n");
        return output;
    }

    public static StringBuilder printMessage(Message message) {
        StringBuilder output = new StringBuilder();
        output.append("Sender : ").append(message.getSender()).append("\n");
        output.append("Receiver : ").append(message.getReceiver()).append("\n");
        output.append("Message : ").append(message.getMessage()).append("\n");
        output.append("---------------\n");
        return output;
    }


}
