package models.tools;

import models.Player;
import models.Result;
import models.Skill;
import models.Weather;
import models.cropsAndFarming.Plant;
import models.map.Tile;
import models.map.TileType;

public class Scythe extends Tool {
    public Scythe() {
        super("scythe", ToolType.BASIC, 2);
    }

    @Override
    public Result useTool(Tile tile, Player player) {
        if (tile.getType() == TileType.SOIL) {
            Plant plant = tile.getPlant();
            if (plant != null) {
                if (plant.hasCrop())
                    return new Result(true, "crop");
                else
                    return new Result(false, "Nothing to harvest.");
            }
            if (tile.getItemOnTile().getItem().getName().equals("grass")) {
                return new Result(true, "grass");
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
