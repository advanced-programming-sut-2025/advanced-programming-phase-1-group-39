package models.tools;

import models.Result;
import models.Skill;
import models.Weather;

public class Scythe extends Tool {
    public Scythe() {
        super("sycthe", ToolType.BASIC, 2);
    }

    @Override
    public Result useTool() {
        return null;
    }


    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy)
                * getWeatherMultiplier(weather));
    }
}
