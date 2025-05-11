package models.tools;

import models.Result;

public class Axe extends Tool {
    public Axe() {
        super("axe", ToolType.BASIC, 5);
    }

    @Override
    public Result useTool() {
        return null;
    }


    public int getSkillEnergyReduce() {
        return 0;
        // TODO : complete
    }

    @Override
    public int getUsingEnergy() {
        return baseUsingEnergy - type.getEnergyReduce() - getSkillEnergyReduce();
    }
}
