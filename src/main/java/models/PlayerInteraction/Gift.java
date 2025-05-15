package models.PlayerInteraction;

import models.ItemStack;

public class Gift {
    private int giftId;
    private final String sender;
    private final String receiver;
    private final ItemStack giftItem;
    boolean isNew;

    public Gift(String sender, String receiver, ItemStack giftItem, int giftId) {
        this.sender = sender;
        this.receiver = receiver;
        this.giftItem = giftItem;
        this.isNew = true;
        this.giftId = giftId;
    }

    public int getGiftId() { return giftId; }

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
