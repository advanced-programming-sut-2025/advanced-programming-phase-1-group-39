import controllers.AppControllers;
import models.*;
import models.Enums.Season;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.TreeManager;
import models.map.FarmType;
import models.map.Map;
import models.map.Tile;
import views.AppView;

public class Main {
//    public static void main(String[] args) {
//        (new AppView()).run();
//    }

    public static void main(String[] args) {

//        Map map = new Map();
//        ItemManager.loadItems();
//
//        map.addRandomFarm(FarmType.LAKE_FARM, 2);
//        map.addRandomFarm(FarmType.MINE_FARM, 1);
//
//        map.loadMap();
//        System.out.println(map.printColorMap());
        ItemManager.loadItems();

        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Game game = new Game(player1, player2, player3, player4);
        game.addRandomFarmForPlayer(player1, FarmType.getFarmTypeById(0));
        game.addRandomFarmForPlayer(player2, FarmType.getFarmTypeById(0));
        game.addRandomFarmForPlayer(player3, FarmType.getFarmTypeById(1));
        game.addRandomFarmForPlayer(player4, FarmType.getFarmTypeById(1));
        game.startGame();
        System.out.println(game.printColorMap());
//        System.out.println(game.getMap().printMapBySize(70, 10, 25, game.getPlayers()));

        game.getMap().findWalkingEnergy(player2.getLocation(), new Location(76, 8) ,player1);
    }
}