package com.StardewValley.network.server;

import com.StardewValley.network.client.controllers.ClientHandler;
import com.StardewValley.network.shares.Lobby;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerMain {
    private static final int PORT = 8080;
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static final List<Lobby> lobbies = Collections.synchronizedList(new ArrayList<>()); // لیست لابی‌ها

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

    // متدی برای ایجاد لابی جدید
    public static void createLobby(String lobbyName, ClientHandler adminHandler) {
        // فرض می‌کنیم هر کلاینت یک نام کاربری دارد که بعداً اضافه می‌شود. فعلا از آدرسش استفاده می‌کنیم.
        String adminName = adminHandler.getClientIdentifier();
        Lobby newLobby = new Lobby(lobbyName, adminName);
        lobbies.add(newLobby);
        System.out.println("New lobby created: " + lobbyName);
        broadcastLobbyList(); // لیست جدید را برای همه ارسال کن
    }

    // متدی برای ارسال لیست لابی‌ها به تمام کلاینت‌ها
    public static void broadcastLobbyList() {
        Request updateRequest = new Request(RequestType.UPDATE_LOBBY_LIST, new ArrayList<>(lobbies));
        for (ClientHandler client : clients) {
            client.sendMessage(updateRequest);
        }
    }

    // متدی برای حذف یک کلاینت
    public static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        // منطق حذف بازیکن از لابی هم باید اینجا یا در ClientHandler اضافه شود
        System.out.println("Player disconnected: " + clientHandler.getClientIdentifier());
    }

    public static List<Lobby> getLobbies() {
        return lobbies;
    }
}