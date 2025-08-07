package com.StardewValley.network.client;

import com.StardewValley.Main; // <<-- وارد کردن کلاس اصلی بازی
import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.App; // <<-- وارد کردن مدل App
import com.StardewValley.models.Game;
import com.StardewValley.models.map.FarmType;
import com.StardewValley.network.shares.Lobby;
import com.StardewValley.network.shares.dtos.GameStateDTO;
import com.StardewValley.network.shares.message.*;
import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class NetworkClient {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;

    private final Main main;
    private final App app;

    public NetworkClient() {
        app = App.getApp();
        main = Main.getMain();
    }

    public boolean connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            sendUsername(app.getLoggedInUser().getUserName());
            new Thread(this::listenToServer).start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void listenToServer() {
        try {
            while (true) {
                Request serverRequest = (Request) in.readObject();
                // <<-- بسیار مهم: هر آپدیتی باید در ترد اصلی LibGDX انجام شود -->>
                Gdx.app.postRunnable(() -> handleServerRequest(serverRequest));
            }
        } catch (Exception e) {
            System.out.println("Disconnected from server.");
            // TODO: نمایش پیام قطع اتصال در UI و بازگشت به منوی اصلی
            // Gdx.app.postRunnable(() -> gameMain.showDisconnectionScreen());
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

            case JOIN_LOBBY_RESPONSE:
                if (payload instanceof Boolean) {
                    Boolean response = (Boolean) payload;
                    joinLobby(response);
                }
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
    public void sendUsername(String username) {
        sendRequest(new Request(RequestType.SEND_USERNAME, username));
    }

    // TODO : call after click refresh
    public void sendRefreshLobbiesRequest() {
        sendRequest(new Request(RequestType.REFRESH_LOBBY_LIST, null));
    }

    //TODO : call after click on create lobby and filled details
    public void sendCreateLobbyRequest(String lobbyName, String usernameOfAdmin, boolean isPrivate, boolean isVisibleToAll, String password) {
        sendRequest(new Request(RequestType.CREATE_LOBBY, new LobbyData(lobbyName, usernameOfAdmin, isPrivate, isVisibleToAll, password)));
    }

    //TODO : after click on each lobby or searching lobby by id
    public void sendJoinLobbyRequest(String lobbyId) {
        sendRequest(new Request(RequestType.JOIN_LOBBY, new String[]{lobbyId, app.getLoggedInUser().getUserName()}));
    }

    public void joinLobby(boolean success) {
        if (!success) {
        //TODO  : show error
        } else {
        //TODO  : show lobby window
        }
    }

    // TODO : on button clicked
    public void sendChooseMapRequest(FarmType farmType) {
        sendRequest(new Request(RequestType.CHOOSE_MAP, farmType));
    }

    // Game starting
    // TODO : on button clicked
    public void sendStartGameRequest(String lobbyId) {
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