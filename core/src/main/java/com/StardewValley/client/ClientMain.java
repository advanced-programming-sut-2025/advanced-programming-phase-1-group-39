package com.StardewValley.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    private static final String SERVER_IP = "127.0.0.1"; // IP سرور (این آدرس برای تست روی یک سیستم است)
    private static final int SERVER_PORT = 8080; // پورت سرور

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to the game server.");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // یک Thread برای گوش دادن به پیام‌های سرور
            new Thread(() -> {
                try {
                    while (true) {
                        Object serverMessage = in.readObject();
                        System.out.println("Message from server: " + serverMessage);
                        // در اینجا باید رابط کاربری بازی را بر اساس پیام سرور آپدیت کنید
                    }
                } catch (Exception e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            // حلقه اصلی برای ارسال پیام از کلاینت به سرور
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String messageToSend = scanner.nextLine();
                out.writeObject(messageToSend);
                out.flush();
            }

        } catch (Exception e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
        }
    }
}
