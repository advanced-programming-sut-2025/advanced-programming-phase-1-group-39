package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.cropsAndFarming.Crop;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FarmingProduct extends Crop {
    boolean canBecomeGiant;

    public FarmingProduct(String name, int baseSellPrice, boolean isEdible, int baseEnergy,
                          int baseHealth, Season[] seasons, boolean canBecomeGiant) {
        super(name);
        this.baseSellPrice = baseSellPrice;
        this.canBeEaten = isEdible;
        this.baseEnergy = baseEnergy;
        this.baseHealth = baseHealth;
        this.seasons = seasons;
        this.canBecomeGiant = canBecomeGiant;
    }

    @Override
    public TextureRegion getTexture() {
        TextureAtlas cropsAtlas = GameAssetManager.getCropsAtlas();
        String pathName = name.replaceAll(" " , "_");

        TextureRegion product = cropsAtlas.findRegion(pathName);
        if (product == null) {
            TextureAtlas fruits = GameAssetManager.getTressAtlas();
            product = new TextureRegion(fruits.findRegion(pathName));
        }

        return product;
    }
}