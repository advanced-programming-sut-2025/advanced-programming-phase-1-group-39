import controllers.AppControllers;
import models.App;
import models.Enums.Season;
import models.Enums.commands.GameCommands;
import models.ItemManager;
import models.Location;
import models.NPC.NPC;
import models.Player;
import models.Shops.*;
import models.artisan.*;
import models.cooking.Food;
import models.cooking.FoodRecipe;
import models.crafting.CraftingManager;
import models.crafting.CraftingRecipe;
import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.TreeManager;
import models.map.Tile;

import java.io.IOException;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args){
        Shop newShop = new PierresGeneralStore("salam", new Location(0,0), 0, 0, "src/main/resources/data/PierresGeneralStore.json",
                0, 0, new NPC("mmd"));
        //System.out.println(newShop.showAllProducts());
        //System.out.println(newShop.showAvailableProducts());
        ItemManager.loadItems();
        System.out.println(ItemManager.getItemByName("Vinegar").getName());
    }
}