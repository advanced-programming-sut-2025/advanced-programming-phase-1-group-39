package models.tools;

import models.Result;
import models.Skill;
import models.Weather;

public class WateringCan extends Tool {
    private int tilesWaterNumRemaining;

    public WateringCan() {
        super("watering can", ToolType.BASIC, 5);
    }

    @Override
    public Result useTool() {
        return null;
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
