package com.StardewValley.client.controllers;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private List<ClientHandler> clients;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.clientSocket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
                // به طور مداوم منتظر دریافت پیام از کلاینت می‌ماند
                Object message = in.readObject();
                System.out.println("Received message from a player: " + message);

                // در اینجا منطق اصلی بازی روی سرور پیاده‌سازی می‌شود
                // برای مثال، اگر پیام درخواست حرکت بود، موقعیت بازیکن را آپدیت کرده
                // و به همه بازیکنان دیگر اطلاع می‌دهد.
            }
        } catch (Exception e) {
            System.err.println("Player disconnected: " + e.getMessage());
        } finally {
            // در صورت قطع اتصال، بازیکن از لیست حذف می‌شود
            clients.remove(this);
        }
    }
}