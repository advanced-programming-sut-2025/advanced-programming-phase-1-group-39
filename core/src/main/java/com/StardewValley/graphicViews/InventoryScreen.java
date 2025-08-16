package com.StardewValley.graphicViews;

import com.StardewValley.models.Game;
import com.StardewValley.models.ItemStack;
import com.StardewValley.models.Player;
import com.StardewValley.models.inventory.Inventory;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class InventoryScreen {



    private GameGuiController controller;
    private Game game;
    private GameInputAdapter gameMenuInputAdapter;
    private SpriteBatch batch;

    private Stage uiStage;
    private Window exitWindow = null;

    private MiniMapWidget miniMapWidget;

    // Terminal
    private Window terminalWindow = null;
    private Table historyTable;
    private ScrollPane scrollPane;
    private TextField commandInput;
    private String lastCommand;

    //  player
    private TextureAtlas playerAtlas;
    private HashMap<Player, ArrayList<Animation<TextureRegion>>> playersAnimations = new LinkedHashMap<>();

    private float stateTime = 0f;

    // UI
    private Texture clock;
    private BitmapFont font;
    private BitmapFont smallFont;

    private GlyphLayout layout = new GlyphLayout();
    private TextureRegion inventorySlot;
    private TextureRegion inventoryHighlightSlot;


    private ProgressBar energyBar;
    private Image energyBox;
    private Label energyAmount;

    // inventory
    private Integer pendingTrashSlot = null;
    private boolean pendingInventoryUiRefresh = false;
    private Window inventoryWindow = null;
    Table inventoryTable = new Table();
    private int lastSelectedSlot = -1;
    private int lastBagHash = 0;
    private Cell<?> contentCell;
    private Window skillTooltip;
    private Label skillTooltipLabel;
    private Image skillDescImage; // تصویر توضیح مهارت
    public void toggleInventoryMenu() {
        if (inventoryWindow != null) {
            hideInventoryMenu();
            return;
        }
        Skin skin = GameAssetManager.skin;
        inventoryWindow = new Window("Inventory", skin);
        inventoryWindow.setModal(true);
        inventoryWindow.setSize(1356, 1000);
        inventoryWindow.setPosition(
                uiStage.getWidth() / 2f,
                uiStage.getHeight() / 2f,
                Align.center
        );

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // ==== ردیف اول: دکمه‌های تب ====
        String[] tabNames = {"Journal", "Inventory", "Skills", "Map", "Setting", "Social"};
        Table tabsRow = new Table();

        // رو این دکمه‌ها اکشن تعویض محتوا می‌ذاریم:
        for (String tab : tabNames) {
            TextButton tabBtn = new TextButton(tab, skin);
            tabsRow.add(tabBtn).pad(5);

            tabBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    contentCell.setActor(null);
                    switch (tab) {
                        case "Journal":
                            contentCell.setActor(getQuestsList());
                            break;
                        case "Inventory":
                            contentCell.setActor(getInventoryContentTable());
                            break;
                        case "Skills":
                            contentCell.setActor(getSkillsMenuTable());
                            break;
                        default:
                            Label comingSoon = new Label(tab + " content coming soon!", skin);
                            comingSoon.setAlignment(Align.center);
                            contentCell.setActor(comingSoon);
                    }
                }
            });
        }
        mainTable.add(tabsRow).growX().padTop(35).row();

        // ==== ردیف وسط: محتوای تب جاری ====
        contentCell = mainTable.add(getQuestsList()).expand().fill();
        mainTable.row();

        // ==== ردیف آخر: دکمه Close پایین ====
        TextButton closeBtn = new TextButton("Close", skin);
        closeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideInventoryMenu();
            }
        });
        Table closeRow = new Table();
        closeRow.add(closeBtn).center().padBottom(12);
        mainTable.add(closeRow).growX().bottom().row();

        inventoryWindow.clearChildren();
        inventoryWindow.add(mainTable).grow().pad(8);

        uiStage.addActor(inventoryWindow);
        Gdx.input.setInputProcessor(uiStage);
    }

    private void hideInventoryMenu() {
        if (inventoryWindow != null) {
            inventoryWindow.remove();
            inventoryWindow = null;
            Gdx.input.setInputProcessor(gameMenuInputAdapter);
        }
    }

    private Table getQuestsList() {
        Table questsTable = new Table();
        Skin skin = GameAssetManager.skin;

        Label title = new Label("Journal - Quests", skin, "title");
        title.setAlignment(Align.center);
        title.setFontScale(1f);

        questsTable.add(title).colspan(2).padBottom(32).center().row();

        // NPC names & quests
        String[] npcs = {"Abigail", "Harvey", "Leah", "Robin", "Sebastian"};
        String[] colors = {"e63946", "f1faee", "a8dadc", "457b9d", "1d3557"};

        for (int i = 0; i < npcs.length; i++) {
            Label nameLabel = new Label(npcs[i] + ":", skin, "subtitle");
            nameLabel.setColor(Color.valueOf(colors[i]));
            nameLabel.setFontScale(0.9f);

            Label questsLabel = new Label(controller.getQuesList(npcs[i].toLowerCase()), skin);
            questsLabel.setWrap(true);
            questsLabel.setColor(Color.valueOf(colors[i]));
            questsLabel.setFontScale(0.9f);

            questsTable.add(nameLabel).padRight(50).top().left().width(170);
            questsTable.add(questsLabel).growX().padBottom(30).padTop(10).left().row();
        }

        questsTable.pad(30, 20, 30, 20).top().left();
        return questsTable;
    }

    private Table getInventoryTable() {
        Table inventoryGrid = new Table();
        Skin skin = GameAssetManager.skin;

        Player player = game.getPlayerInTurn();
        Inventory inventory = player.getInventory();
        int numSlots = player.getMaxInventorySize();
        ArrayList<ItemStack> items = inventory.getInventoryItems();

        final float slotSize = 82f;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int i = row * 10 + col;

                Stack slotStack = new Stack();

                Image slotBg = new Image(GameAssetManager.inventorySlot);
                slotBg.setColor(Color.WHITE);
                slotStack.add(slotBg);

                if (i < items.size() && items.get(i) != null && items.get(i).getItem() != null) {
                    TextureRegion itemTex = items.get(i).getItem().getTexture();
                    Image itemImg = new Image(new TextureRegionDrawable(itemTex));
                    slotStack.add(itemImg);

                    int quantity = items.get(i).getAmount();
                    if (quantity > 1) {
                        Table countTable = new Table();
                        Label lbl = new Label(String.valueOf(quantity), skin);
                        lbl.setFontScale(0.74f);
                        lbl.setColor(Color.GOLD);
                        countTable.add(lbl).bottom().center().padBottom(4);
                        countTable.setFillParent(true);
                        countTable.bottom();
                        slotStack.add(countTable);
                    }
                }

                if (i == player.getSelectedSlot()) {
                    Image highlight = new Image(GameAssetManager.inventoryHighlightSlot);
                    highlight.setColor(new Color(1, 1, 1, 0.41f));
                    slotStack.add(highlight);
                }

                final int slotIndex = i;
                slotStack.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        player.setSelectedSlot(slotIndex);
                        updateInventoryContent();
                        return true;
                    }
                });

                inventoryGrid.add(slotStack).size(slotSize, slotSize).pad(4);
            }
            inventoryGrid.row();
        }

        return inventoryGrid;
    }

    private Table getInventoryContentTable() {
        Skin skin = GameAssetManager.skin;

        Image trashImg = new Image(new Texture("inventory/stardewmoddingapi_ou7895mbeu.png"));
        trashImg.setScaling(Scaling.fit);
        trashImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Player player = game.getPlayerInTurn();
                int idx = player.getSelectedSlot();
                Inventory inv = player.getInventory();
                ArrayList<ItemStack> items = inv.getInventoryItems();

                if (idx >= 0 && idx < items.size() && items.get(idx) != null) {
                    pendingTrashSlot = idx;
                }

                return true;
            }
        });

        Table inventoryMenuTable = new Table(skin);

        Table gridTable = getInventoryTable();
        ScrollPane scrollPane = new ScrollPane(gridTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollPercentY(0); // اول جدول

        float slotSize = 82 + 8;
        scrollPane.setHeight(2 * slotSize);

        // ---- لیبل بالای جدول ----
        Label titleLabel = new Label("Inventory", skin, "title");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.16f);
        inventoryMenuTable.add(titleLabel).center().padBottom(28).row();

        Table toolsRow = new Table();
        toolsRow.add(scrollPane).width(944).height(2 * slotSize).padRight(14); // 10*82 + پدها
        toolsRow.add(trashImg).size(55, 55).center();

        inventoryMenuTable.add(toolsRow).center().padTop(33);
        inventoryMenuTable.row();

        return inventoryMenuTable;
    }

    public void updateInventoryContent() {
        if (contentCell != null) {
            contentCell.setActor(getInventoryContentTable());
        }
    }

    private Table getSkillsMenuTable() {
        Skin skin = GameAssetManager.skin;
        Player player = game.getPlayerInTurn();

        Table skillsTable = new Table(skin);

        // عنوان
        Label title = new Label("Skills", skin, "title");
        //title.setAlignment(Align.center);
        title.setFontScale(1.15f);
        skillsTable.add(title).padBottom(36).center().colspan(2).padLeft(700).row();

        // اسامی و آیکون و عکس توضیح هر مهارت
        String[] skills = {"Farming", "Fishing", "Mining", "Foraging"};
        String[] skillIcons = {
                GameAssetManager.farmingSkillName,
                GameAssetManager.fishingSkillName,
                GameAssetManager.miningSkillName,
                GameAssetManager.foragingSkillName
        };
        String[] skillDescImgs = {
                "inventory/farming.png",
                "inventory/fishing.png",
                "inventory/mining.png",
                "inventory/foraging.png"
        };
        int[] skillLevels = {
                player.getSkills().getFarmingLevel(),
                player.getSkills().getFishingLevel(),
                player.getSkills().getMiningLevel(),
                player.getSkills().getForagingLevel()
        };
        // تصاویر ستاره برای همه مهارت‌ها مشترک
        String[] starImages = {
                GameAssetManager.star1Name,
                GameAssetManager.star2Name,
                GameAssetManager.star3Name,
                GameAssetManager.star4Name
        };

        String[] skillColors = {
                "#B2E672", // Farming
                "#7DD1F4", // Fishing
                "#DDBEEA", // Mining
                "#FECC5C"  // Foraging
        };

        for (int i = 0; i < skills.length; i++) {
            Table row = new Table(skin);

            // آیکون مهارت
            Image skillIcon = new Image(new TextureRegion(new Texture(skillIcons[i])));
            skillIcon.setSize(64, 64);

            // عکس توضیح مهارت مخصوص همون اسکیل
            final TextureRegion descRegion = new TextureRegion(new Texture(skillDescImgs[i]));
            skillIcon.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    showSkillImageTooltip(descRegion, skillIcon);
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    hideSkillImageTooltip();
                }
            });

            row.add(skillIcon).size(64, 64).padRight(18);

            // عنوان
            Label lbl = new Label(skills[i], skin, "title");
            lbl.setColor(Color.valueOf(skillColors[i]));
            lbl.setFontScale(0.5f);
            row.add(lbl).padRight(40);

            // ستاره‌ها (نمایش فقط لول‌هایی که باز شده)
            Table stars = new Table();
            int level = skillLevels[i];
            for (int lv = 0; lv < 4; lv++) {
                if (lv < level) {
                    Image star = new Image(new TextureRegion(new Texture(starImages[lv])));
                    star.setSize(28, 28);
                    stars.add(star).pad(2);
                }
            }
            row.add(stars).padLeft(24);

            skillsTable.add(row).padBottom(32).left().row();
        }

        skillsTable.top().pad(40, 36, 30, 36).left();
        return skillsTable;
    }

    private void showSkillImageTooltip(TextureRegion region, Actor icon) {
        if (skillDescImage == null) {
            skillDescImage = new Image(region);
            uiStage.addActor(skillDescImage);
        } else {
            skillDescImage.setDrawable(new TextureRegionDrawable(region));
            skillDescImage.setVisible(true);
        }
        // اندازه سه برابر آیکون مهارت
        skillDescImage.setSize(420, 210);
        // موقعیت سمت چپ آیکون
        float tipX = icon.localToStageCoordinates(new Vector2(-icon.getWidth() * 3 - 135, 0)).x;
        float tipY = icon.localToStageCoordinates(new Vector2(0, 50)).y;
        skillDescImage.setPosition(tipX, tipY);
        skillDescImage.toFront();
        skillDescImage.setVisible(true);
    }

    private void hideSkillImageTooltip() {
        if (skillDescImage != null)
            skillDescImage.setVisible(false);
    }
}
