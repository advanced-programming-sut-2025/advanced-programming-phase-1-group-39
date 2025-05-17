package controllers;

import models.*;
import models.Enums.Menu;
import models.map.AnsiColors;
import models.map.FarmType;
import models.map.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class GameMenuController {

    public Result startNewGame(Matcher matcher) {
        App app = App.getApp();
        String username1 = matcher.group("username1");
        String username2 = matcher.group("username2");
        String username3 = matcher.group("username3");
        String otherUsers = matcher.group("otherUsers");

        if (username1 == null || username2 == null || username3 == null) {
            return new Result(false, "you must enter the usernames of three users.");
        } else if (!isUsernameExist(username1)) {
            return new Result(false, "username1 not found in the system.");
        } else if (!isUsernameExist(username2)) {
            return new Result(false, "username2 not found in the system.");
        } else if (!isUsernameExist(username3)) {
            return new Result(false, "username3 not found in the system.");
        } else if (otherUsers != null) {
            return new Result(false, "You can only select three players to start the game.");
        } else if (hasCurrentGame(app.getLoggedInUser().getUserName())) {
            return new Result(false, "You are already in a game and you cannot start a new game.");
        } else if (hasCurrentGame(username1)) {
            return new Result(false, "username1 already has a current game.");
        } else if (hasCurrentGame(username2)) {
            return new Result(false, "username2 already has a current game.");
        } else if (hasCurrentGame(username3)) {
            return new Result(false, "username3 already has a current game.");
        } else {
            Player player1 = new Player(app.getLoggedInUser().getUserName(), Game.lastGameId);
            Player player2 = new Player(username1, Game.lastGameId);
            Player player3 = new Player(username2, Game.lastGameId);
            Player player4 = new Player(username3, Game.lastGameId);
            ArrayList<Player> players = new ArrayList<>(List.of(player1, player2, player3, player4));
            ArrayList<User> users = app.getUsers();
            for (Player player : players) {
                users.get(getIndexInUsers(player.getUsername())).addPlayer(player);
                users.get(getIndexInUsers(player.getUsername())).addNumberOfGamesPlayed();
            }
            Game newGame = new Game(app.getLastGameId() + 1, player1, player2, player3, player4);
            app.setLastGameId(app.getLastGameId() + 1);
            app.setCurrentGame(newGame);
            app.addGame(newGame);
            app.getCurrentGame().setPlayerInTurn(player1);
            for (Player player : players) {
                users.get(getIndexInUsers(player.getUsername())).setCurrentGame(newGame);
            }
            return new Result(true, "Congratulations!!!" + "\n" +
                    "The new game has been successfully created. " + "\n" +
                    "Now, each player must choose their game map in order, starting with the first player. \n" +
                    "The available map types are as follows: \n" + newGame.getMap().showFarmTypesInfo());
        }
    }

    public Result chooseMap(Matcher matcher) {
        String mapNumber = matcher.group("mapNumber");
        App app = App.getApp();
        if (app.getCurrentGame() == null) {
            return new Result(false, "To choose a map, you must first start a new game!");
        } else if (!isInteger(mapNumber)) {
            return new Result(false, "The map number must be a number.");
        } else if (Integer.parseInt(mapNumber) >= 3) {
            return new Result(false, "The map number must be either 1 or 2.");
        } else {
            Game currentGame = app.getCurrentGame();
            if (currentGame.getPlayers().indexOf(currentGame.getPlayerInTurn()) == 3) {
                currentGame.addRandomFarmForPlayer(currentGame.getPlayerInTurn(),
                        FarmType.getFarmTypeById(Integer.parseInt(mapNumber)));
                currentGame.startGame();
                app.setCurrentMenu(Menu.GAME);
                currentGame.setPlayerInTurn(currentGame.getPlayers().get(0));
                StringBuilder text = new StringBuilder();
                int counter = 1;
                for (Player player : currentGame.getPlayers()) {
                    String farmPlace = switch (counter) {
                        case 1 -> "Top right";
                        case 2 -> "Top left";
                        case 3 -> "Bottom left";
                        default -> "Bottom right";
                    };
                    text.append("Player "+counter++ +" : " + player.getUsername() + " - Farm: "+farmPlace+"\n");
                }
                return new Result(true, "The game map has been successfully created\nlet the adventure begin!\n" +
                        text.toString() + AnsiColors.ANSI_CYAN_BOLD + "Starting turn : " + currentGame.getPlayerInTurn().getUsername() + AnsiColors.ANSI_RESET
                );
            } else {
                currentGame.addRandomFarmForPlayer(currentGame.getPlayerInTurn(),
                        FarmType.getFarmTypeById(Integer.parseInt(mapNumber)));
                int index = currentGame.getPlayers().indexOf(currentGame.getPlayerInTurn();
                currentGame.setPlayerInTurn(currentGame.getPlayers().get(index + 1));
                return new Result(true, "the game map was successfully selected. " + currentGame.getPlayerInTurn().getUsername() + "! choose your map please.");
            }
        }
    }

    public Result loadGame() {
        App app = App.getApp();
        User user = app.getLoggedInUser();
        if (user.getCurrentGame() == null) {
            return new Result(false, "You don't have any game. please create a game first.");
        } else if (user.getCurrentGame() != null && haveOtherPlayersCurrentGame(user.getCurrentGame().getPlayers())) {
            return new Result(false, "to start loading a game, none of the players must have another active game.");
        } else {
            app.setCurrentGame(user.getCurrentGame());
            Game game = app.getCurrentGame();
            game.setPlayerInTurn(getPlayerFromPlayers(app.getCurrentGame().getPlayers(),
                    user.getUserName()));
            app.getCurrentGame().setMainPlayer(game.getPlayerByUsername(user.getUserName()));
            app.getGames().set(getIndexInGames(game.getId()), app.getCurrentGame());
            app.getCurrentGame().startGame();
            app.setCurrentMenu(Menu.GAME);
            return new Result(true, "the game was loaded successfully. you can now continue your game.");
        }
    }

    public Result goMainMenu() {
        App.getApp().setCurrentMenu(Menu.MAIN_MENU);
        return new Result(true, "you are now in main menu.");
    }

    public Result showCurrentMenu() {
        return new Result(true, "you are now in game menu.");
    }



    // Auxiliary functions :

    private boolean isUsernameExist(String username) {
        App app = App.getApp();
        for (User user : app.getUsers()) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCurrentGame(String username) {
        App app = App.getApp();
        for (User user : app.getUsers()) {
            if (user.getUserName().equals(username)) {
                return user.getCurrentGame() != null;
            }
        }
        return false;
    }

    private boolean isInteger(String input) {
        if (input == null) return false;
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getIndexInGames(int mapId) {
        App app = App.getApp();
        for (int i = 0; i < app.getGames().size(); i++) {
            if (app.getGames().get(i).getId() == mapId) {
                return i;
            }
        }
        return -1;
    }

    private Player getPlayerFromPlayers(ArrayList<Player> players, String playerUsername) {
        for (Player player : players) {
            if (player.getUsername().equals(playerUsername)) {
                return player;
            }
        }
        return null;
    }

    private int getIndexInUsers(String username) {
        App app = App.getApp();
        for (int i = 0; i < app.getUsers().size(); i++) {
            if (app.getUsers().get(i).getUserName().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    private boolean haveOtherPlayersAnotherCurrentGame(ArrayList<Player> players, int id) {
        App app = App.getApp();
        for (Player player : players) {
            if (app.getUsers().get(getIndexInUsers(player.getUsername())).getCurrentGame() != null &&
            app.getUsers().get(getIndexInUsers(player.getUsername())).getCurrentGame().getId() != id) {
                return true;
            }
        }
        return false;
    }
}
