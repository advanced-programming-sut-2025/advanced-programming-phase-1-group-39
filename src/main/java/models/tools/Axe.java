package models.tools;

import models.Item;
import models.Result;
import models.Skill;
import models.Weather;
import models.map.Tile;
import models.map.TileType;

public class Axe extends Tool {
    public Axe() {
        super("axe", ToolType.BASIC, 5);
    }

    @Override
    public Result useTool(Tile tile) {
        if (tile.getType() == TileType.SOIL) {
            Item item = tile.getItemOnTile().getItem();
            if (item.getName().equals("wood")) {
                return new Result(true, "");
            } else if (tile.getTree() != null) {
                return new Result(true, "");
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
