package models;

import models.animals.AnimalProduct;
import models.animals.Fish;
import models.artisan.ArtisanGood;
import models.artisan.ArtisanMachine;
import models.crafting.CraftingItem;
import models.cropsAndFarming.Crop;
import models.cropsAndFarming.ForagingMaterial;
import models.cropsAndFarming.ForagingMineral;

public abstract class Item {
    protected String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public Boolean isSellable() {
        if (this instanceof ArtisanGood || this instanceof Fish || this instanceof AnimalProduct
                || this instanceof ArtisanMachine || this instanceof CraftingItem || this instanceof Crop
                || this instanceof ForagingMaterial || this instanceof ForagingMineral) {
            return true;
        }
        return false;
    }
    public int getItemPrice() {
        if (this instanceof ArtisanMachine) {
            return ((ArtisanMachine) this).getSellPrice();
        }
        if (this instanceof ArtisanGood) {
            return ((ArtisanGood) this).getSellPrice();
        }
        if (this instanceof Fish){
            return ((Fish) this).getPrice();
        }
        if (this instanceof AnimalProduct) {
            return ((AnimalProduct) this).getBaseSellPrice();
        }
        if (this instanceof CraftingItem) {
            return ((CraftingItem) this).getSellPrice();
        }
        if (this instanceof Crop) {
            return ((Crop) this).getBaseSellPrice();
        }
        if (this instanceof ForagingMaterial) {
            return ((ForagingMaterial) this).getBaseSellPrice();
        }
        if (this instanceof ForagingMineral) {
            return ((ForagingMineral) this).getBaseSellPrice();
        }

        return 0;
    }

}

