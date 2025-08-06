package com.StardewValley.network.client;

import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.network.shares.message.PlayerMovePayload;
import com.StardewValley.network.shares.message.Request;
import com.StardewValley.network.shares.message.RequestType;
import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// در پکیج کلاینت
public class NetworkClient {
    private ObjectOutputStream out; // To Server
    private ObjectInputStream in;  //From Server
    private Socket socket;
    private GameScreen gameScreen; // <<-- یک رفرنس به گیم اسکرین

    public NetworkClient() {
    }

    public boolean connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // یک ترد جدا برای گوش دادن به پیام‌های سرور
            new Thread(this::listenToServer).start();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // این متد در ترد پس‌زمینه اجرا می‌شود
    private void listenToServer() {
        try {
            while (true) {
                Request serverRequest = (Request) in.readObject();
                // <<-- بسیار مهم: آپدیت UI باید در ترد اصلی LibGDX انجام شود
                Gdx.app.postRunnable(() -> {
                    gameScreen.handleServerUpdate(serverRequest);
                });
            }
        } catch (Exception e) {
            System.out.println("Disconnected from server.");
        }
    }

    // متدهایی که GameScreen برای ارسال درخواست صدا می‌زند
    public void sendMoveRequest(float dx, float dy) {
        // TODO: یک کلاس PlayerMovePayload بسازید که dx و dy را نگه دارد
        sendRequest(new Request(RequestType.PlAYER_MOVE, new PlayerMovePayload(dx, dy)));
    }

    public void sendRequest(Request request) {
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}