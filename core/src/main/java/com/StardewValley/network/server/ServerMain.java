package com.StardewValley.network.server;

import com.StardewValley.models.*;
import com.StardewValley.models.map.FarmType;
import com.StardewValley.models.services.AppDataManager;
import com.StardewValley.network.server.controllers.ClientHandler;
import com.StardewValley.network.shares.Lobby;
import com.StardewValley.network.shares.message.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class ServerMain {
    public static final int PORT = 5050;
    public static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    public static final List<Lobby> lobbies = Collections.synchronizedList(new ArrayList<>());
    public static final Map<Integer, GameSession> activeGames = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        changePlaceOfData();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            System.out.println("Loading server app ...");
            AppDataManager.loadUsersFromFile();
            System.out.println("server app Loaded. " + App.getApp().getUsers().size() + " users loaded.");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New player connected: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void changePlaceOfData() {
        AppDataManager.USERS_DATA_PATH = "serverData/users.json";
    }

    public static synchronized void registerNewClient(User user, ClientHandler handler) {
        handler.setUsername(user.getUserName());
        if (App.getApp().getUserByUsername(user.getUserName()) == null) {
            App.getApp().addUser(user);
        } else {
            App.getApp().updateUser(user);
        }
        // saving app for server
        AppDataManager.saveUsers(App.getApp().getUsers(), App.getApp().getLoggedInUser());
        broadcastOnlineUserList();
    }


        // متد createLobby با دریافت نام کاربری ادمین
    public static void createLobby(LobbyData lobbyData, ClientHandler adminHandler) {
        Lobby newLobby = new Lobby(lobbyData.lobbyName, lobbyData.adminUsername, lobbyData.isPrivate, lobbyData.isVisibleToAll);
        synchronized (lobbies) {
            lobbies.add(newLobby);
            adminHandler.setLobbyId(newLobby.getId());
            System.out.println("New lobby created: " + newLobby + " by " + adminHandler.getUsername());

            broadcastLobbyListInternal();
        }
    }

    // متد joinLobby با دریافت نام کاربری
    public static Result joinLobby(String lobbyId, String username, ClientHandler joiningClient) {
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getId().equals(lobbyId) && !lobby.isFull()) {
                    boolean success = lobby.addPlayer(username); // بازیکن با نام کاربری اضافه می‌شود
                    if (success) {
                        joiningClient.setUsername(username); // نام کاربری را در هندلر ذخیره کن
                        joiningClient.setLobbyId(lobby.getId());
                        broadcastLobbyListInternal();
                        return new Result(true,
                                "Player " + username + " joined lobby. New state: " + lobby);
                    } else {
                        return new Result(false, "Failed to join lobby (Admin: " + username + ")");
                    }
                }
            }
            return new Result(false, "Lobby with ID " + lobbyId + " not found or is full.");
        }
    }

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
        if (targetLobby.getPlayerCount() < 1) {
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
        System.out.println("Game " + gameId + " started in lobby: " + targetLobby.getName());

        Request gameStartedRequest = new Request(RequestType.GAME_STARTED, actualGame);
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
                if (request.getType() == RequestType.PLAYER_MOVE && request.getPayload() instanceof PlayerMovePayload) {
                    //session.processMoveRequest((PlayerMovePayload) request.getPayload(), fromClient.getUsername());
                } else if (request.getType() == RequestType.PLAYER_REACTION && request.getPayload() instanceof PlayerReactionPayload) {
                    session.processReactionRequest((PlayerReactionPayload) request.getPayload(), fromClient.getUsername());
                }
            }
        }
    }


    public static void broadcastChatMessageToLobby(String lobbyId, PublicChatMessage chatMessage) {
        if (chatMessage instanceof PrivateChatMessage) {
            PrivateChatMessage privateMessage = (PrivateChatMessage) chatMessage;
            Request chatRequest = new Request(RequestType.LOBBY_PRIVATE_CHAT_MESSAGE, privateMessage);

            ClientHandler senderHandler = findClientByUsername(privateMessage.getSenderName());
            ClientHandler recipientHandler = findClientByUsername(privateMessage.getRecipientName());

            if (senderHandler != null) {
                senderHandler.sendMessage(chatRequest);
            }
            if (recipientHandler != null) {
                recipientHandler.sendMessage(chatRequest);
            }

        } else {
            Request chatRequest = new Request(RequestType.LOBBY_PUBLIC_CHAT_MESSAGE, chatMessage);
            broadcastMessageToLobby(lobbyId, chatRequest);
        }
    }

    private static ClientHandler findClientByUsername(String username) {
        synchronized (clients) {
            return clients.stream()
                    .filter(c -> username.equals(c.getUsername()))
                    .findFirst()
                    .orElse(null);
        }
    }

    public static List<Lobby> getLobbies() {
        synchronized (lobbies) {
            return new ArrayList<>(lobbies);
        }
    }

    public static Lobby getLobbyById(String id) {
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getId().equals(id)) {
                    return lobby;
                }
            }
        }
        return null;
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


    public static void handleDisconnection(ClientHandler handler) {
        if (handler.getGameId() == -1) {
            removeClient(handler);
            return;
        }

        // اگر بازیکن در یک بازی فعال بود، منطق داکیومنت را اجرا کن
        GameSession session = activeGames.get(handler.getGameId());
        if (session != null) {
            System.out.println("Player " + handler.getUsername() + " disconnected from game " + handler.getGameId() + ". Starting 2-minute timer.");
            // به سشن بازی می‌گوییم که این بازیکن قطع شده و تایمر را شروع کند
            session.onPlayerDisconnected(handler.getUsername());
        } else {
            // اگر بازی‌ای پیدا نشد (حالت نادر)، فقط حذفش کن
            removeClient(handler);
        }
    }

    public static void removeClient(ClientHandler handler) {
        clients.remove(handler);
        System.out.println("Client " + handler.getClientIdentifier() + " removed.");
        broadcastOnlineUserList();
    }

    private static void broadcastOnlineUserList() {
        synchronized (clients) {
            List<String> clientsUsernames = new ArrayList<>();
            for (ClientHandler client : clients) {
                clientsUsernames.add(client.getUsername());
            }

            for (ClientHandler client : clients) {
                client.sendMessage(new Request(RequestType.UPDATE_ONLINE_USERS, clientsUsernames));
            }
        }
    }


    public static void setPlayerFarmType(ClientHandler clientHandler, FarmType farmType) {
        String lobbyId = clientHandler.getLobbyId();
        Lobby lobby = getLobbyById(lobbyId);
        if (lobby != null) {
            lobby.setMapOfPlayer(clientHandler.getUsername(), farmType);
        }
    }
}