package com.StardewValley.models.animals;

import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.animals.Fish;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum FishType {
    SALMON(75, Season.FALL, new TextureRegion(new Texture(Gdx.files.internal("fishes/Salmon.png")))),
    SARDINE(40, Season.FALL,  new TextureRegion(new Texture(Gdx.files.internal("fishes/Sardine.png")))),
    SHAD(60, Season.FALL,  new TextureRegion(new Texture(Gdx.files.internal("fishes/Shad.png")))),
    BLUE_DISCUS(120, Season.FALL, new TextureRegion(new Texture(Gdx.files.internal("fishes/Blue_Discus.png")))),

    MIDNIGHT_CARP(150, Season.WINTER, new TextureRegion(new Texture(Gdx.files.internal("fishes/Midnight_Carp.png")))),
    SQUID(80, Season.WINTER, new TextureRegion(new Texture(Gdx.files.internal("fishes/Squid.png")))),
    TUNA(100, Season.WINTER, new TextureRegion(new Texture(Gdx.files.internal("fishes/Tuna.png")))),
    PERCH(55, Season.WINTER, new TextureRegion(new Texture(Gdx.files.internal("fishes/Perch.png")))),

    FLOUNDER(100, Season.SPRING,  new TextureRegion(new Texture(Gdx.files.internal("fishes/Flounder.png")))),
    LIONFISH(100, Season.SPRING,  new TextureRegion(new Texture(Gdx.files.internal("fishes/Lionfish.png")))),
    HERRING(30, Season.SPRING,  new TextureRegion(new Texture(Gdx.files.internal("fishes/Herring.png")))),
    GHOSTFISH(45, Season.SPRING,  new TextureRegion(new Texture(Gdx.files.internal("fishes/Ghostfish.png")))),

    TILAPIA(75, Season.SUMMER, new TextureRegion(new Texture(Gdx.files.internal("fishes/Tilapia.png")))),
    DORADO(100, Season.SUMMER, new TextureRegion(new Texture(Gdx.files.internal("fishes/Dorado.png")))),
    SUNFISH(30, Season.SUMMER,  new TextureRegion(new Texture(Gdx.files.internal("fishes/Sunfish.png")))),
    RAINBOW_TROUT(65, Season.SUMMER, new TextureRegion(new Texture(Gdx.files.internal("fishes/Rainbow_Trout.png")))),

    // Legendary
    LEGEND(5000, Season.SPRING,  new TextureRegion(new Texture(Gdx.files.internal("fishes/Legend.png")))),
    GLACIERFISH(1000, Season.WINTER, new TextureRegion(new Texture(Gdx.files.internal("fishes/Glacierfish.png")))),
    ANGLER(900, Season.FALL,  new TextureRegion(new Texture(Gdx.files.internal("fishes/Angler.png")))),
    CRIMSONFISH(1500, Season.SUMMER, new TextureRegion(new Texture(Gdx.files.internal("fishes/Crimsonfish.png")))),;

    public final int basePrice;
    public final Season season;
    public final TextureRegion texture;

    FishType(int basePrice, Season season, TextureRegion texture) {
        this.basePrice = basePrice;
        this.season = season;
        this.texture = texture;
    }

    public Fish create() {
        return new Fish(this.name(), this, basePrice, season);
    }

    public boolean isLegendary() {
        return this == LEGEND || this == GLACIERFISH || this == ANGLER || this == CRIMSONFISH;
    }


    public static Fish getFishByName(String name) {
        for (FishType type : values()) {
            if (name.equalsIgnoreCase(type.name())) {
                return type.create();
            }
        }
        return null;
    }
}
