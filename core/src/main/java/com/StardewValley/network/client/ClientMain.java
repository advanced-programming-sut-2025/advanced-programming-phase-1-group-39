package com.StardewValley.network.client;

import com.StardewValley.network.shares.Lobby;
import com.StardewValley.network.shares.message.ChatMessage;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientMain {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static ObjectOutputStream out;
    private static boolean inLobby = false;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to the game server.");
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        Request serverRequest = (Request) in.readObject();
                        handleServerRequest(serverRequest);
                    }
                } catch (Exception e) {
                    System.out.println("\nDisconnected from server.");
                }
            }).start();

            showMenu();

        } catch (Exception e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
        }
    }

    private static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Create Lobby");
            System.out.println("2. Join Lobby");
            System.out.println("3. Refresh Lobby List");
            System.out.println("4. Send Chat Message (if in lobby)");
            System.out.print("Enter command: ");
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    System.out.print("Enter lobby name: ");
                    String lobbyName = scanner.nextLine();
                    sendRequest(new Request(RequestType.CREATE_LOBBY, lobbyName));
                    // <<-- رفع باگ اصلی: سازنده هم باید بداند که در لابی است --
                    inLobby = true;
                    break;
                case "2":
                    System.out.print("Enter Lobby ID to join: ");
                    String lobbyId = scanner.nextLine();
                    sendRequest(new Request(RequestType.JOIN_LOBBY, lobbyId));
                    inLobby = true;
                    break;
                case "3":
                    sendRequest(new Request(RequestType.GET_LOBBY_LIST, null));
                    break;
                case "4":
                    if (inLobby) {
                        System.out.print("Enter message: ");
                        String message = scanner.nextLine();
                        sendRequest(new Request(RequestType.LOBBY_CHAT_MESSAGE, message));
                    } else {
                        System.out.println("You must be in a lobby to chat.");
                    }
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
            }
        }
    }

    private static void sendRequest(Request request) {
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException e) {
            System.err.println("Failed to send request to server: " + e.getMessage());
        }
    }

    private static void handleServerRequest(Request request) {
        switch (request.getType()) {
            case UPDATE_LOBBY_LIST:
                List<Lobby> lobbies = (List<Lobby>) request.getPayload();
                // \r برای پاک کردن خط فعلی ترمینال و چاپ مجدد
                System.out.print("\r" + " ".repeat(50) + "\r");
                System.out.println("--- Available Lobbies (Updated) ---");
                if (lobbies.isEmpty()) {
                    System.out.println("No lobbies available.");
                } else {
                    lobbies.forEach(System.out::println);
                }
                System.out.print("Enter command: ");
                break;

            case LOBBY_CHAT_MESSAGE:
                ChatMessage chatMessage = (ChatMessage) request.getPayload();
                System.out.print("\r" + " ".repeat(50) + "\r");
                System.out.println("[Lobby Chat] " + chatMessage);
                System.out.print("Enter command: ");
                break;
        }
    }
}
