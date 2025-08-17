package com.StardewValley.models.tools;


import com.StardewValley.models.*;
import com.StardewValley.models.animals.Fish;
import com.StardewValley.models.map.Tile;
import com.StardewValley.models.map.TileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class FishingPole extends Tool {
    private FishingPoleType poleType;
    private TextureRegion texture;

    public FishingPole(String name, FishingPoleType poleType, TextureRegion texture) {
        super(name, ToolType.BASIC, poleType.getUsingEnergy());
        this.poleType = poleType;
        this.texture = texture;
    }

    public FishingPole(String name, FishingPoleType poleType) {
        super(name, ToolType.BASIC, poleType.getUsingEnergy());
        this.poleType = poleType;
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }

    @Override
    public Result useTool(Tile tile, Player player, Skill skill) {
        if (tile.getType() == TileType.WATER) {
            Game game = App.getApp().getCurrentGame();

            FishingPole fishingPole = (FishingPole) player.getInventory().getItemByName(player.getInventory().getInHand().getName()).getItem();

            int usingEnergy = fishingPole.getUsingEnergy(player.getSkills(), game.getTodayWeather());
            if (player.getTurnEnergy() <= usingEnergy) {
                return new Result(false, "You don't have enough energy to use fishing pole!");
            }

            if (game.getMap().isNearWater(player)) {
                ArrayList<Fish> caughtFishes = player.goFishing(fishingPole, game.getTodayWeather(), game.getTime().getSeason());
                player.changeEnergy(-usingEnergy);
                if (caughtFishes.isEmpty()) {
                    return new Result(true, "You didn't got any fish!" + "Energy consumed: " + usingEnergy);
                }
                int num = 0;
                StringBuilder text = new StringBuilder("Fishes:\n");
                System.out.println(caughtFishes);

                for (Fish caughtFish : caughtFishes) {
                    ItemStack fishItem = new ItemStack(caughtFish, 1);

                    if (!player.getInventory().hasSpace(fishItem)) {
                        if (num == 0)
                            return new Result(true, "You don't have enough space to add fish!");
                        else
                            return new Result(true, "You caught " + num + " fishes!\n" + text);
                    }
                    num ++;
                    text.append(caughtFish.getType().name().toLowerCase() + "\n");
                    player.getInventory().addItem(fishItem.getItem(), fishItem.getAmount());
                }
                player.getSkills().addToFishingXP(5);
                return new Result(true, "You caught " + num + " fishes! (Fishing XP + 5)\n" + text);
            } else {
                return new Result(false, "You need to be near water to get Fish!");
            }
        }
        return null;
    }

    public int getSkillEnergyReduce(Skill skill) {
        return skill.isFishingLevelMax() ? 1 : 0;
    }

    @Override
    public int getUsingEnergy(Skill skill, Weather weather) {
        return (int)((baseUsingEnergy - getSkillEnergyReduce(skill))
                * getWeatherMultiplier(weather));
    }

    public FishingPoleType getPoleType() {
        return poleType;
    }
}