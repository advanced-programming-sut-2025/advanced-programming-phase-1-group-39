package com.StardewValley.models.crafting;

import com.StardewValley.controllers.AppControllers;
import com.StardewValley.graphicViews.GameGuiController;
import com.StardewValley.models.ItemStack;
import com.StardewValley.models.Player;
import com.StardewValley.models.inventory.Inventory;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CraftingWidget extends Table {
    private Image image;
    private CraftingRecipe recipe;
    private Inventory inventory;
    private GameGuiController controller;

    public CraftingWidget(CraftingRecipe recipe, Player player, Skin skin, GameGuiController controller) {
        this.recipe = recipe;
        this.inventory = player.getInventory();
        this.controller = controller;

        image = new Image(new TextureRegionDrawable(new TextureRegion(recipe.getItem().getTexture())));
        image.setColor(player.hasLearnedCraftingRecipe(recipe) ? Color.WHITE : Color.GRAY);

        add(image).width(64).height(90);

        // نمایش Tooltip
        Table tooltipTable = new Table(skin);
        tooltipTable.setBackground(skin.getDrawable("square"));
        tooltipTable.pad(10);
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.YELLOW);
        tooltipTable.add(new Label(recipe.getName(), labelStyle)).row();

        for (String name : recipe.getIngredients().keySet()) {
            String line = "- " + name + " × " + recipe.getIngredients().get(name);
            tooltipTable.add(new Label(line, labelStyle)).row();
        }

        Tooltip<Table> tooltip = new Tooltip<>(tooltipTable, new TooltipManager());


        addListener(tooltip);

        // هندل کلیک برای ساخت
        addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (player.hasLearnedCraftingRecipe(recipe)) {
                    controller.craft(recipe, player);
                } else {
                    // صدای خطا یا فلش قرمز نشون بده Todo
                }
            }
        });
    }

    public void update(Player player) {
        image.setColor(player.hasLearnedCraftingRecipe(recipe) ? Color.WHITE : Color.GRAY);
    }
}
