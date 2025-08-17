package com.StardewValley.network.server.controllers;


import com.StardewValley.models.map.FarmType;
import com.StardewValley.models.saveClasses.UserData;
import com.StardewValley.network.server.ServerMain;
import com.StardewValley.network.shares.message.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private String username;
    private Socket clientSocket;
    private ObjectOutputStream out; // To Client
    private ObjectInputStream in;   // From Client
    private String clientIdentifier;
    private String lobbyId;
    private int gameId = -1;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.clientIdentifier = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

        this.lobbyId = null; // در ابتدا در هیچ لابی نیست
    }

    public String getClientIdentifier() { return clientIdentifier; }
    public String getLobbyId() { return lobbyId; }
    public int getGameId() {
        return gameId;
    }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());


            sendMessage(new Request(RequestType.UPDATE_LOBBY_LIST, ServerMain.getLobbies()));

            while (true) {
                Request request = (Request) in.readObject();
                handleRequest(request);
            }
        } catch (SocketException e) {
            // قطع ناگهانی
            System.out.println("Client " + getClientIdentifier() + " disconnected abruptly.");
        } catch (Exception e) {
            e.printStackTrace(); // TODO : check
        } finally {
            ServerMain.handleDisconnection(this);
        }
    }

    private void handleRequest(Request request) {
        if (gameId != -1 && isGameRequest(request.getType())) {
            ServerMain.forwardRequestToGameSession(request, this);
            return;
        }

        switch (request.getType()) {
            case SEND_USER_DATA:
                if (request.getPayload() instanceof UserData userData) {
                    ServerMain.registerNewClient(userData.getUser(), this);
                }
                break;

            case REFRESH_LOBBY_LIST:
                sendMessage(new Request(RequestType.UPDATE_LOBBY_LIST, ServerMain.getLobbies()));
                break;


            case CREATE_LOBBY:
                LobbyData lobbyData = (LobbyData) request.getPayload();
                ServerMain.createLobby(lobbyData, this);
                break;

            case JOIN_PUBLIC_LOBBY:
                String requestedLobbyId = (String) request.getPayload();
                JoinLobbyResponse response = ServerMain.joinLobby(requestedLobbyId, username, this);

                sendMessage(new Request(RequestType.JOIN_LOBBY_RESPONSE, response));
                break;

            case JOIN_PRIVATE_LOBBY:
                String[] payload = (String[]) request.getPayload();
                String id = payload[0];
                String password = payload[1];
                JoinLobbyResponse lobbyResponse = ServerMain.joinLobby(id, password, username, this);

                sendMessage(new Request(RequestType.JOIN_LOBBY_RESPONSE, lobbyResponse));
                break;

            case LEAVE_LOBBY: // یک RequestType جدید برای این کار بسازید
                ServerMain.leaveLobby(this);
                break;

            case CHOOSE_MAP:
                FarmType farmType = (FarmType) request.getPayload();
                ServerMain.setPlayerFarmType(this, farmType);
                break;

            case START_GAME:
                ServerMain.startGame(this);
                break;

            case PLAYER_REACTION:
                if (gameId != -1) {
                    ServerMain.forwardRequestToGameSession(request, this);
                }
                break;

            case PLAYER_MOVE:
                if (username != null) {
                    ServerMain.forwardRequestToGameSession(request, this);
                }
                break;
        }
    }

    private boolean isGameRequest(RequestType type) {
        return type == RequestType.PLAYER_MOVE ||
                type == RequestType.PLAYER_REACTION ||
                type == RequestType.SEND_CHAT_MESSAGE;
    }

    public void sendMessage(Request request) {
        try {
            synchronized (out) {
                out.writeUnshared(request);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error sending message to " + clientIdentifier);
        }
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}