package com.StardewValley.network.server;// در GameSession.java (سمت سرور)

import com.StardewValley.models.*;
import com.StardewValley.network.client.controllers.ClientHandler;
import com.StardewValley.network.shares.dtos.GameStateDTO;
import com.StardewValley.network.shares.dtos.PlayerStateDTO;
import com.StardewValley.network.shares.dtos.ShowReactionDTO;
import com.StardewValley.network.shares.message.PlayerMovePayload;
import com.StardewValley.network.shares.message.PlayerReactionPayload;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameSession implements Runnable {
    private Game actualGame;
    private List<ClientHandler> playersInSession;

    private final Map<String, Long> disconnectedPlayers = new ConcurrentHashMap<>();

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

    // Check disconnection
    public void onPlayerDisconnected(String username) {
        // 1. بازیکن را به لیست قطع شده‌ها اضافه کن و زمان فعلی را ثبت کن
        disconnectedPlayers.put(username, System.currentTimeMillis());

        // 2. به بقیه بازیکنان در بازی اطلاع بده
        String message = "Player '" + username + "' has disconnected. They have 2 minutes to reconnect.";
        Request infoRequest = new Request(RequestType.PLAYER_DISCONNECTED, message);
        broadcastToSession(infoRequest);

        // 3. یک ترد جدید برای چک کردن تایمر 2 دقیقه‌ای بساز
        new Thread(() -> {
            try {
                // 120,000 میلی‌ثانیه = 2 دقیقه
                Thread.sleep(120000);

                // اگر بعد از 2 دقیقه، بازیکن هنوز در لیست قطع شده‌ها بود، یعنی برنگشته
                if (disconnectedPlayers.containsKey(username)) {
                    // بازیکن را به طور کامل حذف کن
                    kickPlayerPermanently(username);
                }
            } catch (InterruptedException e) {
                // اگر بازیکن زودتر برگشت، این ترد interrupt می‌شود
            }
        }).start();
    }

    public void onPlayerReconnected(String username) {
        // اگر بازیکن در لیست قطع شده‌ها بود، او را برگردان
        if (disconnectedPlayers.containsKey(username)) {
            disconnectedPlayers.remove(username);

            String message = "Player '" + username + "' has reconnected!";
            Request infoRequest = new Request(RequestType.PLAYER_RECONNECTED, message);
            broadcastToSession(infoRequest);
            // TODO: باید ترد تایمر آن بازیکن را interrupt کنیم تا kick نشود
        }
    }

    private void kickPlayerPermanently(String username) {
        disconnectedPlayers.remove(username);
        System.out.println("Player " + username + " did not reconnect in time. Kicking permanently.");
    }

    public void processReactionRequest(PlayerReactionPayload payload, String fromUsername) {
        System.out.println("Player " + fromUsername + " reacted with: " + payload.getReactionContent());

        // ساخت DTO برای ارسال به همه کلاینت‌ها
        ShowReactionDTO reactionInfo = new ShowReactionDTO(fromUsername, payload.getReactionContent());
        Request reactionRequest = new Request(RequestType.SHOW_REACTION_ON_PLAYER, reactionInfo);

        // ارسال به همه بازیکنان در این جلسه (شامل خود فرد هم می‌شود اگر بخواهید)
        broadcastToSession(reactionRequest);
    }
}