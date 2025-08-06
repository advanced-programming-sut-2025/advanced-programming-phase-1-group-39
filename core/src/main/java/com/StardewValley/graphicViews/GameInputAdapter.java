package com.StardewValley.graphicViews;


import com.StardewValley.models.*;
import com.StardewValley.network.client.NetworkClient;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class GameInputAdapter extends InputAdapter {
    private GameGuiController controller;
    private GameScreen screen;

    public GameInputAdapter(GameGuiController controller, GameScreen screen) {
        this.controller = controller;
        this.screen = screen;
    }

    // در کلاس GameInputAdapter.java

    public void handlePlayerMovement(float delta, Game game, NetworkClient networkClient) {
        // 1. دریافت بازیکن محلی (بازیکنی که پشت این کامپیوتر است)
        // نکته: مفهوم playerInTurn برای حرکت دیگر کاربرد ندارد. ما به بازیکن اصلی این کلاینت نیاز داریم.
        // فرض می‌کنیم game.getMainPlayer() بازیکن این کلاینت را برمی‌گرداند.
        Player localPlayer = game.getMainPlayer();

        // 2. بررسی‌های اولیه سمت کلاینت (اینها خوب هستند و باید بمانند)
        if (localPlayer.getCurrentState().equals(GameScreen.PlayerState.Unconscious)) {
            return; // اگر بازیکن بیهوش است، هیچ ورودی ارسال نکن
        }

        // منطق UI گلخانه کاملاً سمت کلاینت است و می‌تواند باقی بماند.
        // چون فقط یک پاپ‌آپ نمایش می‌دهد و وضعیت بازی را تغییر نمی‌دهد.
        // تنها زمانی که کاربر "Yes" را بزند، یک درخواست جداگانه به سرور ارسال می‌شود.
        if (controller.nearGreenHouse(localPlayer) && !localPlayer.isBuildGreenhouse()) {
            Result buildGreenHousePopup = controller.buildGreenHouseRequest(localPlayer);
            if (!buildGreenHousePopup.success()) {
                screen.showPopup(buildGreenHousePopup.message(), () -> {
                    // وقتی کاربر "Yes" را کلیک کرد، این درخواست به سرور ارسال می‌شود:
                    networkClient.sendRequest(new Request(RequestType.BUILD_GREENHOUSE, null));
                });
            } else {
                screen.showError(buildGreenHousePopup.message());
            }
        }


        // 3. خواندن ورودی و محاسبه جهت حرکت
        Vector2 movementIntent = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movementIntent.y += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movementIntent.y -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movementIntent.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movementIntent.x += 1;
        }

        // اگر هیچ کلیدی فشار داده نشده، کاری انجام نده
        if (movementIntent.isZero()) {
            // (اختیاری) می‌توانید یک درخواست "توقف حرکت" بفرستید تا سرور جهت بازیکن را NONE کند
            // networkClient.sendMoveRequest(0, 0);
            return;
        }

        // 4. محاسبه بردار نهایی حرکت و ارسال به سرور
        float speed = game.getGameSetting().getPlayerSpeed();
        movementIntent.nor().scl(speed * delta); // نرمالایز و اعمال سرعت و دلتا

        // <<--- بخش کلیدی: ارسال درخواست به سرور ---<<
        // کلاینت دیگر خودش را حرکت نمی‌دهد، فقط قصدش را به سرور اعلام می‌کند.
//        networkClient.sendMoveRequest(movementIntent.x, movementIntent.y);

        // تمام منطق قبلی از اینجا به بعد حذف می‌شود!
    /*
        حذف شد:
        - Map map = game.getMap();
        - float newX = ...;
        - game.getMap().canWalkTo(...);  <-- بررسی برخورد حالا وظیفه سرور است
        - player.setLocationAbsolut(...); <-- تغییر موقعیت حالا وظیفه سرور است
        - player.setDirection(...);       <-- تنظیم جهت حالا وظیفه سرور است
        - player.changeEnergy(...);       <-- تغییر انرژی حالا وظیفه سرور است
    */
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            screen.toggleExitMenu();
        } else if (keycode == Input.Keys.ENTER) {
            screen.blackBackgroundAnimation(() -> controller.changeTurn(), 0.2f);
        } else if (keycode == Input.Keys.BACKSLASH || keycode == Input.Keys.SLASH) {
            screen.toggleTerminalBox();
        }
        ///  test
        else if (keycode == Input.Keys.MINUS) {
            screen.getGame().getPlayerInTurn().changeEnergy(-10);
        }

        else if (keycode == Input.Keys.N) {
            Game game = App.getApp().getCurrentGame();
            game.getMap().growWateredPlantsAndTrees();
        } else if (keycode == Input.Keys.C) {
            screen.changeCookingMenu();
        } else if (keycode == Input.Keys.B) {
            screen.changeCraftingMenu();
        } else if (keycode == Input.Keys.P) {
            screen.showShopMenu();
        }
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        Game game = screen.getGame();

        int current = game.getPlayerInTurn().getSelectedSlot();
        int size = game.getPlayerInTurn().getMaxInventorySize();
        int next = (current + (amountY > 0 ? 1 : -1) + size) % size;
        game.getPlayerInTurn().setSelectedSlot(next);
        return true;
    }
}