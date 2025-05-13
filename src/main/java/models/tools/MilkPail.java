package models.tools;

import models.*;
import models.animals.Animal;
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
            return new Result(true, "You got Milk of Your "+animal.getName()+" !");
        }
    }


    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy)
                * getWeatherMultiplier(weather));
    }
}
