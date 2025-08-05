package com.StardewValley.network.server;

import com.StardewValley.network.client.controllers.ClientHandler;
import com.StardewValley.network.shares.Lobby;
import com.StardewValley.network.shares.message.ChatMessage;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ServerMain {
    private static final int PORT = 8080;
    // <<-- استفاده از volatile برای تضمین Visibility بین Thread ها --
    private static volatile List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static volatile List<Lobby> lobbies = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        // ... (بدون تغییر)
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

    public static void createLobby(String lobbyName, ClientHandler adminHandler) {
        String adminName = adminHandler.getClientIdentifier();
        Lobby newLobby = new Lobby(lobbyName, adminName);

        // <<-- عملیات در یک بلاک synchronized واحد --
        synchronized (lobbies) {
            lobbies.add(newLobby);
            adminHandler.setLobbyId(newLobby.getId());
            System.out.println("New lobby created: " + newLobby + " by " + adminName);
            // ارسال لیست بلافاصله پس از تغییر و در همان بلاک
            broadcastLobbyListInternal();
        }
    }

    public static void joinLobby(String lobbyId, ClientHandler joiningClient) {
        // <<-- کل منطق در یک بلاک synchronized --
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getId().equals(lobbyId)) {
                    boolean success = lobby.addPlayer(joiningClient.getClientIdentifier());
                    if (success) {
                        joiningClient.setLobbyId(lobby.getId());
                        System.out.println("Player " + joiningClient.getClientIdentifier() + " joined lobby. New state: " + lobby);
                        // ارسال لیست بلافاصله پس از تغییر و در همان بلاک
                        broadcastLobbyListInternal();
                    } else {
                        System.out.println("Failed to join lobby for player " + joiningClient.getClientIdentifier());
                    }
                    return;
                }
            }
            System.err.println("Lobby with ID " + lobbyId + " not found.");
        }
    }

    /**
     * این متد باید همیشه از داخل یک بلاک synchronized (lobbies) فراخوانی شود
     */
    private static void broadcastLobbyListInternal() {
        List<Lobby> lobbiesCopy = new ArrayList<>(lobbies);
        System.out.println("DEBUG: Broadcasting " + lobbiesCopy.size() + " lobbies. Details: " + lobbiesCopy);
        Request updateRequest = new Request(RequestType.UPDATE_LOBBY_LIST, lobbiesCopy);

        // این بلاک synchronized خودش thread-safe است
        for (ClientHandler client : clients) {
            client.sendMessage(updateRequest);
        }
    }

    // متد عمومی که می‌تواند از خارج فراخوانی شود
    public static void broadcastLobbyList() {
        synchronized(lobbies) {
            broadcastLobbyListInternal();
        }
    }


    public static void broadcastMessageToLobby(String lobbyId, ChatMessage chatMessage) {
        // ... (بدون تغییر)
        Request chatRequest = new Request(RequestType.LOBBY_CHAT_MESSAGE, chatMessage);
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (lobbyId.equals(client.getLobbyId())) {
                    client.sendMessage(chatRequest);
                }
            }
        }
    }

    public static List<Lobby> getLobbies() {
        // ... (بدون تغییر)
        synchronized (lobbies) {
            return new ArrayList<>(lobbies);
        }
    }

    public static void removeClient(ClientHandler clientHandler) {
        // ... (بدون تغییر)
        clients.remove(clientHandler);
        // TODO: باید منطق حذف بازیکن از لابی هنگام خروج نیز اضافه شود
        System.out.println("Player disconnected: " + clientHandler.getClientIdentifier());
    }
}