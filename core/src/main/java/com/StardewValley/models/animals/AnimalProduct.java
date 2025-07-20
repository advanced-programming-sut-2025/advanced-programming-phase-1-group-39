package com.StardewValley.models.animals;

import com.StardewValley.models.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimalProduct extends Item {
    private int baseSellPrice;
    private AnimalProductQuality quality;

    public AnimalProduct(String name, int baseSellPrice) {
        super(name);
        this.baseSellPrice = baseSellPrice;
    }

    @Override
    public TextureRegion getTexture() {
        return new TextureRegion(new Texture(Gdx.files.internal("animalProducts/" + name.replaceAll(" ", "_") + ".png")));
    }

    public String getName() {
        return name;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    public AnimalProductQuality getQuality() {
        return quality;
    }

    public void setQuality(AnimalProductQuality quality) {
        this.quality = quality;
    }


    @Override
    public String toString() {
        return "AnimalProduct{" +
                "baseSellPrice=" + baseSellPrice +
                ", name='" + name + '\'' +
                '}';
    }

    public AnimalProduct clone() {
        AnimalProduct newProduct = new AnimalProduct(this.name, this.baseSellPrice);
        newProduct.setQuality(this.quality);
        return newProduct;
    }
}