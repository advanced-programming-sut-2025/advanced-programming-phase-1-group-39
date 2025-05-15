package models.PlayerInteraction;

import models.ItemStack;

public class Gift {
    private final String sender;
    private final String receiver;
    private final ItemStack giftItem;
    boolean isNew;

    public Gift(String sender, String receiver, ItemStack giftItem) {
        this.sender = sender;
        this.receiver = receiver;
        this.giftItem = giftItem;
        this.isNew = true;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public ItemStack getGiftItem() {
        return giftItem;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
