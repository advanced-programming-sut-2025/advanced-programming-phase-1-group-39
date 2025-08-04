package com.StardewValley.models.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameAssetManager {
    public static Skin skin;
    public static Texture titleImage;
    //public static Skin skin = new Skin(Gdx.files.internal("skin2/uiskin.json"));
    public static Texture logoTexture = new Texture("Stardew_Valley_Images-main/sprites/Logo No Background.png");
    public static Texture MenuTexture = new Texture("Stardew_Valley_Images-main/sprites/Panorama.png");
    public static Texture MenuTexture2 = new Texture("Stardew_Valley_Images-main/sprites/pixel-art-river-landscape-illustration (1).jpg");
    public static Texture pregameBackground = new Texture(Gdx.files.internal("menu_background.jfif"));

    public static Label.LabelStyle messageBoxStyle;

    public static Music music1 = Gdx.audio.newMusic(Gdx.files.internal("musics/01. Stardew Valley Overture.mp3"));
    // avatars
    public static String avatar1 = "Avatars/Alex.png";
    public static String avatar2 = "Avatars/Sam.png";
    public static String avatar3 = "Avatars/Leah.png";
    public static String avatar4 = "Avatars/Penny.png";

    public static TextureRegion nameLabel = new TextureRegion(new Texture("Label/Marlon.png"));
    public static TextureRegion avatarFrame = new TextureRegion(new Texture("Label/Gunther.png"));


    // Plants
    public static Texture deadPlantTexture = getDeadPlantTexture();

    public static TextureAtlas cropsAtlas = getCropsAtlas();
    public static TextureAtlas foragingsAtlas = getForagingsAtlas();
    public static TextureAtlas treesAtlas = getTressAtlas();

    public static Texture blackBox;

    public static Texture energyBox = new Texture(Gdx.files.internal("energy_bar.png"));
    public static ProgressBar.ProgressBarStyle greenBarStyle;
    public static ProgressBar.ProgressBarStyle yellowBarStyle;
    public static ProgressBar.ProgressBarStyle orangeBarStyle;
    public static ProgressBar.ProgressBarStyle redBarStyle;

    static {
        initializeAssets();
    }

    public static void initializeAssets() {
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        titleImage = new Texture(Gdx.files.internal("title-logo.png"));

        setMessageBoxStyle();

        loadBlackBg();
        loadBarStyles();
    }

    private static void loadBarStyles() {
        greenBarStyle = createEnergyBarStyle(Color.GREEN);
        yellowBarStyle = createEnergyBarStyle(Color.YELLOW);
        orangeBarStyle = createEnergyBarStyle(Color.ORANGE);
        redBarStyle = createEnergyBarStyle(Color.RED);
    }

    public static TextureAtlas getCropsAtlas() {
        TextureAtlas crops =  new TextureAtlas(Gdx.files.internal("crops/crops.atlas"));
        for (Texture texture : crops.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        return crops;
    }

    private static Texture getDeadPlantTexture() {
        Texture texture = new Texture(Gdx.files.internal("crops/dead_plant.png"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        return texture;
    }

    public static TextureAtlas getForagingsAtlas() {
        TextureAtlas foragings = new TextureAtlas(Gdx.files.internal("foragings/foragings.atlas"));
        for (Texture texture : foragings.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        return foragings;
    }

    public static TextureAtlas getTressAtlas() {
        TextureAtlas trees = new TextureAtlas(Gdx.files.internal("trees/trees.atlas"));
        for (Texture texture : trees.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        return trees;
    }

    public static Texture getPregameBackground() {
        pregameBackground.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return pregameBackground;
    }


    public static void setMessageBoxStyle() {
        Texture messageBoxTexture = new Texture(Gdx.files.internal("box.9.png"));
        NinePatch ninePatch = new NinePatch(messageBoxTexture, 1, 1, 1, 1);
        NinePatchDrawable messageBoxDrawable = new NinePatchDrawable(ninePatch);

        messageBoxStyle = new Label.LabelStyle();
        messageBoxStyle.font = skin.getFont("font");
        messageBoxStyle.fontColor = Color.BLACK;
        messageBoxStyle.background = messageBoxDrawable;

        messageBoxStyle.background.setLeftWidth(30);
        messageBoxStyle.background.setRightWidth(30);
        messageBoxStyle.background.setTopHeight(30);
        messageBoxStyle.background.setBottomHeight(30);

        messageBoxStyle.background.setMinWidth(450);
        messageBoxStyle.background.setMinHeight(100);
    }

    public static void loadBlackBg() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        blackBox = new Texture(pixmap);
        pixmap.dispose();
    }

    public static ProgressBar.ProgressBarStyle createEnergyBarStyle(Color color) {
        int w = 40, h = 60;
        ProgressBar.ProgressBarStyle barStyle;

        Pixmap bgPixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(0,0,0,0);
        bgPixmap.fill();

        Pixmap fillPixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        fillPixmap.setColor(color);
        fillPixmap.fill();

        Pixmap knobPixmap = new Pixmap(0, 0, Pixmap.Format.RGBA8888);

        barStyle = new ProgressBar.ProgressBarStyle();
        barStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        barStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(knobPixmap)));
        barStyle.knobBefore = new TextureRegionDrawable(new Texture(fillPixmap));

        bgPixmap.dispose();
        fillPixmap.dispose();

        return barStyle;
    }
}
