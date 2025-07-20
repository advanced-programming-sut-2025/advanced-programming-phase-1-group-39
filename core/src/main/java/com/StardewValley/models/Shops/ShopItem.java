package com.StardewValley.models.Shops;


import com.StardewValley.models.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ShopItem extends Item {
    private int price;
    private int dailyLimit;
    private int availableQuantity;
    private TextureRegion texture;

    public ShopItem(String name, int price, int dailyLimit) {
        super(name);
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.availableQuantity = dailyLimit;
        this.texture = null;
    }

    public String getName() {
        return name;
    }

    public void setTexture(String path) {
        this.texture = new TextureRegion(new Texture(Gdx.files.internal(path)));
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }

    public int getPrice() {
        return price;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void decreaseQuantity(int amount) {
        availableQuantity = Math.max(availableQuantity - amount, 0);
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public void resetDailyLimit() {
        availableQuantity = dailyLimit;
    }

    public void purchase(int amount) {
        if (dailyLimit > 500000) {
            return;
        }
        decreaseQuantity(amount);
    }
}
