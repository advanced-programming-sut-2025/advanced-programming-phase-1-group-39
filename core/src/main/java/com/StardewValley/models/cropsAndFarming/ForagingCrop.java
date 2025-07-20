package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.cropsAndFarming.Crop;
import com.StardewValley.models.cropsAndFarming.ForagingSource;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;

public class ForagingCrop extends Crop {
    private ForagingSource source;

    public ForagingCrop(String name, int baseSellPrice, int energy, ForagingSource source, Season[] seasons) {
        super(name);
        this.baseSellPrice = baseSellPrice;
        this.canBeEaten = true;
        this.baseEnergy = energy;
        this.source = source;
        this.seasons = seasons;
    }

    public ForagingSource getSource() {
        return source;
    }
    public Season[] getSeasons() {
        return seasons;
    }

    @Override
    public String toString() {
        return "ForagingCrop{" +
                "source=" + source +
                ", name='" + getName() + '\'' +
                ", baseSellPrice=" + baseSellPrice +
                ", canBeEaten=" + canBeEaten +
                ", baseEnergy=" + baseEnergy +
                ", baseHealth=" + baseHealth +
                ", seasons=" + Arrays.toString(seasons) +
                '}';
    }

    @Override
    protected ForagingCrop clone(){
        return new ForagingCrop(this.name, this.baseSellPrice, this.baseEnergy, this.source, this.seasons);
    }

    @Override
    public TextureRegion getTexture() {
        TextureAtlas foragingsAtlas = GameAssetManager.getForagingsAtlas();
        return foragingsAtlas.findRegion(getName().replace(" ", "_"));
    }

}
