package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.Game;
import com.StardewValley.models.Item;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Seed extends Item {
    Season[] seasons;

    public Seed(String name, Season[] seasons) {
        super(name);
        this.seasons = seasons;
    }

    public String getName() {
        return name;
    }

    public TextureRegion getTexture() {
        TextureAtlas cropsAtlas = GameAssetManager.getCropsAtlas();
        String pathName = name.replace(" " , "_");

        TextureRegion region = cropsAtlas.findRegion(pathName);
        if (region == null) {
            TextureAtlas treesAtlas = GameAssetManager.getTressAtlas();
            region = treesAtlas.findRegion(pathName);
        }
        return region;
    }
}