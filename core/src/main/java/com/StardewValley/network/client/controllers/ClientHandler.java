package com.StardewValley.network.client.controllers;


import com.StardewValley.network.server.ServerMain;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            // این خطا معمولا وقتی رخ می‌دهد که کلاینت به طور ناگهانی قطع می‌شود
            System.out.println("Client " + getClientIdentifier() + " disconnected abruptly.");
        } catch (Exception e) {
            // سایر خطاها
            System.out.println("Error with client " + getClientIdentifier() + ": " + e.getMessage());
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
            case CREATE_LOBBY:
                String lobbyName = (String) request.getPayload();
                ServerMain.createLobby(lobbyName, username, this);
                break;

            case JOIN_LOBBY:
                String requestedLobbyId = (String) request.getPayload();
                ServerMain.joinLobby(requestedLobbyId, username, this);
                break;

            case GET_LOBBY_LIST:
                sendMessage(new Request(RequestType.UPDATE_LOBBY_LIST, ServerMain.getLobbies()));
                break;

            case START_GAME:
                ServerMain.startGame(this);
                break;

        }
    }

    private boolean isGameRequest(RequestType type) {
        return type == RequestType.PlAYER_MOVE ||
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