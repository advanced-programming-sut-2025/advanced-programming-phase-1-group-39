import controllers.AppControllers;
import models.*;
import models.Enums.Menu;
import models.Enums.Season;
import models.buildings.GreenHouse;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.TreeManager;
import models.map.FarmType;
import models.map.Map;
import models.map.Tile;
import org.w3c.dom.Node;
import views.AppView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
//    public static void main(String[] args) {
//        (new AppView()).run();
//    }

    public static void main(String[] args) {
        ItemManager.loadItems();

        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        ArrayList<Player> players = new ArrayList<>(List.of(player1, player2, player3, player4));
        Game game = new Game(player1, player2, player3, player4);

        game.addRandomFarmForPlayer(player1, FarmType.getFarmTypeById(0));
        game.addRandomFarmForPlayer(player2, FarmType.getFarmTypeById(0));
        game.addRandomFarmForPlayer(player3, FarmType.getFarmTypeById(1));
        game.addRandomFarmForPlayer(player4, FarmType.getFarmTypeById(1));

        game.startGame();
        System.out.println(game.getMap().printColorMap(players));
        ((GreenHouse)player2.getBuildingByName("greenhouse")).build();
        player2.getBuildingByName("greenhouse").updateMap(game.getMap());
        App.getApp().addGame(game);
        App.getApp().setCurrentGame(game);
        game.setPlayerInTurn(player2);
        System.out.println(game.getMap().printColorMap(players));
//        Menu.GAME.checkInput("print map -l 74,8 -s 39");
        while (true) {
            Menu.GAME.checkInput(Input.getNextLine());
        }
    }
}