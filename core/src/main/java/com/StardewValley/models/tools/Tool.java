package com.StardewValley.models.tools;


import com.StardewValley.models.*;
import com.StardewValley.models.map.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Tool extends Item {
    protected ToolType type;
    protected int baseUsingEnergy;

    public Tool(String name, ToolType type, int baseUsingEnergy) {
        super(name);
        this.type = type;
        this.baseUsingEnergy = baseUsingEnergy;
    }

    public abstract Result useTool(Tile tile, Player player, Skill skill);

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

    public TextureRegion getTexture() {
        return new TextureRegion(new Texture(Gdx.files.internal("tools/" + getName() + "/" + type.getPrefixForTexture() + getName() + ".png")));
    }

    public String getName() {
        String[] nameSplit = this.getClass().getName().split("\\.");
        String name = nameSplit[nameSplit.length - 1];
        if (name.equals("WateringCan"))
            return "Watering_Can";
        if (name.equals("MilkPail"))
            return "Milk_Pail";
        return name;
    }
}