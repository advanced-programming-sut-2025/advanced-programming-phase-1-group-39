package com.StardewValley.network.client.controllers;


import com.StardewValley.network.server.ServerMain;
import com.StardewValley.network.shares.message.ChatMessage;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientIdentifier;
    private String lobbyId; // <<-- فیلد جدید برای دانستن اینکه کلاینت در کدام لابی است

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.clientIdentifier = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        this.lobbyId = null; // در ابتدا در هیچ لابی نیست
    }

    public String getClientIdentifier() { return clientIdentifier; }
    public String getLobbyId() { return lobbyId; }
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }

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
        } catch (Exception e) {
            ServerMain.removeClient(this);
        }
    }

    private void handleRequest(Request request) {
        switch (request.getType()) {
            case CREATE_LOBBY:
                String lobbyName = (String) request.getPayload();
                ServerMain.createLobby(lobbyName, this);
                break;

            case JOIN_LOBBY:
                String requestedLobbyId = (String) request.getPayload();
                ServerMain.joinLobby(requestedLobbyId, this);
                break;

            case GET_LOBBY_LIST:
                sendMessage(new Request(RequestType.UPDATE_LOBBY_LIST, ServerMain.getLobbies()));
                break;

            // <<-- منطق جدید برای چت --
            case LOBBY_CHAT_MESSAGE:
                if (lobbyId != null) { // فقط اگر در لابی باشد می‌تواند چت کند
                    String messageContent = (String) request.getPayload();
                    ChatMessage chatMessage = new ChatMessage(this.clientIdentifier, messageContent);
                    ServerMain.broadcastMessageToLobby(this.lobbyId, chatMessage);
                }
                break;
        }
    }

    public void sendMessage(Request request) {
        try {
            synchronized (out) {
                out.writeObject(request);
                out.flush();
            }
        } catch (Exception e) {
            System.err.println("Error sending message to " + clientIdentifier);
        }
    }
}