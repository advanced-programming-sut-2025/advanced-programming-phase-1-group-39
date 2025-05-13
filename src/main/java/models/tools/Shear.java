package models.tools;

import models.*;
import models.animals.Animal;
import models.animals.AnimalType;
import models.animals.LivingPlace;
import models.map.Tile;

public class Shear extends Tool {
    public Shear() {
        super("shear", ToolType.BASIC, 4);
    }

    @Override
    public Result useTool(Tile tile, Player player) {
        Location location = tile.getLocation();
        Animal animal = player.getAnimalByLocation(location);
        if (animal == null)
            return new Result(false, "Animal not found");
        else if (animal.getPlace() != LivingPlace.BARN ||
                animal.getType() != AnimalType.SHEEP) {
            return new Result(false, "This Animal can't produce Wool!");
        } else {
            return new Result(true, "You got Wool of Your "+animal.getName()+" !");
        }
    }


    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy) * getWeatherMultiplier(weather));
    }
}
