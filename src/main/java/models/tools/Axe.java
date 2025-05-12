package models.tools;

import models.Result;
import models.Skill;
import models.Weather;

public class Axe extends Tool {
    public Axe() {
        super("axe", ToolType.BASIC, 5);
    }

    @Override
    public Result useTool() {
        return null;
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
