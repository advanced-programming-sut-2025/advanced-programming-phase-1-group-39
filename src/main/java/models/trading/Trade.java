package models.trading;

import models.Player;

public class Trade {
    private static int nextId = 1;

    private int id;
    private Player sender;
    private Player receiver;
    private TradeType type;
    private TradeItem offeredItem;
    private TradeItem requestedItem; // can be null
    private int price; // 0 if payment is not money
    private TradeStatus status = TradeStatus.PENDING;
    private boolean isSeenByReceiver = false;

    public Trade(Player sender, Player receiver, TradeType type, TradeItem offeredItem,
                 TradeItem requestedItem, int price) {
        this.id = nextId++;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.offeredItem = offeredItem;
        this.requestedItem = requestedItem;
        this.isSeenByReceiver = false;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public TradeStatus getStatus() {
        return status;
    }

    public TradeType getType() {
        return type;
    }

    public TradeItem getOfferedItem() {
        return offeredItem;
    }

    public TradeItem getRequestedItem() {
        return requestedItem;
    }

    public int getPrice() {
        return price;
    }

    public boolean isMoneyTrade() {
        return price == 0;
    }

    public boolean isSeenByTarget() {
        return isSeenByReceiver;
    }

    public void markSeen() {
        isSeenByReceiver = true;
    }

    public void accept() {
        status = TradeStatus.ACCEPTED;
    }

    public void reject() {
        status = TradeStatus.REJECTED;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trade ID: ").append(id)
                .append(" | From: ").append(sender.getUsername())
                .append(" | To: ").append(receiver.getUsername())
                .append(" | Type: ").append(type)
                .append(" | Item: ").append(offeredItem);
        if (price >= 0) {
            sb.append(" | Price: ").append(price).append(" coins");
        } else if (requestedItem != null) {
            sb.append(" | In exchange for: ").append(requestedItem);
        }
        sb.append(" | Status: ").append(status);
        return sb.toString();
    }
}
