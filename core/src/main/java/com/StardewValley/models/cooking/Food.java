package com.StardewValley.models.cooking;

import com.StardewValley.models.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Food extends Item {
    int energy;
    int sellPrice;
    FoodBuff buff;

    public Food(String name, int energy, int sellPrice,  FoodBuff buff) {
        super(name);
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.buff = buff;
    }

    @Override
    public TextureRegion getTexture() {
        return new TextureRegion(new Texture(Gdx.files.internal("animalProducts/" + name.replaceAll(" ", "_") + ".png")));
    }

    public String getName() {
        return name;
    }

    public int getEnergy() {
        return energy;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public FoodBuff getBuff() {
        return buff;
    }
}