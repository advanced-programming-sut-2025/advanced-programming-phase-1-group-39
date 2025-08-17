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
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            System.out.println("Loading server app ...");
            AppDataManager.loadAppForServer();
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


    public static synchronized void registerNewClient(User user, ClientHandler handler) {
        handler.setUsername(user.getUserName());
        if (App.getApp().getUserByUsername(user.getUserName()) == null) {
            App.getApp().addUser(user);
        } else {
            App.getApp().updateUser(user);
        }
        // saving app for server
        AppDataManager.saveAppForServer();
        broadcastOnlineUserList();
    }

    public static void createLobby(LobbyData lobbyData, ClientHandler adminHandler) {
        Lobby newLobby = new Lobby(lobbyData.lobbyName, lobbyData.adminUsername, lobbyData.isPrivate, lobbyData.password, lobbyData.isVisibleToAll);
        synchronized (lobbies) {
            lobbies.add(newLobby);
            adminHandler.setLobbyId(newLobby.getId());
            System.out.println("New lobby created: " + newLobby + " by " + adminHandler.getUsername());

            adminHandler.sendMessage(new Request(RequestType.CREATE_LOBBY_SUCCESS, newLobby));
        }
    }

    // متد joinLobby با دریافت نام کاربری
    public static JoinLobbyResponse joinLobby(String lobbyId, String username, ClientHandler joiningClient) {
        synchronized (lobbies) {
            Result res;
            for (Lobby lobby : lobbies) {
                if (lobby.getId().equals(lobbyId) && !lobby.isFull()) {
                    boolean success = lobby.addPlayer(username);
                    if (success) {
                        joiningClient.setUsername(username);
                        joiningClient.setLobbyId(lobby.getId());
                        broadcastLobbyListInternal();
                        res = new Result(true,
                                "Player " + username + " joined lobby " + lobby);
                        return new JoinLobbyResponse(res, lobby);
                    } else {
                        res = new Result(false, "Failed to join lobby (Admin: " + username + ")");
                        return new JoinLobbyResponse(res, null);
                    }
                }
            }
            res = new Result(false, "Lobby with ID " + lobbyId + " not found or is full.");
            return new JoinLobbyResponse(res, null);
        }
    }

    public static JoinLobbyResponse joinLobby(String lobbyId, String password, String username, ClientHandler joiningClient) {
        synchronized (lobbies) {
            Result res;
            for (Lobby lobby : lobbies) {
                if (lobby.getId().equals(lobbyId) && !lobby.isFull()) {
                    if (!lobby.getPassword().equals(password)) {
                        res = new Result(false, "The password does not match.");
                        return new JoinLobbyResponse(res, null);
                    }

                    boolean success = lobby.addPlayer(username);
                    if (success) {
                        joiningClient.setUsername(username);
                        joiningClient.setLobbyId(lobby.getId());
                        broadcastLobbyListInternal();
                        res = new Result(true,
                                "Player " + username + " joined lobby " + lobby);
                        return new JoinLobbyResponse(res, lobby);
                    } else {
                        res = new Result(false, "Failed to join lobby (Admin: " + username + ")");
                        return new JoinLobbyResponse(res, null);
                    }
                }
            }
            res = new Result(false, "Lobby with ID " + lobbyId + " not found or is full.");
            return new JoinLobbyResponse(res, null);
        }
    }

    public static void leaveLobby(ClientHandler leavingClient) {
        String username = leavingClient.getUsername();
        String lobbyId = leavingClient.getLobbyId();

        // ۱. بررسی اولیه: آیا بازیکن اصلاً در لابی هست؟
        if (lobbyId == null || username == null) {
            System.err.println("Attempted to leave lobby but client was not in one.");
            return;
        }

        Lobby targetLobby = getLobbyById(lobbyId);

        if (targetLobby == null) {
            System.err.println("Lobby " + lobbyId + " not found for leaving client " + username);
            leavingClient.setLobbyId(null); // وضعیت کلاینت را تصحیح کن
            return;
        }

        boolean wasAdmin = targetLobby.getAdmin().equals(username);

        // ۲. بازیکن را از لیست لابی حذف کن
        targetLobby.removePlayer(username);
        System.out.println("Player " + username + " removed from lobby " + lobbyId);

        // ۳. وضعیت خود کلاینت را آپدیت کن
        leavingClient.setLobbyId(null);
        // (اختیاری) می‌توانید یک پیام تایید برای خود بازیکن بفرستید
        leavingClient.sendMessage(new Request(RequestType.LEAVE_LOBBY_SUCCESS, "You have left the lobby."));

        // ۴. منطق اصلی بر اساس اینکه چه کسی خارج شده
        if (wasAdmin) {
            // اگر ادمین خارج شده...
            if (targetLobby.getPlayerCount() > 0) {
                // ...و هنوز بازیکنی هست، نفر اول را به عنوان ادمین جدید انتخاب کن
                String newAdmin = targetLobby.getPlayers().get(0);
                // targetLobby.setAdmin(newAdmin); // اگر فیلد admin را non-final کردید
                System.out.println("Admin left. New admin for lobby " + lobbyId + " is " + newAdmin);
                // به بازیکنان داخل لابی، وضعیت جدید لابی را اطلاع بده
                broadcastLobbyStateUpdate(targetLobby);
            } else {
                // ...و هیچکس نمانده، لابی را به طور کامل حذف کن
                synchronized (lobbies) {
                    lobbies.remove(targetLobby);
                }
                System.out.println("Lobby " + lobbyId + " closed as the last player (admin) left.");
            }
        } else {
            // اگر یک بازیکن عادی خارج شده، فقط وضعیت لابی را برای بقیه آپدیت کن
            broadcastLobbyStateUpdate(targetLobby);
        }

        // ۵. در نهایت، لیست کلی لابی‌ها را برای همه کسانی که بیرون هستند، آپدیت کن
        broadcastLobbyList();
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
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client.getLobbyId() == null) {
                    client.sendMessage(updateRequest);
                }
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

    private static void broadcastLobbyStateUpdate(Lobby lobby) {
        Request updateRequest = new Request(RequestType.UPDATE_LOBBY_STATE, lobby);
        broadcastMessageToLobby(lobby.getId(), updateRequest);
    }


    public static void handleDisconnection(ClientHandler handler) {
        if (handler.getGameId() == -1) {
            removeClient(handler);
            return;
        }

        GameSession session = activeGames.get(handler.getGameId());
        if (session != null) {
            System.out.println("Player " + handler.getUsername() + " disconnected from game " + handler.getGameId() + ". Starting 2-minute timer.");
            session.onPlayerDisconnected(handler.getUsername());
        } else {
            removeClient(handler);
        }
    }

    public static void removeClient(ClientHandler handler) {
        handler.sendMessage(new Request(RequestType.YOU_WERE_KICKED, "You have been removed from the server."));
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