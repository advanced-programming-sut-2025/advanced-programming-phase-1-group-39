package models.tools;

import models.*;
import models.animals.Animal;
import models.animals.AnimalProduct;
import models.animals.AnimalType;
import models.animals.LivingPlace;
import models.map.Tile;

public class MilkPail extends Tool {
    public MilkPail() {
        super("milk pail", ToolType.BASIC, 4);
    }

    @Override
    public Result useTool(Tile tile, Player player) {
        Location location = tile.getLocation();
        Animal animal = player.getAnimalByLocation(location);
        if (animal == null)
            return new Result(false, "Animal not found");
        else if (animal.getPlace() != LivingPlace.BARN || animal.getPlace() != LivingPlace.BIG_BARN
                || animal.getPlace() != LivingPlace.DELUXE_BARN
                || animal.getType() != AnimalType.COW || animal.getType() != AnimalType.GOAT) {
            return new Result(false, "This Animal can't produce Milk!");
        } else {
            AnimalProduct product = animal.getProduct();
            if (!player.getInventory().hasSpace(new ItemStack(product, 1)))
                return new Result(false, "You don't have enough space to get Animal products!");
            player.getInventory().addItem(product, 1);

            animal.changeFriendShip(5);
            ItemStack products = new ItemStack(animal.collectProduct(), 1);
            player.getInventory().addItem(products.getItem(), 1);
            return new Result(true, "You got Milk of Your "+animal.getName()+" !");
        }
    }


    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy)
                * getWeatherMultiplier(weather));
    }
}
