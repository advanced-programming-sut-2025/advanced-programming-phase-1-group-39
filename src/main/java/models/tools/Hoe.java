package models.tools;

import models.Enums.WeatherStatus;
import models.Player;
import models.Result;
import models.Skill;
import models.Weather;
import models.map.Tile;
import models.map.TileType;

public class Hoe extends Tool {
    public Hoe() {
        super("hoe", ToolType.BASIC, 5);
    }

    @Override
    public Result useTool(Tile tile, Player player) {
        if (tile.getType() == TileType.SOIL) {
            if (tile.getTree() != null &&
                tile.getPlant() != null &&
                tile.getItemOnTile() != null) {
                return new Result(false, "You can't plow tile that a plant or tree is on it!");
            }
            tile.plow();
            return new Result(true, "");
        } else {
            return new Result(false, "You can't use Hoe on tiles that aren't soil");
        }
    }


    public int getSkillEnergyReduce(Skill skill) {
        return skill.isFarmingLevelMax() ? 1 : 0;
    }

    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy - type.getEnergyReduce() - getSkillEnergyReduce(skill))
                * getWeatherMultiplier(weather));
    }
}
