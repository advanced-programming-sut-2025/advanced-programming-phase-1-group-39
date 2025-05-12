package models.tools;

import models.*;
import models.map.Tile;
import models.map.TileType;

public class Pickaxe extends Tool {
    public Pickaxe() {
        super("pickaxe", ToolType.BASIC, 5);
    }

    public boolean canGetItemInQuarry(Item item) {
        if (item.getName().equals("stone")) return true;
        if (item.getName().equals("Copper")) return true;

        if (item.getName().equals("Iron") &&
                type.equals(ToolType.BASIC)) return false;
        if (item.getName().equals("Gold") &&
        (type.equals(ToolType.BASIC) || type.equals(ToolType.COPPER))) return false;
        if (item.getName().equals("Iriduim") &&
        (type.equals(ToolType.BASIC) || type.equals(ToolType.COPPER) || type.equals(ToolType.IRON)))
            return false;

        return true;
    }


    @Override
    public Result useTool(Tile tile) {
        ItemStack item;
        switch (tile.getType()) {
            case QUARRY:
                if ((item = tile.getItemOnTile()) == null) {
                    return new Result(false, "There is nothing!");
                } else {
                    if (!canGetItemInQuarry(item.getItem()))
                        return new Result(false, "Your pickaxe is not strong enough!");

                    return new Result(true, "You broke the Mineral!");
                }

            case SOIL:
                if (tile.getPlant() != null) {
                    tile.removePlant();
                    return new Result(true, "You destroyed a plant!");
                } else if (tile.getItemOnTile() != null) {
                    return new Result(true, "You got the item on tile");
                }

                if (!tile.canPlant()) {
                    return new Result(false, "The Soil wasn't plowed!");
                } else {
                    tile.setNotPlow();
                    return new Result(true, "");
                }
            case INDOOR:
                if (tile.getItemOnTile() == null)
                    return new Result(false, "There is nothing!");
                else
                    return new Result(true, "You got the item on tile");
            default:
                return new Result(false, "You can't use Pickaxe on this tile!");
        }
    }

    public int getSkillEnergyReduce(Skill skill) {
        return skill.isMiningLevelMax() ? 1 : 0;
    }

    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy - type.getEnergyReduce() - getSkillEnergyReduce(skill))
                * getWeatherMultiplier(weather));
    }
}
