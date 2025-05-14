package models.NPC;

import models.Game;
import models.ItemManager;
import models.Location;

import java.util.ArrayList;
import java.util.List;

public class Clint extends NPC {
    public Clint() {
        super(
                "clint",
                "   ",
                "     ",
                new Location(1,1),
                new ArrayList<>(List.of(ItemManager.getItemByName("aaaa"),
                        ItemManager.getItemByName("aaaa")))

        );
    }

    @Override
    public void getRewardMission1(int friendship, Game game) {}

    @Override
    public void getRewardMission2(int friendship, Game game) {}

    @Override
    public void getRewardMission3(int friendship, Game game) {}
}




//public RobinNPC() {
//    super(
//            "robin",
//            "Carpenter",
//            "Robin is a hardworking and pragmatic carpenter with a deep connection to the land and her craft.\nShe enjoys simple, home-cooked meals like spaghetti and finds fulfillment in her work around the farm.\nThough she’s always focused on her job, she has a warm and supportive personality, eager to help those in need.",
//            new Location(25,43),
//            new ArrayList<>(List.of(ItemManager.getItemByName("spaghetti"), ItemManager.getItemByName("Wood"),
//                    ItemManager.getItemByName("Iron Bar")))
//    );
//}