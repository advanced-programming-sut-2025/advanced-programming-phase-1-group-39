package com.StardewValley.network.server;

import com.StardewValley.models.Player; // import Player model
import com.StardewValley.models.Game; // import Game model
import com.StardewValley.network.client.controllers.ClientHandler;
import com.StardewValley.network.shares.Lobby;
import com.StardewValley.network.shares.message.ChatMessage;
import com.StardewValley.network.shares.message.PlayerMovePayload;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class ServerMain {
    private static final int PORT = 8080;
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static final List<Lobby> lobbies = Collections.synchronizedList(new ArrayList<>());
    private static final Map<Integer, GameSession> activeGames = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New player connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // متد createLobby با دریافت نام کاربری ادمین
    public static void createLobby(String lobbyName, String adminUsername, ClientHandler adminHandler) {
        Lobby newLobby = new Lobby(lobbyName, adminUsername); // ادمین با نام کاربری شناخته می‌شود
        synchronized (lobbies) {
            lobbies.add(newLobby);
            adminHandler.setUsername(adminUsername); // نام کاربری را در هندلر ذخیره کن
            adminHandler.setLobbyId(newLobby.getId());
            System.out.println("New lobby created: " + newLobby + " by " + adminUsername);
            broadcastLobbyListInternal();
        }
    }

    // متد joinLobby با دریافت نام کاربری
    public static void joinLobby(String lobbyId, String username, ClientHandler joiningClient) {
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getId().equals(lobbyId) && !lobby.isFull()) {
                    boolean success = lobby.addPlayer(username); // بازیکن با نام کاربری اضافه می‌شود
                    if (success) {
                        joiningClient.setUsername(username); // نام کاربری را در هندلر ذخیره کن
                        joiningClient.setLobbyId(lobby.getId());
                        System.out.println("Player " + username + " joined lobby. New state: " + lobby);
                        broadcastLobbyListInternal();
                    } else {
                        System.out.println("Failed to join lobby for player " + username);
                    }
                    return;
                }
            }
            System.err.println("Lobby with ID " + lobbyId + " not found or is full.");
        }
    }

    // *** متد اصلی بازنویسی شده ***
    public static void startGame(ClientHandler requestingClient) {
        String lobbyId = requestingClient.getLobbyId();
        if (lobbyId == null) {
            requestingClient.sendMessage(new Request(RequestType.GAME_START_FAILED, "You are not in a lobby."));
            return;
        }

        Lobby targetLobby;
        List<ClientHandler> playersInLobby = new ArrayList<>();

        // با یک بار قفل کردن، هم لابی را پیدا می‌کنیم و هم لیست کلاینت‌ها را می‌سازیم
        synchronized (clients) {
            synchronized (lobbies) {
                targetLobby = lobbies.stream().filter(l -> l.getId().equals(lobbyId)).findFirst().orElse(null);
                if (targetLobby == null) {
                    requestingClient.sendMessage(new Request(RequestType.GAME_START_FAILED, "Lobby not found."));
                    return;
                }

                // کلاینت‌های حاضر در این لابی را پیدا کن
                for (ClientHandler client : clients) {
                    if (lobbyId.equals(client.getLobbyId())) {
                        playersInLobby.add(client);
                    }
                }
            }
        }

        // بررسی ادمین بودن و تعداد بازیکنان
        if (!targetLobby.getAdmin().equals(requestingClient.getUsername())) {
            requestingClient.sendMessage(new Request(RequestType.GAME_START_FAILED, "Only the admin can start the game."));
            return;
        }
        if (targetLobby.getPlayerCount() < 1) { // برای تست می‌توانید با ۱ بازیکن شروع کنید
            requestingClient.sendMessage(new Request(RequestType.GAME_START_FAILED, "You need at least 2 players to start."));
            return;
        }

        // 1. ساختن مدل‌های Player بر اساس کلاینت‌های لابی
        ArrayList<Player> gamePlayers = new ArrayList<>();
        for (ClientHandler client : playersInLobby) {
            // فرض می‌کنیم Player constructor شما (username, nickname, gameId) است
            // gameId بعدا توسط خود Game ست می‌شود، پس یک مقدار موقت می‌دهیم
            gamePlayers.add(new Player(client.getUsername(), client.getUsername(), -1));
        }

        // 2. ساختن آبجکت اصلی Game و GameSession
        Game actualGame = new Game(gamePlayers);
        actualGame.startGame(); // آماده‌سازی اولیه بازی
        int gameId = actualGame.getId(); // گرفتن ID منحصربفرد این بازی

        GameSession newGameSession = new GameSession(playersInLobby, actualGame);

        // 3. ثبت کردن بازی جدید و اختصاص gameId به کلاینت‌ها
        activeGames.put(gameId, newGameSession);
        for (ClientHandler client : playersInLobby) {
            client.setGameId(gameId); // <<-- بسیار مهم: هر کلاینت حالا می‌داند در کدام بازی است
        }

        // 4. شروع ترد بازی (Game Loop)
        new Thread(newGameSession).start();

        // 5. اطلاع‌رسانی به کلاینت‌ها و آپدیت لیست لابی‌ها
        targetLobby.setGameStarted(true);
        System.out.println("Game " + gameId + " started in lobby: " + targetLobby.getLobbyName());

        Request gameStartedRequest = new Request(RequestType.GAME_STARTED, "The game is starting now!");
        broadcastMessageToLobby(lobbyId, gameStartedRequest);
        broadcastLobbyList();
    }

    // متد جدید برای هدایت درخواست‌های داخل بازی به سشن مربوطه
    public static void forwardRequestToGameSession(Request request, ClientHandler fromClient) {
        int gameId = fromClient.getGameId();
        if (gameId != -1) {
            GameSession session = activeGames.get(gameId);
            if (session != null) {
                // اینجا بر اساس نوع درخواست، متد مربوطه در سشن را صدا می‌زنیم
                if (request.getType() == RequestType.PlAYER_MOVE && request.getPayload() instanceof PlayerMovePayload) {
                    //session.processMoveRequest((PlayerMovePayload) request.getPayload(), fromClient.getUsername());
                }
                // سایر درخواست‌های بازی اینجا مدیریت می‌شوند
                // else if (request.getType() == RequestType.USE_TOOL) { ... }
            }
        }
    }

    public static void broadcastChatMessageToLobby(String lobbyId, ChatMessage chatMessage) {
        Request chatRequest = new Request(RequestType.LOBBY_CHAT_MESSAGE, chatMessage);
        broadcastMessageToLobby(lobbyId, chatRequest);
    }

    public static List<Lobby> getLobbies() {
        // ... (بدون تغییر)
        synchronized (lobbies) {
            return new ArrayList<>(lobbies);
        }
    }

    private static void broadcastLobbyListInternal() {
        List<Lobby> availableLobbies = lobbies.stream()
                .filter(lobby -> !lobby.isGameStarted())
                .collect(Collectors.toList());
        Request updateRequest = new Request(RequestType.UPDATE_LOBBY_LIST, availableLobbies);
        for (ClientHandler client : clients) {
            if (client.getLobbyId() == null) {
                client.sendMessage(updateRequest);
            }
        }
    }

    public static void broadcastLobbyList() {
        synchronized(lobbies) {
            broadcastLobbyListInternal();
        }
    }

    public static void broadcastMessageToLobby(String lobbyId, Request request) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (lobbyId != null && lobbyId.equals(client.getLobbyId())) {
                    client.sendMessage(request);
                }
            }
        }
    }

    public static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        // TODO: منطق خروج بازیکن از لابی و بازی
        System.out.println("Player disconnected: " + clientHandler.getClientIdentifier());
    }
}