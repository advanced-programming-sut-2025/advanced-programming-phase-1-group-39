package models.tools;

import models.Player;
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
    public Result useTool(Tile tile, Player player) {
        if (tilesWaterNumRemaining <= 0)
            return new Result(false, "You don't have water!\nYou should fill your Watering Can by water");
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

    public int getMaxWaterSize() {
        return switch (type) {
            case BASIC -> 40;
            case COPPER -> 55;
            case IRON -> 70;
            case GOLD -> 85;
            case IRIDIUM -> 100;
        };
    }

    public void fillWateringCan() {
        tilesWaterNumRemaining = getMaxWaterSize();
    }
    public boolean haveWater() {
        return tilesWaterNumRemaining > 0;
    }

    public int getHowmuchWater() {
        return tilesWaterNumRemaining;
    }

    public int getSkillEnergyReduce(Skill skill) {
        return skill.isFarmingLevelMax() ? 1 : 0;
    }

    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy - type.getEnergyReduce() - getSkillEnergyReduce(skill))
                * getWeatherMultiplier(weather));
    }

    public void upgradeToNext() {}

    public void upgradeTo(ToolType type) {
        this.type = type;
    }
}
