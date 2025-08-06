package com.StardewValley.network.client.controllers;


import com.StardewValley.network.server.ServerMain;
import com.StardewValley.network.shares.message.ChatMessage;
import com.StardewValley.network.shares.message.LobbyData;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;

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


//            sendMessage(new Request(RequestType.UPDATE_LOBBY_LIST, ServerMain.getLobbies()));

            while (true) {
                Request request = (Request) in.readObject();
                handleRequest(request);
            }
        } catch (SocketException e) {
            // قطع ناگهانی
            System.out.println("Client " + getClientIdentifier() + " disconnected abruptly.");
        } catch (Exception e) {
            System.out.println("Error with client " + getClientIdentifier() + ": " + e.getMessage());
        } finally {
            ServerMain.handleDisconnection(this);
        }
    }

    private void handleRequest(Request request) {
        switch (request.getType()) {
            case SEND_USERNAME:
                if (request.getPayload() instanceof String) {
                    String username = (String) request.getPayload();
                    ServerMain.registerNewClient(username, this);
                }
                break;

            case REFRESH_LOBBY_LIST:
                sendMessage(new Request(RequestType.UPDATE_LOBBY_LIST, ServerMain.getLobbies()));
                break;


            case CREATE_LOBBY:
                LobbyData lobbyData = (LobbyData) request.getPayload();
                ServerMain.createLobby(lobbyData, this);
                break;

            case JOIN_LOBBY:
                String requestedLobbyId = (String) request.getPayload();
                ServerMain.joinLobby(requestedLobbyId, username, this);
                break;

            case START_GAME:
                ServerMain.startGame(this);
                break;

            case LOBBY_CHAT_MESSAGE:
                if (lobbyId != null) {
                    String messageContent = (String) request.getPayload();
                    ChatMessage chatMessage = new ChatMessage(this.clientIdentifier, messageContent);
                    ServerMain.broadcastChatMessageToLobby(this.lobbyId, chatMessage);
                }
                break;

            case PlAYER_MOVE:
                if (username != null) {
                    ServerMain.forwardRequestToGameSession(request, this);
                }
                break;

            case PLAYER_REACTION:
                if (gameId != -1) {
                    ServerMain.forwardRequestToGameSession(request, this);
                }
                break;
        }
    }

    public void sendMessage(Request request) {
        try {
            synchronized (out) {
                out.writeUnshared(request);
                out.flush();
            }
        } catch (Exception e) {
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