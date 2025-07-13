package com.StardewValley.models.tools;


import com.StardewValley.models.Player;
import com.StardewValley.models.Result;
import com.StardewValley.models.Skill;
import com.StardewValley.models.Weather;
import com.StardewValley.models.map.Tile;

public class FishingPole extends Tool {
    private FishingPoleType poleType;

    public FishingPole(String name, FishingPoleType poleType) {
        super(name, ToolType.BASIC, poleType.getUsingEnergy());
        this.poleType = poleType;
    }

    @Override
    public Result useTool(Tile tile, Player player, Skill skill) {
        return null;
    }

    public int getSkillEnergyReduce(Skill skill) {
        return skill.isFishingLevelMax() ? 1 : 0;
    }

    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy - getSkillEnergyReduce(skill))
                * getWeatherMultiplier(weather));
    }

    public FishingPoleType getPoleType() {
        return poleType;
    }
}