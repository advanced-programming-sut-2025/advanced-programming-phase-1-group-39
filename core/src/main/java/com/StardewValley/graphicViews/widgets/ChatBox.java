// در پکیج graphicViews.widgets
package com.StardewValley.graphicViews.widgets;

import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.services.GameAssetManager;
import com.StardewValley.network.client.NetworkClient;
import com.StardewValley.network.shares.dtos.ChatMessageDTO;
import com.StardewValley.network.shares.message.RequestType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

public class ChatBox extends Window {
    private Table messageHistoryTable;
    private ScrollPane scrollPane;
    private TextField inputField;
    private NetworkClient networkClient;

    public ChatBox(String title, Skin skin, NetworkClient networkClient) {
        super(title, skin);
        this.networkClient = networkClient;

        this.setMovable(true);
        this.setSize(600, 300);
        this.setPosition(20, 20); // گوشه پایین چپ

        messageHistoryTable = new Table(skin);
        scrollPane = new ScrollPane(messageHistoryTable, skin);
        scrollPane.setFadeScrollBars(false);

        inputField = new TextField("", skin);

        this.add(scrollPane).expand().fill().row();
        this.add(inputField).expandX().fillX().padTop(5);

        setupInputListener();
    }

    private void setupInputListener() {
        inputField.setTextFieldListener((textField, c) -> {
            if (c == '\n' || c == '\r') {
                String text = textField.getText().trim();
                if (!text.isEmpty()) {
                    // **منطق تشخیص چت خصوصی**
                    if (text.startsWith("/w ")) {
                        String[] parts = text.split(" ", 3);
                        if (parts.length == 3) {
                            networkClient.sendChatMessage(parts[2], parts[1]);
                        }
                    } else {
                        networkClient.sendChatMessage(text, null); // چت عمومی
                    }
                    textField.setText("");
                }
            }
        });
    }

    public void addMessage(ChatMessageDTO message) {
        Label messageLabel = new Label(message.toString(), getSkin());
        if (message.isPrivate()) {
            messageLabel.setColor(Color.YELLOW);
        }
        messageHistoryTable.add(messageLabel).align(Align.left).row();

        // اسکرول به پایین
        scrollPane.layout();
        scrollPane.setScrollPercentY(1.0f);
    }

    public TextField getInputField() {
        return inputField;
    }
}