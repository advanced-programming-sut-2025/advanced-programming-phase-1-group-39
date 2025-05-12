package models.tools;

import models.Enums.WeatherStatus;
import models.Result;
import models.Skill;
import models.Weather;

public class Hoe extends Tool {
    public Hoe() {
        super("hoe", ToolType.BASIC, 5);
    }

    @Override
    public Result useTool() {
        return null;
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
