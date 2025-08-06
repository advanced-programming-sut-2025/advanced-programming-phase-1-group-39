package com.StardewValley.models.animals;

import com.StardewValley.models.Game;
import com.StardewValley.models.GameSetting;
import com.StardewValley.models.Location;
import com.StardewValley.models.map.Map;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Animal {
    private AnimalType type;
    private String name;
    private int price;
    private LivingPlace place;

    private Vector2 position;
    private Vector2 firstLocation;
    private Vector2 toGoLocation;


    private ArrayList<AnimalProduct> products;

    private int friendship = 0;
    private boolean pettedToday = false;
    private boolean fedToday = false;
    private boolean outsideToday = false;
    private int daysSinceLastProduce = 0;

    private AnimalProduct todayProduct = null;

    private static final int walkingBoundInTile = 5;

    public Animal(AnimalType type, String name, int price, LivingPlace place, ArrayList<AnimalProduct> products) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.place = place;
        this.products = products;
    }

    public Animal() {
    }

    public AnimalType getType() { return type; }
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
    public int getPrice() { return price; }
    public LivingPlace getPlace() { return place; }
    public ArrayList<AnimalProduct> getProducts() { return products; }


    public void updateMovement(float deltaTime, Game game) {
        if (position.dst(toGoLocation) < 1.0f) {
            setNewRandomToGoLocation();
            return;
        }

        // مسیر به سمت مقصد را محاسبه کن
        Vector2 direction = toGoLocation.cpy().sub(position).nor();
        // قدم بعدی را محاسبه کن
        Vector2 nextStep = position.cpy().mulAdd(direction, GameSetting.getAnimalSpeed() * deltaTime);

        if (game.isPositionPassable(nextStep.x, nextStep.y)) {
            position.set(nextStep);
        } else {
            setNewRandomToGoLocation();
        }
    }

    private void setNewRandomToGoLocation() {
        Random rand = new Random();
        double angle = 2 * Math.PI * rand.nextDouble();
        double radius = walkingBoundInTile * Map.TILE_SIZE * Math.sqrt(rand.nextDouble());

        float dx = (float)(radius * Math.cos(angle));
        float dy = (float)(radius * Math.sin(angle));

        toGoLocation.set(firstLocation.x + dx, firstLocation.y + dy);
    }

    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }

    public void setLoc(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public Location getLocation() {
        return new Location((int)position.x, (int)position.y);
    }

    public void setLocation(Location location) {
        Location inMap = Map.TileToPixelConverter(location);
        this.position = new Vector2(inMap.x(), inMap.y());
        this.firstLocation = new Vector2(inMap.x(), inMap.y());
        this.toGoLocation = new Vector2(inMap.x(), inMap.y());
    }



    public int getFriendship() { return friendship; }

    public void changeFriendship(int amount) {
        friendship += amount;
    }

    public void pet() {
        if (!pettedToday) {
            pettedToday = true;
            friendship += 15;
        }
    }

    public void feedHay() { fedToday = true; }

    public void sendOutside(int x, int y) {
        outsideToday = true;
        fedToday = true;
        setLocation(new Location(x, y));
    }

    public void endDay() {
        if (!pettedToday) friendship -= Math.max(10, 200 - friendship);
        if (!fedToday) friendship -= 20;
        if (!outsideToday) friendship -= 20;

        pettedToday = false;
        fedToday = false;
        outsideToday = false;

        daysSinceLastProduce++;
        todayProduct = null;
    }

    public void generateProductForNextDay() {
        if (!fedToday) return;
        if (daysSinceLastProduce < type.produceCycleDays) return;

        daysSinceLastProduce = 0;
        AnimalProduct baseProduct = type.products.get(0);
        AnimalProduct altProduct = type.products.size() > 1 ? type.products.get(1) : null;

        double chance = (friendship + Math.random() * 150) / 1500.0;
        AnimalProduct produced = (altProduct != null && chance > 0.5) ? altProduct : baseProduct;

        double qualityValue = (Math.random() * 0.5 + 0.5) * friendship / 1000.0;
        AnimalProductQuality quality;
        if (qualityValue < 0.5) quality = AnimalProductQuality.NORMAL;
        else if (qualityValue < 0.7) quality = AnimalProductQuality.SILVER;
        else if (qualityValue < 0.9) quality = AnimalProductQuality.GOLD;
        else quality = AnimalProductQuality.IRIDIUM;

        produced = produced.clone();
        produced.setQuality(quality);
        todayProduct = produced;
    }

    public boolean hasProductReady() {
        return todayProduct != null;
    }

    public AnimalProduct collectProduct() {
        AnimalProduct p = todayProduct;
        todayProduct = null;
        return p;
    }

    public String listProductStatus() {
        if (hasProductReady()) {
            return name + ": " + todayProduct.getName() + " (" + todayProduct.getQuality().toString().toLowerCase() + ")";
        } else {
            return name + ": No product available";
        }
    }

    @Override
    public String toString() {
        return "Animal{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", place=" + place +
                ", location=" + position +
                ", products=" + products +
                ", friendship=" + friendship +
                ", pettedToday=" + pettedToday +
                ", fedToday=" + fedToday +
                ", outsideToday=" + outsideToday +
                ", daysSinceLastProduce=" + daysSinceLastProduce +
                '}';
    }

    public TextureRegion getTexture() {
        return type.getTextureRegion();
    }
}
