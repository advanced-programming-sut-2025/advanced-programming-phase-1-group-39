package models.tools;

import models.Result;
import models.Skill;
import models.Weather;

public class Shear extends Tool {
    public Shear() {
        super("shear", ToolType.BASIC, 4);
    }

    @Override
    public Result useTool() {
        return null;
    }


    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy) * getWeatherMultiplier(weather));
    }
}
