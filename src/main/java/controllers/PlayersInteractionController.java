package controllers;

import models.*;
import models.PlayerInteraction.Friendship;
import models.PlayerInteraction.Message;

import java.util.regex.Matcher;

public class PlayersInteractionController {
    App app = App.getApp();
    Game game = App.getApp().getCurrentGame();
    Player currentPlayer = app.getCurrentGame().getPlayerInTurn();


    public Result talk(Matcher matcher) {
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
            String output = addXP(friendship, 20, playerName);
            return new Result(true, "Your message has been sent to player " + playerName + ".\n" + output);
        }
    }


    // Auxiliary functions :

    private  boolean isPlayerExists(String playerName) {
        for (Player player : game.getPlayers()) {
            if (player.getUsername().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayersNear(Location player1Location, Location player2Location) {

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

    private static String addXP(Friendship friendship, int xp, String playerName) {
        int currentXP = friendship.getXp();
        if (friendship.getFriendshipLevel() == 0 && currentXP < 100 && currentXP + xp >= 100) {
            friendship.setFriendshipLevel(1);
            friendship.setXp(currentXP + xp);
            return "Congratulations! You've reached Friendship Level 1 with " + playerName + " !";
        } else if (friendship.getFriendshipLevel() == 0 && currentXP < 100 && currentXP + xp < 100) {
            friendship.setXp(currentXP + xp);
            return "Congratulations! Your friendship XP increased by " + xp + " !";
        } else if (friendship.getFriendshipLevel() == 1 && currentXP < 200 && currentXP + xp >= 200) {
            friendship.setFriendshipLevel(2);
            friendship.setXp(currentXP + xp);
            return "Congratulations! You've managed to grow your friendship with Player " + playerName + " and reached Friendship Level 2!";
        } else if (friendship.getFriendshipLevel() == 1 && currentXP < 200 && currentXP + xp < 200) {
            friendship.setXp(currentXP + xp);
            return "Congratulations! Your friendship XP increased by " + xp + " !";
        } else if (friendship.getFriendshipLevel() == 2 && currentXP < 300 && currentXP + xp >= 300) {
            friendship.setXp(299);
            return "You've built a great friendship with " + playerName + " ! To strengthen this bond and reach the next friendship level, consider buying your friend a flower!";
        } else if (friendship.getFriendshipLevel() == 2 && currentXP < 300 && currentXP + xp < 300) {
            friendship.setXp(currentXP + xp);
            return "Congratulations! Your friendship XP increased by " + xp + " !";
        } else if (friendship.getFriendshipLevel() == 3 && currentXP < 400 && currentXP + xp >= 400) {
            friendship.setXp(399);
            return "Your friendship has reached its peak! If you're of opposite genders, marriage is the only path to deepen your bond. Otherwise, you've reached the highest level of friendship possible! (:";
        } else {
            friendship.setXp(currentXP + xp);
            return "Congratulations! Your friendship XP increased by " + xp + " !";
        }
    }

}
