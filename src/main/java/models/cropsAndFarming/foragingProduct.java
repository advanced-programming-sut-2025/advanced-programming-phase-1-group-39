package models.cropsAndFarming;

import models.Enums.Season;

import java.util.ArrayList;

public class foragingProduct extends Crop {
    ForagingSource source;
    boolean isSpawnableInLand;
    boolean isSpawnableInMine;
    ArrayList<Season> seasons;
}
