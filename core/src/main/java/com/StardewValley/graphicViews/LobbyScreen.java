package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.models.App;
import com.StardewValley.models.Result;
import com.StardewValley.models.services.GameAssetManager;
import com.StardewValley.network.client.NetworkClient;
import com.StardewValley.network.shares.Lobby;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
    private List<Lobby> currentlyDisplayedLobbies = new ArrayList<>();
    private Table lobbyListTable;
    private ScrollPane scrollPane;

    private Label errorLabel;

    // network
    public NetworkClient networkClient;


    public LobbyScreen(NetworkClient networkClient) {
        this.networkClient = networkClient;

        stage = new Stage(new FitViewport(1920, 1080));
        skin = GameAssetManager.skin;

        setupUI();
    }

    private void setupUI() {
        Stack stack = new Stack();
        stack.setFillParent(true);
        stage.addActor(stack);

        stack.add(new Image(GameAssetManager.pregameBackground));

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stack.add(rootTable);

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

        lobbyListTable = new Table();
        scrollPane = new ScrollPane(lobbyListTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        // grow() باعث می‌شود این بخش تمام فضای عمودی و افقی باقیمانده را بگیرد
        contentTable.add(scrollPane).grow().width(820).row();

        // بخش ارور ها
        Table messageTable = new Table();
        messageTable.setFillParent(true);
        stack.add(messageTable);

        errorLabel = new Label("", GameAssetManager.messageBoxStyle);
        errorLabel.setVisible(false);
        errorLabel.setAlignment(Align.center);
        messageTable.add(errorLabel).bottom().padBottom(50).expandY();

        addListeners(backButton, refreshButton, createLobbyButton);
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
                populateLobbyList();
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

    public void populateLobbyList() {
        networkClient.sendRefreshLobbiesRequest();

        currentlyDisplayedLobbies = App.getApp().getAvailableLobbies();
        rebuildLobbyListUI();
    }


    public void showError(String message) {
        if(message == null || message.isEmpty()) {
            errorLabel.setVisible(false);
            return;
        }

        errorLabel.setText(message);

        errorLabel.clearActions();
        errorLabel.getColor().a = 1;
        errorLabel.setVisible(true);

        errorLabel.addAction(Actions.sequence(
                Actions.delay(3f),
                Actions.fadeOut(1f),
                Actions.visible(false)
        ));
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

    private void rebuildLobbyListUI() {
        lobbyListTable.clear();
        if (currentlyDisplayedLobbies.isEmpty()) {
            lobbyListTable.add(new Label("No active currentlyDisplayedLobbies found.", skin));
            return;
        }

        for (Lobby lobby : currentlyDisplayedLobbies) {
            if (!lobby.isVisibleToAll()) continue;
            String lobbyText = String.format("%s (%d/4)", lobby.getName(), lobby.getPlayerCount());
            TextButton lobbyButton = new TextButton(lobbyText, skin);
            if (lobby.isPrivate()) {
                lobbyButton.setColor(Color.GRAY);
            }

            lobbyButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (lobby.isPrivate()) {
                        showPasswordDialog(lobby);
                    } else {
                        networkClient.sendJoinLobbyRequest(lobby.getId());

                    }
                }
            });

            lobbyListTable.add(lobbyButton).fillX().height(80).pad(5).row();
        }
    }

    // join lobby handling
    public void handleJoinResponse(Result result) {
        boolean success = result.success();
        if (!success) {
            showError(result.message());
        } else {
            showJoinedLobbyWindow(App.getApp().getCurrentLobby(), App.getApp().getLoggedInUser().getUserName());
        }
    }


    //Utils
    private boolean areListsEqual(List<Lobby> list1, List<Lobby> list2) {
        return list1.size() == list2.size() && list1.containsAll(list2);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        populateLobbyList();
    }

    @Override
    public void render(float delta) {
        List<Lobby> appLobbyList = App.getApp().getAvailableLobbies();

        if (!areListsEqual(currentlyDisplayedLobbies, appLobbyList)) {
            this.currentlyDisplayedLobbies = new ArrayList<>(appLobbyList);
            rebuildLobbyListUI();
        }

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
}