package com.StardewValley.network.server;// در GameSession.java (سمت سرور)

import com.StardewValley.models.*;
import com.StardewValley.network.client.controllers.ClientHandler;
import com.StardewValley.network.shares.dtos.GameStateDTO;
import com.StardewValley.network.shares.dtos.PlayerStateDTO;
import com.StardewValley.network.shares.message.PlayerMovePayload;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSession implements Runnable {
    private Game actualGame;
    private List<ClientHandler> playersInSession;
    private boolean isRunning = true;

    public GameSession(List<ClientHandler> players, Game game) {
        this.playersInSession = players;
        this.actualGame = game;
        this.actualGame.startGame(); // آماده‌سازی اولیه
    }

    // این متد باید در یک ترد جداگانه اجرا شود تا بازی را به روز کند
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0; // 60 آپدیت در ثانیه
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                // آپدیت منطق بازی (مثلا حرکت NPC ها)
                //updateGameLogic();
                // ارسال وضعیت جدید به همه کلاینت‌ها
                broadcastGameState();
                delta--;
            }
        }
    }

    // ساخت و ارسال وضعیت فعلی بازی برای همه
    private void broadcastGameState() {
        Map<String, PlayerStateDTO> currentStates = new HashMap<>();
        for (Player p : actualGame.getPlayers()) {
            PlayerStateDTO dto = new PlayerStateDTO(p.getUsername(), p.getX(), p.getY(), p.getDirection());
            currentStates.put(p.getUsername(), dto);
        }

        GameStateDTO gameState = new GameStateDTO(currentStates, actualGame.getTime());
        Request updateRequest = new Request(RequestType.UPDATE_GAME_STATE, gameState);

        broadcastToSession(updateRequest);
    }

    public void broadcastToSession(Request request) {
        for (ClientHandler client : playersInSession) {
            client.sendMessage(request);
        }
    }

    /*public void processMoveRequest(PlayerMovePayload payload, String clientUsername) {
        Player playerToMove = actualGame.getPlayerByUsername(clientUsername);
        if (playerToMove != null) {
            float newX = playerToMove.getX() + payload.getDx();
            float newY = playerToMove.getY() + payload.getDy();

            // <<--- اینجا منطق بررسی برخورد را از کد قدیمی کلاینت می‌آوریم ---<<

            // ابتدا باید یک Location از پیکسل‌ها بسازیم
            Location newPixelLocation = new Location(Math.round(newX), Math.round(newY));

            // سپس آن را به مختصات کاشی تبدیل کنیم
            Location newTileLocation = Map.pixelToTileConverter(newPixelLocation);

            // حالا از متد خود نقشه برای بررسی استفاده می‌کنیم
            Result result = actualGame.getMap().canWalkTo(newTileLocation, playerToMove, actualGame.getPlayers());

            if (result.success()) {
                // اگر حرکت مجاز بود، موقعیت بازیکن را در سرور آپدیت می‌کنیم
                playerToMove.setLocationAbsolut(newX, newY);
                // اینجا می‌توانید جهت بازیکن را هم بر اساس dx و dy تنظیم کنید
                // playerToMove.setDirection(...);

                // و انرژی را هم در سرور کم می‌کنیم
                // این عدد را از کد قبلی کلاینت آوردم، باید آن را دقیق‌تر محاسبه کنید
                playerToMove.changeEnergy(-Constants.MAX_ENERGY * 0.0005);
            }
            // اگر حرکت مجاز نبود، هیچ کاری انجام نمی‌دهیم. بازیکن در جای خود می‌ماند.
            // کلاینت در فریم بعدی آپدیت را دریافت کرده و می‌بیند که بازیکن حرکت نکرده است.
        }
    }*/
}