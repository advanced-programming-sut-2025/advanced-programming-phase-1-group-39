package models.tools;

import models.*;
import models.cropsAndFarming.Tree;
import models.map.Tile;
import models.map.TileType;

public class Axe extends Tool {
    public Axe() {
        super("axe", ToolType.BASIC, 5);
    }

    @Override
    public Result useTool(Tile tile, Player player, Skill skill) {
        if (tile.getType() == TileType.SOIL) {
            ItemStack itemStack = tile.getItemOnTile();
            if (itemStack == null && tile.getTree() == null) {
                return new Result(false, "Did nothing!");
            }
            Item item;
            if (itemStack != null && (item = itemStack.getItem()).getName().equals("Wood")) {
                if (!player.getInventory().hasSpace(itemStack))
                    return new Result(false, "You don't have enough space to get wood!");
                else{
                    player.getInventory().addItem(item, itemStack.getAmount());
                    tile.removeItemOnTile();
                    return new Result(true, "You got wood!");
                }
            }
            else if (tile.getTree() != null) {
                Tree tree = tile.getTree();
                ItemStack treeProducts = tree.cutDown();
                if (!player.getInventory().hasSpace(treeProducts))
                    return new Result(false, "You don't have enough space to get tree products!");
                else{
                    ItemStack woods = new ItemStack(ItemManager.getItemByName("Wood"), (tree.getCurrentStage() * 5 + 5));
                    player.getInventory().addItem(treeProducts.getItem(), treeProducts.getAmount());
                    tile.removeTree();
                    if (!player.getInventory().hasSpace(woods)) {
                        tile.placeItem(woods);
                        return new Result(true, "You don't have enough space to get woods!");
                    }
                    player.getInventory().addItem(woods.getItem(), woods.getAmount());
                    return new Result(true, "You got wood and tree seeds!");
                }
            } else
                return new Result(false, "There is no wood");
        } else {
            return new Result(false, "Did nothing!");
        }
    }


    public int getSkillEnergyReduce(Skill skill) {
        return skill.isForagingLevelMax() ? 1 : 0;
    }

    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy - type.getEnergyReduce() - getSkillEnergyReduce(skill))
                * getWeatherMultiplier(weather));
    }
}
