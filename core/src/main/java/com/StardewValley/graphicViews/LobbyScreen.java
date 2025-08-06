package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;

public class LobbyScreen implements Screen {
    private Stage stage;
    private Skin skin;

    // عناصر UI
    private Table lobbyListTable;
    private ScrollPane scrollPane;

    public LobbyScreen(/* NetworkClient networkClient */) {
        stage = new Stage(new FitViewport(1920, 1080));
        skin = GameAssetManager.skin; // فرض می‌کنیم Skin شما به صورت استاتیک در دسترس است

        setupUI();
    }

    private void setupUI() {
        // استفاده از Stack برای قرار دادن پس‌زمینه در لایه زیرین و جدول اصلی در لایه رویی
        Stack stack = new Stack();
        stack.setFillParent(true);
        stage.addActor(stack);

        // 1. لایه پس‌زمینه
        stack.add(new Image(GameAssetManager.pregameBackground));

        // 2. لایه UI اصلی
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stack.add(rootTable);

        // --- دکمه بازگشت در بالا-چپ ---
        TextButton backButton = new TextButton("Back", skin);
        rootTable.add(backButton).width(150).top().left();

        rootTable.add().expandX();
        rootTable.row();


        Table contentTable = new Table();
        rootTable.add(contentTable).colspan(2).expandY().center().padBottom(50);

        // آیکون و عنوان
        contentTable.add(new Image(GameAssetManager.titleImage)).width(800).padBottom(10).row();
        Label header = new Label("Lobbies", skin);
        header.setAlignment(Align.center);
        header.setFontScale(2f);
        contentTable.add(header).padBottom(30).row();

        // --- جدول کنترل‌ها (جستجو، رفرش و دکمه‌های دیگر) ---
        Table controlsTable = new Table();
        contentTable.add(controlsTable).padBottom(20).row();

        TextField searchField = new TextField("", skin);
        searchField.setMessageText("Search by Lobby Name...");
        TextButton onlineUsersButton = new TextButton("Online Users", skin);

        // ستون چپ کنترل‌ها
        Table leftControls = new Table();
        leftControls.add(onlineUsersButton).width(400).pad(10).row();
        leftControls.add(searchField).width(400).pad(10);

        TextButton refreshButton = new TextButton("Refresh", skin);
        TextButton createLobbyButton = new TextButton("Create New Lobby", skin);

        // ستون راست کنترل‌ها
        Table rightControls = new Table();
        rightControls.add(createLobbyButton).width(450).pad(10).row();
        rightControls.add(refreshButton).width(300).pad(10);

        controlsTable.add(leftControls).padRight(20);
        controlsTable.add(rightControls).padLeft(20);

//        // --- لیست لابی‌ها در ScrollPane ---
        lobbyListTable = new Table();
        scrollPane = new ScrollPane(lobbyListTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // فقط اسکرول عمودی

        // grow() باعث می‌شود این بخش تمام فضای عمودی و افقی باقیمانده را بگیرد
        contentTable.add(scrollPane).grow().width(820).row();
//
//        // --- اضافه کردن Listener ها ---
        addListeners(backButton, refreshButton, createLobbyButton);
        stage.setDebugAll(true);
    }

    private void addListeners(TextButton backButton, TextButton refreshButton, TextButton createLobbyButton) {
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().switchScreen(new MainMenuScreen());
            }
        });

        refreshButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Refreshing lobbies...");
                populateLobbyList(createDummyLobbies());
            }
        });

        createLobbyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: باز کردن پنجره ساخت لابی جدید
                System.out.println("Create lobby window should open...");
            }
        });
    }

    // این متد لیست لابی‌ها را از سرور دریافت و در جدول نمایش می‌دهد
    public void populateLobbyList(List<Lobby> lobbies) {
        lobbyListTable.clear();
        if (lobbies.isEmpty()) {
            lobbyListTable.add(new Label("No active lobbies found.", skin));
            return;
        }

        for (Lobby lobby : lobbies) {
            String lobbyText = String.format("%s (%d/4)", lobby.getName(), lobby.getPlayerCount());
            TextButton lobbyButton = new TextButton(lobbyText, skin);
//            if (lobby.isPrivate()) {
//                lobbyButton.getImageCell().padLeft(10);
//                lobbyButton.add(new Image(new Texture(Gdx.files.internal("icons/lock_icon.png")))).size(32,32).padLeft(10); // آیکون قفل برای لابی خصوصی
//            }

            lobbyButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (lobby.isPrivate()) {
                        showPasswordDialog(lobby);
                    } else {
                        System.out.println("Joining public lobby: " + lobby.getName());
                        showJoinedLobbyWindow(lobby, "Player1"); // فرض می‌کنیم بازیکن فعلی ادمین است
                    }
                }
            });

            lobbyListTable.add(lobbyButton).fillX().height(80).pad(5).row();
        }
    }

    private void showPasswordDialog(Lobby lobby) {
        Dialog passwordDialog = new Dialog("Enter Password", skin, "dialog") {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    TextField passwordField = findActor("passwordField");
                    System.out.println("Attempting to join private lobby with password...");
                    // TODO: ارسال درخواست Join به سرور با رمز عبور
                    // بعد از تایید سرور، پنجره لابی باز می‌شود
                    showJoinedLobbyWindow(lobby, "Player1"); // نام بازیکن فعلی
                }
            }
        };
        passwordDialog.text("Lobby \"" + lobby.getName() + "\" requires a password:");
        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setName("passwordField");
        passwordDialog.getContentTable().row();
        passwordDialog.getContentTable().add(passwordField).width(300).pad(20);
        passwordDialog.button("Join", true);
        passwordDialog.button("Cancel", false);
        passwordDialog.show(stage);
    }

    private void showJoinedLobbyWindow(Lobby lobby, String currentPlayerName) {
        // ابتدا پنجره‌های قبلی را می‌بندیم (اگر وجود داشته باشند)
        Actor oldWindow = stage.getRoot().findActor("joinedLobbyWindow");
        if(oldWindow != null) oldWindow.remove();

        Window joinedWindow = new Window("Lobby: " + lobby.getName(), skin);
        joinedWindow.setName("joinedLobbyWindow");
        joinedWindow.setSize(800, 900);
        joinedWindow.setPosition(stage.getWidth() / 2f, stage.getHeight() / 2f, Align.center);
        joinedWindow.setModal(true);

        // لیست بازیکنان
        Table playersTable = new Table();
        for (String playerName : lobby.getPlayers()) {
            String labelText = playerName;
            if (playerName.equals(lobby.getAdmin())) {
                labelText += " (Admin)";
            }
            Label label = new Label(labelText, skin);
            label.setFontScale(1.5f);
            playersTable.add().left().pad(5).row();
        }
        joinedWindow.add(new ScrollPane(playersTable, skin)).expand().fill().pad(10).row();

        // انتخاب نقشه
        joinedWindow.add(new Label("Choose your map:", skin)).padTop(20).row();
        SelectBox<String> mapSelectBox = new SelectBox<>(skin);
        mapSelectBox.setItems("Default Farm", "Riverland Farm", "Forest Farm", "Hill-top Farm", "Wilderness Farm");
        joinedWindow.add(mapSelectBox).pad(10).row();

        // دکمه‌های پایین صفحه
        TextButton startGameButton = new TextButton("Start Game", skin);
        startGameButton.setDisabled(!currentPlayerName.equals(lobby.getAdmin())); // فقط برای ادمین فعال است
        TextButton leaveLobbyButton = new TextButton("Leave Lobby", skin);

        Table bottomTable = new Table();
        bottomTable.add(leaveLobbyButton).uniformX().pad(20);
        bottomTable.add(startGameButton).uniformX().pad(20);

        joinedWindow.add(bottomTable).padTop(30);

        // Listener برای خروج از لابی
        leaveLobbyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: ارسال پیام خروج از لابی به سرور
                joinedWindow.remove();
            }
        });

        stage.addActor(joinedWindow);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        populateLobbyList(createDummyLobbies()); // نمایش داده‌های ساختگی برای تست
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }

    // --- کلاس‌ها و متدهای ساختگی برای تست UI (اینها باید با داده‌های واقعی از سرور جایگزین شوند) ---
    private static class Lobby {
        private String name, admin;
        private boolean isPrivate;
        private List<String> players = new ArrayList<>();
        public Lobby(String name, boolean isPrivate, String admin, List<String> players) {
            this.name = name; this.isPrivate = isPrivate; this.admin = admin; this.players.addAll(players);
        }
        public String getName() { return name; }
        public int getPlayerCount() { return players.size(); }
        public boolean isPrivate() { return isPrivate; }
        public List<String> getPlayers() { return players; }
        public String getAdmin() { return admin; }
    }

    private List<Lobby> createDummyLobbies() {
        List<Lobby> lobbies = new ArrayList<>();
        lobbies.add(new Lobby("Fun Times Farm", false, "Ali", List.of("Ali", "Sara")));
        lobbies.add(new Lobby("Pro Gamers Only", true, "Reza", List.of("Reza")));
        lobbies.add(new Lobby("Chill Farming", false, "Mina", List.of("Mina", "Hassan", "Neda")));
        lobbies.add(new Lobby("Late Night Gamers", false, "Kian", List.of("Kian", "Tara", "Pouya", "Sima")));
        lobbies.add(new Lobby("Expert Only", true, "Farhad", List.of("Farhad", "Maryam")));
        return lobbies;
    }
}