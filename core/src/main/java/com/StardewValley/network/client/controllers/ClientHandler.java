package com.StardewValley.network.client.controllers;


import com.StardewValley.network.server.ServerMain;
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

    // یک شناسه برای هر کلاینت
    private String clientIdentifier;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.clientIdentifier = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            // به محض اتصال، لیست لابی‌ها را برای کلاینت جدید بفرست
            ServerMain.broadcastLobbyList();

            while (true) {
                Request request = (Request) in.readObject();
                handleRequest(request);
            }
        } catch (Exception e) {
            // در صورت قطع اتصال، از لیست سرور حذف شو
            ServerMain.removeClient(this);
        }
    }

    // پردازش درخواست‌های کلاینت
    private void handleRequest(Request request) {
        switch (request.getType()) {
            case CREATE_LOBBY:
                String lobbyName = (String) request.getPayload();
                ServerMain.createLobby(lobbyName, this);
                break;
            case GET_LOBBY_LIST:
                // لیست لابی‌ها را فقط برای همین کلاینت ارسال می‌کند
                sendMessage(new Request(RequestType.UPDATE_LOBBY_LIST, ServerMain.getLobbies()));
                break;
            // سایر case ها در آینده
        }
    }

    // متد ارسال پیام به این کلاینت
    public void sendMessage(Request request) {
        try {
            out.writeObject(request);
            out.flush();
        } catch (Exception e) {
            System.err.println("Error sending message to " + clientIdentifier);
        }
    }
}