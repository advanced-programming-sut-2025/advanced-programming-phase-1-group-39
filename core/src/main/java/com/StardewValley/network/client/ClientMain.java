package com.StardewValley.network.client;

import com.StardewValley.network.shares.Lobby;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientMain {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static ObjectOutputStream out;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to the game server.");
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Thread برای گوش دادن به پیام‌های سرور
            new Thread(() -> {
                try {
                    while (true) {
                        Request serverRequest = (Request) in.readObject();
                        handleServerRequest(serverRequest);
                    }
                } catch (Exception e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            showMenu();

        } catch (Exception e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
        }
    }

    // نمایش منو به کاربر
    private static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Create Lobby");
            System.out.println("2. Refresh Lobby List");
            System.out.print("Enter command: ");
            String command = scanner.nextLine();

            if (command.startsWith("1")) {
                System.out.print("Enter lobby name: ");
                String lobbyName = scanner.nextLine();
                createLobby(lobbyName);
            }
            // دستورات دیگر
        }
    }

    // ارسال درخواست ایجاد لابی
    private static void createLobby(String lobbyName) {
        Request request = new Request(RequestType.CREATE_LOBBY, lobbyName);
        try {
            out.writeObject(request);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // پردازش پیام‌های دریافتی از سرور
    private static void handleServerRequest(Request request) {
        switch (request.getType()) {
            case UPDATE_LOBBY_LIST:
                List<Lobby> lobbies = (List<Lobby>) request.getPayload();
                System.out.println("\n--- Available Lobbies ---");
                if (lobbies.isEmpty()) {
                    System.out.println("No lobbies available.");
                } else {
                    for (Lobby lobby : lobbies) {
                        System.out.println(lobby.toString());
                    }
                }
                System.out.print("Enter command: "); // برای اینکه ظاهر ترمینال بهم نریزد
                break;
            // سایر case ها
        }
    }
}
