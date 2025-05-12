package models.tools;

import models.Item;
import models.Result;
import models.Skill;
import models.Weather;
import models.map.Tile;

public abstract class Tool extends Item {
    protected String name;
    protected ToolType type;
    protected int baseUsingEnergy;

    public Tool(String name, ToolType type, int baseUsingEnergy) {
        super(name);
        this.type = type;
        this.baseUsingEnergy = baseUsingEnergy;
    }

    public abstract Result useTool(Tile tile);
    public ToolType getType() {
        return type;
    }
    public void upgradeType() {
        this.type = ToolType.getNext(type);
    }

    public double getWeatherMultiplier(Weather weather) {
        return switch (weather.getStatus()) {
            case SUNNY -> 1;
            case RAIN -> 1.5;
            case STORM -> 1.5;
            default -> 2;
        };
    }

    public abstract int getUsingEnergy(Skill skill, Weather weather);
}