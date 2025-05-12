package models.tools;

import models.Result;
import models.Skill;
import models.Weather;
import models.map.AnsiColors;
import models.map.Tile;
import models.map.TileType;

public class WateringCan extends Tool {
    private int tilesWaterNumRemaining;

    public WateringCan() {
        super("watering can", ToolType.BASIC, 5);
    }

    @Override
    public Result useTool(Tile tile) {
        tilesWaterNumRemaining--;
        if (tile.getType() == TileType.SOIL) {
            if (tile.isPlowed() || tile.getPlant() != null)
                tile.setIsWatered();
            return new Result(true, AnsiColors.ANSI_BLUE + "Watered" + AnsiColors.ANSI_RESET);
        } else if (tile.getType() == TileType.WATER) {
            fillWateringCan();
            return new Result(true, "Your watering can filled up!");
        }

        else {
            return new Result(true, "Nothing watered!");
        }
    }

    public void fillWateringCan() {
        tilesWaterNumRemaining = switch (type) {
            case BASIC -> 40;
            case COPPER -> 55;
            case IRON -> 70;
            case GOLD -> 85;
            case IRIDIUM -> 100;
        };
    }
    public boolean haveWater() {
        return tilesWaterNumRemaining > 0;
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
