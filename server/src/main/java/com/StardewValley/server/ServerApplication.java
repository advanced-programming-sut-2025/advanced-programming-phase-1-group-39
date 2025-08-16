package com.StardewValley.server;

import com.StardewValley.network.server.controllers.ClientHandler;
import com.StardewValley.network.server.ServerMain;
import com.badlogic.gdx.ApplicationListener;

import com.StardewValley.models.App;
import com.StardewValley.models.services.AppDataManager;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication implements ApplicationListener {

    private ServerSocket serverSocket;
    private boolean isRunning = true;

    @Override
    public void create() {
        System.out.println("Server Application creating...");

        // ۱. بارگذاری داده‌های اولیه (حالا Gdx.files کار می‌کند)
        System.out.println("Loading server app...");
        AppDataManager.loadApp();
        System.out.println("Server app Loaded. " + App.getApp().getUsers().size() + " users loaded.");

        // ۲. راه‌اندازی سوکت در یک ترد جدید تا برنامه قفل نشود
        new Thread(this::startSocketListening).start();
    }

    private void startSocketListening() {
        try {
            // از فیلدهای استاتیک ServerMain استفاده می‌کنیم
            serverSocket = new ServerSocket(ServerMain.PORT);
            System.out.println("Socket is listening on port " + ServerMain.PORT);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New player connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                ServerMain.clients.add(clientHandler); // اضافه کردن به لیست استاتیک
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            if (isRunning) {
                System.err.println("Server socket error: " + e.getMessage());
            }
        }
    }

    @Override
    public void render() {
        // این حلقه برای منطق‌های دوره‌ای سرور (مثل آپدیت بازی‌ها) استفاده می‌شود
        // فعلا خالی می‌ماند
    }

    @Override
    public void dispose() {
        // این متد هنگام خاموش شدن سرور صدا زده می‌شود
        System.out.println("Server shutting down.");
        isRunning = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) { /* ignore */ }

        // ذخیره نهایی داده‌ها
        AppDataManager.saveApp();
        System.out.println("Server data saved.");
    }

    // متدهای دیگر که نیازی به تغییر ندارند
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
}