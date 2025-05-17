package models.tools;

import models.*;
import models.cropsAndFarming.Plant;
import models.cropsAndFarming.Tree;
import models.map.Tile;
import models.map.TileType;

public class Scythe extends Tool {
    public Scythe() {
        super("scythe", ToolType.BASIC, 2);
    }

    @Override
    public Result useTool(Tile tile, Player player, Skill skill) {
        if (tile.getType() == TileType.SOIL) {
            Plant plant = tile.getPlant();
            if (plant != null) {
                if (!plant.isAlive()) {
                    tile.removePlant();
                    return new Result(true, "You remove the died plants");
                }
                if (plant.hasCrop()) {
                    ItemStack product = plant.harvest();
                    if (!player.getInventory().hasSpace(product))
                        return new Result(false, "You don't have enough space to get objects!");
                    skill.addToFarmingXP(5);
                    player.getInventory().addItem(product.getItem(), product.getAmount());
                    return new Result(true, "You have gotten products! (Farming XP + 5)");
                }
                else
                    return new Result(false, "Nothing to harvest.");
            }
            if (tile.getItemOnTile().getItem().getName().equals("Grass")) {
                ItemStack item = tile.getItemOnTile();
                if (!player.getInventory().hasSpace(item))
                    return new Result(false, "You don't have enough space to get objects!");
                player.getInventory().addItem(item.getItem(), item.getAmount());
                return new Result(true, "You cut the grasses!");
            }
            if (tile.getTree() != null) {
                Tree tree = tile.getTree();
                ItemStack fruits = tree.harvest();
                if (!player.getInventory().hasSpace(fruits))
                    return new Result(false, "You don't have enough space to get objects!");
                player.getInventory().addItem(fruits.getItem(), fruits.getAmount());
                return new Result(true, "You got fruit: " + fruits.getItem().getName());
            }
        }
        return new Result(false, "ِYou did nothing!");
    }


    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy)
                * getWeatherMultiplier(weather));
    }
}
