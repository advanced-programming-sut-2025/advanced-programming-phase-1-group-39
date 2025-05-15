import models.*;
import models.Enums.Menu;
import models.map.FarmType;
import models.services.AppDataManager;
import models.services.SaveAppManager;
import models.services.UsersDataManager;
import views.AppView;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        try {
//            ItemManager.loadItems();
////            System.out.println(ItemManager.getItemByName("Carrot Pickle"));
//            Player player1 = new Player("p1");
//            Player player2 = new Player("p2");
//            Player player3 = new Player("p3");
//            Player player4 = new Player("p4");
//            ArrayList<Player> players = new ArrayList<>(List.of(player1, player2, player3, player4));
//            Game game = new Game(player1, player2, player3, player4);
//
//            game.addRandomFarmForPlayer(player1, FarmType.getFarmTypeById(0));
//            game.addRandomFarmForPlayer(player2, FarmType.getFarmTypeById(0));
//            game.addRandomFarmForPlayer(player3, FarmType.getFarmTypeById(1));
//            game.addRandomFarmForPlayer(player4, FarmType.getFarmTypeById(1));
//            game.startGame();
//            App.getApp().addGame(game);
//            App.getApp().setCurrentGame(game);
//            game.setPlayerInTurn(player2);
////            System.out.println(ItemManager.getItemByName("Sugar"));
////            System.out.println(game.getMap().printColorMap(players, null));
//            while (true) {
//                Menu.GAME.checkInput(Input.getNextLine());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        // TODO : delete up
        AppDataManager.loadApp();
        UsersDataManager.loadUsers();
        new AppView().run();
        UsersDataManager.saveUsers(App.getApp().getUsers());
        SaveAppManager.saveApp();
    }
}