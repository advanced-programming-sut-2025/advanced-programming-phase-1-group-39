package com.StardewValley.server;

import com.StardewValley.client.controllers.ClientHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.net.ServerSocket;

public class ServerMain {
    private static final int PORT = 8080; // پورتی که سرور روی آن گوش می‌دهد
    private static List<ClientHandler> clients = new ArrayList<>(); // لیستی برای نگهداری تمام بازیکنان متصل

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and waiting for players on port " + PORT);

            while (true) {
                // منتظر می‌ماند تا یک کلاینت جدید متصل شود
                Socket clientSocket = serverSocket.accept();
                System.out.println("A new player has connected: " + clientSocket.getInetAddress().getHostAddress());

                // برای هر بازیکن یک ClientHandler جدید در یک Thread مجزا می‌سازد
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
        }
    }
}
