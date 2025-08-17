package com.StardewValley.graphicViews.widgets;

import com.StardewValley.models.Player;
import com.StardewValley.network.shares.message.ReactionType;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class ReactionBubble extends Table {

    private final Player targetPlayer;
    private float lifetime = 5.0f; // 5 ثانیه عمر

    public ReactionBubble(Player targetPlayer, ReactionType reactionType, Skin skin) {
        super(skin);
        this.targetPlayer = targetPlayer;

        // ظاهر حباب: یک پس‌زمینه و یک لیبل
        this.setBackground("window"); // از یک background مناسب در skin خود استفاده کنید
        Label reactionLabel = new Label(reactionType.getDisplayText(), skin);
        reactionLabel.setFontScale(2f); // اندازه فونت
        this.add(reactionLabel).pad(10);
        this.pack(); // اندازه Table را بر اساس محتوایش تنظیم کن

        // تنظیم موقعیت اولیه
        updatePosition();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // هر فریم، موقعیت حباب را بر اساس بازیکن آپدیت کن
        updatePosition();

        // از عمرش کم کن
        lifetime -= delta;
        if (lifetime <= 0) {
            // وقتی عمرش تمام شد، یک انیمیشن محو شدن اجرا و سپس خودش را حذف کن
            // (اطمینان حاصل کن که قبلا اکشن دیگری اضافه نشده)
            if (getActions().size == 0) {
                this.addAction(Actions.sequence(
                        Actions.fadeOut(0.5f),
                        Actions.removeActor()
                ));
            }
        }
    }

    private void updatePosition() {
        if (targetPlayer == null) {
            this.remove(); // اگر بازیکن به هر دلیلی null شد، حباب را حذف کن
            return;
        }

        // موقعیت حباب را بالای سر بازیکن تنظیم کن
        // این اعداد آفست را می‌توانید برای زیبایی بیشتر تغییر دهید
        float playerHeadX = targetPlayer.getX() + (targetPlayer.getTileLocation().x() / 2f);
        float playerHeadY = targetPlayer.getY() + (targetPlayer.getTileLocation().y() * 1.5f); // کمی بالاتر از قد بازیکن

        // موقعیت را در مرکز حباب تنظیم کن
        this.setPosition(playerHeadX, playerHeadY, Align.center);
    }
}