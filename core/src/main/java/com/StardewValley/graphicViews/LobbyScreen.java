package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.models.App;
import com.StardewValley.models.Result;
import com.StardewValley.models.map.FarmType;
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

        addListeners(backButton, refreshButton, createLobbyButton, searchField);
    }

    private void addListeners(TextButton backButton, TextButton refreshButton, TextButton createLobbyButton,
                              TextField searchField) {
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
                showCreateLobbyWindow();
            }
        });

        searchField.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped(TextField textField, char c) {
                if (c == '\n' || c == '\r') {
                    String id = textField.getText().trim();
                    if (id.isEmpty()) return;

                    networkClient.sendJoinLobbyRequest(id);

                    textField.setText("");
                }
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
        Dialog passwordDialog = new Dialog("Enter Password", skin);

        // عناصر UI
        Label infoLabel = new Label("Lobby \"" + lobby.getName() + "\" requires a password:", skin);
        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextButton joinButton = new TextButton("Join", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        // یک لیبل برای نمایش پیام "در حال بررسی..." یا خطا
        Label statusLabel = new Label("", skin);
        statusLabel.setColor(Color.YELLOW);

        // چیدمان UI
        Table content = passwordDialog.getContentTable();
        content.pad(20);
        content.add(infoLabel).colspan(2).row();
        content.add(passwordField).width(300).colspan(2).pad(20).row();
        content.add(statusLabel).colspan(2).padBottom(10).row();

        passwordDialog.getButtonTable().add(cancelButton).width(150).pad(10);
        passwordDialog.getButtonTable().add(joinButton).width(150).pad(10);

        // --- Listener های جدید ---

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                passwordDialog.hide();
            }
        });

        joinButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // ۱. UI را در حالت انتظار قرار بده
                statusLabel.setText("Verifying password...");
                joinButton.setDisabled(true);
                cancelButton.setDisabled(true);

                // ۲. درخواست را به سرور بفرست
                String password = passwordField.getText();
                // <<-- یک متد جدید در NetworkClient برای این کار بسازید -->>
                networkClient.sendJoinPrivateLobbyRequest(lobby.getId(), password);
            }
        });

        // نمایش دیالوگ
        passwordDialog.show(stage);
    }

    public void closeJoinedLobbyWindow() {
        Actor oldWindow = stage.getRoot().findActor("joinedLobbyWindow");
        if(oldWindow != null) oldWindow.remove();
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
        joinedWindow.add(new Label("Lobby Id: " + lobby.getId(), skin)).pad(15).row();
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
        SelectBox<FarmType> mapSelectBox = new SelectBox<>(skin);
        mapSelectBox.setItems(FarmType.values());
        mapSelectBox.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                networkClient.sendChooseMapRequest(mapSelectBox.getSelected());
            }
        });
        joinedWindow.add(mapSelectBox).pad(10).row();

        // دکمه‌های پایین صفحه
        TextButton startGameButton = new TextButton("Start Game", skin);
        if (currentPlayerName.equals(lobby.getAdmin())) {
            startGameButton.setDisabled(false);
            startGameButton.setColor(Color.WHITE);
        } else {
            startGameButton.setDisabled(true);
            startGameButton.setColor(Color.GRAY);
        }
        TextButton leaveLobbyButton = new TextButton("Leave Lobby", skin);

        Table bottomTable = new Table();
        bottomTable.add(leaveLobbyButton).uniformX().pad(20);
        bottomTable.add(startGameButton).uniformX().pad(20);

        joinedWindow.add(bottomTable).padTop(30);

        // Listener برای خروج از لابی
        leaveLobbyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                networkClient.sendLeaveLobbyRequest();
                joinedWindow.remove();
            }
        });

        stage.addActor(joinedWindow);
    }

    public void updateJoinedLobbyWindow(Lobby updatedLobby) {
        // ۱. پنجره فعلی را پیدا کن
        Window joinedWindow = stage.getRoot().findActor("joinedLobbyWindow");
        if (joinedWindow == null) {
            // اگر پنجره به هر دلیلی بسته شده بود، کاری نکن
            return;
        }

        String currentPlayerName = App.getApp().getLoggedInUser().getUserName();

        // ۲. <<-- آپدیت لیست بازیکنان -->>
        // ابتدا ScrollPane و سپس Table داخل آن را پیدا کن
        ScrollPane scrollPane = joinedWindow.findActor("playersScrollPane"); // <<-- باید به ScrollPane نام بدهید
        if (scrollPane != null) {
            Table playersTable = (Table) scrollPane.getActor();
            playersTable.clear(); // لیست قدیمی را کاملاً پاک کن

            // لیست جدید را بساز
            for (String playerName : updatedLobby.getPlayers()) {
                String labelText = playerName;
                if (playerName.equals(updatedLobby.getAdmin())) {
                    labelText += " (Admin)";
                }
                Label label = new Label(labelText, skin);
                label.setFontScale(1.5f);
                playersTable.add(label).left().pad(5).row(); // <<-- اشتباه قبلی در کد شما اصلاح شد
            }
        }

        // ۳. <<-- آپدیت وضعیت دکمه Start Game -->>
        TextButton startGameButton = joinedWindow.findActor("startGameButton"); // <<-- باید به دکمه نام بدهید
        if (startGameButton != null) {
            boolean isAdmin = currentPlayerName.equals(updatedLobby.getAdmin());
            startGameButton.setDisabled(!isAdmin);
            startGameButton.setColor(isAdmin ? Color.WHITE : Color.GRAY);
        }
    }

    private void showCreateLobbyWindow() {
        // از Dialog استفاده می‌کنیم که یک نوع Window با قابلیت‌های بیشتر است
        Dialog dialog = new Dialog("Create New Lobby", skin);

        Table content = dialog.getContentTable();
        content.pad(20);

        // --- ۱. نام لابی ---
        content.add(new Label("Lobby Name:", skin)).left();
        TextField lobbyNameField = new TextField("", skin);
        content.add(lobbyNameField).width(300).pad(10).row();

        // --- ۲. انتخاب نوع دسترسی (Public/Private) ---
        content.add(new Label("Access:", skin)).left();
        SelectBox<String> accessSelectBox = new SelectBox<>(skin);
        accessSelectBox.setItems("Public", "Private");
        content.add(accessSelectBox).width(300).pad(10).row();

        // --- ۳. فیلد رمز عبور ---
        Label passwordLabel = new Label("Password:", skin);
        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        // در ابتدا فیلد رمز عبور را غیرفعال و مخفی می‌کنیم
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);
        passwordField.setDisabled(true);

        content.add(passwordLabel).left();
        content.add(passwordField).width(300).pad(10).row();

        // --- ۴. چک‌باکس قابلیت مشاهده ---
        CheckBox visibleCheckBox = new CheckBox(" Visible to all players in list", skin);
        visibleCheckBox.setChecked(true); // به صورت پیش‌فرض فعال است
        content.add(visibleCheckBox).colspan(2).left().pad(10).row();

        // --- دکمه‌های پایین دیالوگ ---
        TextButton createButton = new TextButton("Create", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        dialog.getButtonTable().add(cancelButton).width(150).pad(20);
        dialog.getButtonTable().add(createButton).width(150).pad(20);

        // --- منطق و Listener ها ---

        // Listener برای SelectBox دسترسی
        accessSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isPrivate = accessSelectBox.getSelected().equals("Private");
                passwordLabel.setVisible(isPrivate);
                passwordField.setVisible(isPrivate);
                passwordField.setDisabled(!isPrivate);
            }
        });

        // Listener برای دکمه Cancel
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.hide();
            }
        });

        // Listener برای دکمه Create
        createButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String lobbyName = lobbyNameField.getText();
                boolean isPrivate = accessSelectBox.getSelected().equals("Private");
                String password = passwordField.getText();
                boolean isVisible = visibleCheckBox.isChecked();
                String adminUsername = App.getApp().getLoggedInUser().getUserName();

                // ولیدیشن ساده
                if (lobbyName.trim().isEmpty()) {
                    showError("Lobby name cannot be empty.");
                    return;
                }
                if (isPrivate && password.trim().isEmpty()) {
                    showError("Private lobbies must have a password.");
                    return;
                }

                // ارسال درخواست به سرور
                networkClient.sendCreateLobbyRequest(lobbyName, adminUsername, isPrivate, isVisible, isPrivate ? password : null);

                // دیالوگ را ببند
                dialog.hide();
            }
        });

        // نمایش دیالوگ
        dialog.show(stage);
    }

    private void rebuildLobbyListUI() {
        lobbyListTable.clear();
        if (currentlyDisplayedLobbies.isEmpty()) {
            lobbyListTable.add(new Label("No active lobby found.", skin));
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