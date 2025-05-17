package controllers;

import models.*;
import models.Enums.commands.GameMenuCommands;
import models.services.AppDataManager;
import models.services.HashSHA256;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameMenuTest {
    private GameMenuController controller = new GameMenuController();
    Matcher matcher;
    String input;
    String result;

    @BeforeEach
    void setup() {
        controller = new GameMenuController();
    }

    @Mock
    App appMock = App.getApp();

    // testing new game :

    @Test
    void correctNewGame() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.addUser(new User("mohsen", "paSS12#$", "mohi", "mohsen@gmail.com", true));
        appMock.addUser(new User("reza", "paSS12#$", "reza", "reza@gmail.com", true ));
        appMock.addUser(new User("ahmad", "paSS12#$", "ahmad", "ahmad@gmail.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        Player player1 = new Player(appMock.getLoggedInUser().getUserName(), Game.lastGameId);
        Player player2 = new Player("mohsen", Game.lastGameId);
        Player player3 = new Player("reza", Game.lastGameId);
        Player player4 = new Player("ahmad", Game.lastGameId);
        Game newGame = new Game(appMock.getLastGameId() + 1, player1, player2, player3, player4);
        input = "game new -u mohsen reza ahmad";
        matcher = GameMenuCommands.NewGame.getMatcher(input);
        result = "Congratulations!!!" + "\n" +
                "The new game has been successfully created. " + "\n" +
                "Now, each player must choose their game map in order, starting with the first player. \n" +
                "The available map types are as follows: \n" + newGame.getMap().showFarmTypesInfo();
        assertEquals(result, controller.startNewGame(matcher).message());
    }

    @Test
    // Without any players :
    void invalidNewGame() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.addUser(new User("mohsen", "paSS12#$", "mohi", "mohsen@gmail.com", true));
        appMock.addUser(new User("reza", "paSS12#$", "reza", "reza@gmail.com", true ));
        appMock.addUser(new User("ahmad", "paSS12#$", "ahmad", "ahmad@gmail.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        Player player1 = new Player(appMock.getLoggedInUser().getUserName(), Game.lastGameId);
        Player player2 = new Player("mohsen", Game.lastGameId);
        Player player3 = new Player("reza", Game.lastGameId);
        Player player4 = new Player("ahmad", Game.lastGameId);
        Game newGame = new Game(appMock.getLastGameId() + 1, player1, player2, player3, player4);
        input = "game new -u ";
        matcher = GameMenuCommands.NewGame.getMatcher(input);
        result = "you must enter the usernames of three users.";
        assertEquals(result, controller.startNewGame(matcher).message());
    }

    @Test
    // More than 3 players :
    void invalidNewGame2() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.addUser(new User("mohsen", "paSS12#$", "mohi", "mohsen@gmail.com", true));
        appMock.addUser(new User("reza", "paSS12#$", "reza", "reza@gmail.com", true ));
        appMock.addUser(new User("ahmad", "paSS12#$", "ahmad", "ahmad@gmail.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        Player player1 = new Player(appMock.getLoggedInUser().getUserName(), Game.lastGameId);
        Player player2 = new Player("mohsen", Game.lastGameId);
        Player player3 = new Player("reza", Game.lastGameId);
        Player player4 = new Player("ahmad", Game.lastGameId);
        String input = "game new -u mohsen reza ahmad nima";
        matcher = GameMenuCommands.NewGame.getMatcher(input);
        result = "You can only select three players to start the game.";
        assertEquals(result, controller.startNewGame(matcher).message());
    }

    @Test
    // Invalid username :
    void invalidNewGame3() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.addUser(new User("mohsen", "paSS12#$", "mohi", "mohsen@gmail.com", true));
        appMock.addUser(new User("reza", "paSS12#$", "reza", "reza@gmail.com", true ));
        appMock.addUser(new User("ahmad", "paSS12#$", "ahmad", "ahmad@gmail.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        Player player1 = new Player(appMock.getLoggedInUser().getUserName(), Game.lastGameId);
        Player player2 = new Player("mohsen", Game.lastGameId);
        Player player3 = new Player("reza", Game.lastGameId);
        Player player4 = new Player("ahmad", Game.lastGameId);
        String input = "game new -u reza inv@lid mohsen";
        matcher = GameMenuCommands.NewGame.getMatcher(input);
        result = "username2 not found in the system.";
        assertEquals(result, controller.startNewGame(matcher).message());
    }

    @Test
    // Invalid username :
    void invalidNewGame4() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.addUser(new User("mohsen", "paSS12#$", "mohi", "mohsen@gmail.com", true));
        appMock.addUser(new User("reza", "paSS12#$", "reza", "reza@gmail.com", true ));
        appMock.addUser(new User("ahmad", "paSS12#$", "ahmad", "ahmad@gmail.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        Player player1 = new Player(appMock.getLoggedInUser().getUserName(), Game.lastGameId);
        Player player2 = new Player("mohsen", Game.lastGameId);
        Player player3 = new Player("reza", Game.lastGameId);
        Player player4 = new Player("ahmad", Game.lastGameId);
        String input = "game new -u reza mohsen gholi";
        matcher = GameMenuCommands.NewGame.getMatcher(input);
        result = "username3 not found in the system.";
        assertEquals(result, controller.startNewGame(matcher).message());
    }

    @Test
    // The player is a member of another game :
    void invalidNewGame5() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.addUser(new User("mohsen", "paSS12#$", "mohi", "mohsen@gmail.com", true));
        appMock.addUser(new User("reza", "paSS12#$", "reza", "reza@gmail.com", true ));
        appMock.addUser(new User("ahmad", "paSS12#$", "ahmad", "ahmad@gmail.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        Player player1 = new Player(appMock.getLoggedInUser().getUserName(), Game.lastGameId);
        Player player2 = new Player("mohsen", Game.lastGameId);
        Player player3 = new Player("reza", Game.lastGameId);
        Player player4 = new Player("ahmad", Game.lastGameId);
        Game newGame = new Game(appMock.getLastGameId() + 1, player1, player2, player3, player4);
        appMock.getLoggedInUser().setCurrentGame(newGame);
        input = "game new -u mohsen reza ahmad";
        matcher = GameMenuCommands.NewGame.getMatcher(input);
        result = "You are already in a game and you cannot start a new game.";
        assertEquals(result, controller.startNewGame(matcher).message());
    }

    // testing chose map :

    @Test
    void correctChoseMap() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.addUser(new User("mohsen", "paSS12#$", "mohi", "mohsen@gmail.com", true));
        appMock.addUser(new User("reza", "paSS12#$", "reza", "reza@gmail.com", true ));
        appMock.addUser(new User("ahmad", "paSS12#$", "ahmad", "ahmad@gmail.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        Player player1 = new Player(appMock.getLoggedInUser().getUserName(), Game.lastGameId);
        Player player2 = new Player("mohsen", Game.lastGameId);
        Player player3 = new Player("reza", Game.lastGameId);
        Player player4 = new Player("ahmad", Game.lastGameId);
        Game newGame = new Game(appMock.getLastGameId() + 1, player1, player2, player3, player4);
        appMock.getLoggedInUser().setCurrentGame(newGame);
        appMock.setCurrentGame(newGame);
        input = "game map 2";
        matcher = GameMenuCommands.ChooseMap.getMatcher(input);
        result = "the game map was successfully selected. mohsen! choose your map please.";
        assertEquals(result, controller.chooseMap(matcher).message());
    }

    @Test
    // Invalid map number :
    void invalidChosenMap() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.addUser(new User("mohsen", "paSS12#$", "mohi", "mohsen@gmail.com", true));
        appMock.addUser(new User("reza", "paSS12#$", "reza", "reza@gmail.com", true ));
        appMock.addUser(new User("ahmad", "paSS12#$", "ahmad", "ahmad@gmail.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        Player player1 = new Player(appMock.getLoggedInUser().getUserName(), Game.lastGameId);
        Player player2 = new Player("mohsen", Game.lastGameId);
        Player player3 = new Player("reza", Game.lastGameId);
        Player player4 = new Player("ahmad", Game.lastGameId);
        Game newGame = new Game(appMock.getLastGameId() + 1, player1, player2, player3, player4);
        appMock.getLoggedInUser().setCurrentGame(newGame);
        appMock.setCurrentGame(newGame);
        input = "game map 8";
        matcher = GameMenuCommands.ChooseMap.getMatcher(input);
        result = "The map number must be either 1 or 2.";
        assertEquals(result, controller.chooseMap(matcher).message());
    }

    @Test
    // Invalid map number :
    void invalidChosenMap2() {
        appMock.addUser(new User("mostafa", HashSHA256.hashPassword("paSS12#$"), "mosi", "mostafa@gamil.com", true));
        appMock.addUser(new User("mohsen", "paSS12#$", "mohi", "mohsen@gmail.com", true));
        appMock.addUser(new User("reza", "paSS12#$", "reza", "reza@gmail.com", true ));
        appMock.addUser(new User("ahmad", "paSS12#$", "ahmad", "ahmad@gmail.com", true));
        appMock.setLoggedInUser(appMock.getUsers().get(0));
        Player player1 = new Player(appMock.getLoggedInUser().getUserName(), Game.lastGameId);
        Player player2 = new Player("mohsen", Game.lastGameId);
        Player player3 = new Player("reza", Game.lastGameId);
        Player player4 = new Player("ahmad", Game.lastGameId);
        Game newGame = new Game(appMock.getLastGameId() + 1, player1, player2, player3, player4);
        appMock.getLoggedInUser().setCurrentGame(newGame);
        appMock.setCurrentGame(newGame);
        input = "game map eight";
        matcher = GameMenuCommands.ChooseMap.getMatcher(input);
        result = "The map number must be a number.";
        assertEquals(result, controller.chooseMap(matcher).message());
    }
}
