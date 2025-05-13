package models.animals;

import models.Location;
import java.util.ArrayList;

public class Animal {
    private AnimalType type;
    private String name;
    private int price;
    private LivingPlace place;
    private Location location;

    private ArrayList<AnimalProduct> products;

    private int friendship = 0;
    private boolean pettedToday = false;
    private boolean fedToday = false;
    private boolean outsideToday = false;
    private int daysSinceLastProduce = 0;

    private AnimalProduct todayProduct = null;

    public Animal(AnimalType type, String name, int price, LivingPlace place, ArrayList<AnimalProduct> products) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.place = place;
        this.products = products;
    }

    public AnimalType getType() { return type; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public LivingPlace getPlace() { return place; }
    public ArrayList<AnimalProduct> getProducts() { return products; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
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

        produced = produced.clone(); // clone to avoid altering original
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
                ", location=" + location +
                ", products=" + products +
                ", friendship=" + friendship +
                ", pettedToday=" + pettedToday +
                ", fedToday=" + fedToday +
                ", outsideToday=" + outsideToday +
                ", daysSinceLastProduce=" + daysSinceLastProduce +
                '}';
    }
}
