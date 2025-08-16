package com.StardewValley.network.client;

import com.StardewValley.Main; // <<-- وارد کردن کلاس اصلی بازی
import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.App; // <<-- وارد کردن مدل App
import com.StardewValley.models.Game;
import com.StardewValley.models.Result;
import com.StardewValley.models.User;
import com.StardewValley.models.map.FarmType;
import com.StardewValley.models.saveClasses.UserData;
import com.StardewValley.network.shares.Lobby;
import com.StardewValley.network.shares.dtos.GameStateDTO;
import com.StardewValley.network.shares.message.*;
import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkClient {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;

    private final Main main;
    private final App app;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);


    public NetworkClient() {
        app = App.getApp();
        main = Main.getMain();
    }

    public void connect(String ip, int port) {
        new Thread(() -> {
            try {
                socket = new Socket(ip, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                // به ترد اصلی UI بگو که اتصال موفق بود
                Gdx.app.postRunnable(() -> main.onConnectionSuccess());

                // حلقه شنونده را شروع کن
                listenToServer();
            } catch (IOException e) {
                Gdx.app.postRunnable(() -> main.onConnectionFailed("Could not connect to server."));
            }
        }).start();
    }

    private void listenToServer() {
        isRunning.set(true);
        try {
            while (isRunning.get()) { // <<-- حلقه به پرچم وابسته است
                Request serverRequest = (Request) in.readObject();
                Gdx.app.postRunnable(() -> handleServerRequest(serverRequest));
            }
        } catch (Exception e) {
            // اگر در حین اجرا خطایی رخ دهد (مثل قطع شدن سرور)
            if (isRunning.get()) { // فقط اگر خودمان قطع نکرده باشیم، پیام خطا بده
                System.out.println("Disconnected from server.");
                Gdx.app.postRunnable(() -> main.onDisconnectedFromServer("Connection lost to the server."));
            }
        } finally {
            isRunning.set(false);
        }
    }

    public void disconnect() {
        if (isRunning.compareAndSet(true, false)) {
            try {
                // بستن سوکت باعث می‌شود که ترد listenToServer یک Exception دریافت کرده و خارج شود.
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    // Request : پیام دریافتی از سرور
    private void handleServerRequest(Request request) {
        Object payload = request.getPayload();

        switch (request.getType()) {
            case UPDATE_ONLINE_USERS:
                if (payload instanceof List) {
                    List<String> usernames = (List<String>) payload;
                    app.setOnlineUsers(usernames);
                }
                break;

            case UPDATE_LOBBY_LIST:
                if (payload instanceof List) {
                    List<Lobby> lobbies = (List<Lobby>) payload;
                    app.setAvailableLobbies(lobbies);
                }
                break;
            case CREATE_LOBBY_SUCCESS:
                Lobby currentLobby = (Lobby) payload;
                app.setCurrentLobby(currentLobby);
                main.onJoinLobbyResponse(new Result(true, ""));
                break;
            case JOIN_LOBBY_RESPONSE:
                if (payload instanceof JoinLobbyResponse) {
                    JoinLobbyResponse response = (JoinLobbyResponse) payload;
                    main.onJoinLobbyResponse(response.joiningResult);
                    if (response.joiningResult.success())
                        app.setCurrentLobby(response.joinedLobby);
                }
                break;

            case YOU_WERE_KICKED:
                String reason = (String) request.getPayload();
                disconnect();
                main.onDisconnectedFromServer(reason);
                break;

            case GAME_START_FAILED:
                String errorMessage = (String) payload;
                // TODO: یک متد برای نمایش خطاهای عمومی در UI بسازید
                // gameMain.showErrorPopup(errorMessage);
                break;

            case GAME_STARTED:
                if (payload instanceof Game) {
                    Game receivedGame = (Game) payload;
                    app.setCurrentGame(receivedGame);

                    main.switchScreen(new GameScreen());
                }
                break;

            case LOBBY_PUBLIC_CHAT_MESSAGE:
                if (payload instanceof PublicChatMessage) {
                    PublicChatMessage receivedMessage = (PublicChatMessage) payload;
                    // TODO : show message in user public message box
                }
                break;

            case LOBBY_PRIVATE_CHAT_MESSAGE:
                if (payload instanceof PrivateChatMessage) {
                    PrivateChatMessage receivedMessage = (PrivateChatMessage) payload;
                    // TODO : show message in user private message box (only by this)
                }
                break;



            case UPDATE_GAME_STATE:
                // این پیام فقط زمانی که در GameScreen هستیم معنا دارد
                if (payload instanceof GameStateDTO) {
                    // مستقیماً به app می‌گوییم که بازی‌اش را آپدیت کند
//                    app.getCurrentGame().updateFromDTO((GameStateDTO) payload);
                }
                break;
        }
    }

    public void sendRequest(Request request) {
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // send messages
    public void sendUserDataToServer(User user) {
        sendRequest(new Request(RequestType.SEND_USER_DATA, new UserData(user)));
    }

    // call after click refresh
    public void sendRefreshLobbiesRequest() {
        sendRequest(new Request(RequestType.REFRESH_LOBBY_LIST, null));
    }

    // call after click on create lobby and filled details
    public void sendCreateLobbyRequest(String lobbyName, String usernameOfAdmin, boolean isPrivate, boolean isVisibleToAll, String password) {
        sendRequest(new Request(RequestType.CREATE_LOBBY, new LobbyData(lobbyName, usernameOfAdmin, isPrivate, isVisibleToAll, password)));
    }

    // after  on each lobby or searching lobby by id
    public void sendJoinLobbyRequest(String lobbyId) {
        sendRequest(new Request(RequestType.JOIN_LOBBY, lobbyId));
    }

    // TODO : on button clicked
    public void sendChooseMapRequest(FarmType farmType) {
        sendRequest(new Request(RequestType.CHOOSE_MAP, farmType));
    }

    // Game starting
    // TODO : on button clicked
    public void sendStartGameRequest() {
        sendRequest(new Request(RequestType.START_GAME, null));
    }

    // TODO : after message in game
    public void sendChatMessagePublicRequest(String message) {
        String messageContent = message;
        Request request = new Request(RequestType.LOBBY_PUBLIC_CHAT_MESSAGE, messageContent);
        sendRequest(request);
    }

    // TODO : after private message in game
    public void sendChatMessagePrivateRequest(String message, String reciever) {
        Request request = new Request(RequestType.LOBBY_PRIVATE_CHAT_MESSAGE, new String[]{message, reciever});
        sendRequest(request);
    }

    // in game
    // TODO : after reaction in game
    public void sendReactionRequest(String reaction) {
        PlayerReactionPayload payload = new PlayerReactionPayload(reaction);
        Request request = new Request(RequestType.PLAYER_REACTION, payload);
        sendRequest(request);
    }
}