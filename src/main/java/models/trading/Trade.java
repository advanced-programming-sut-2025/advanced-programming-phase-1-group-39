package models.trading;

import models.Player;

public class Trade {
    private int id;
    private Player sender;
    private Player receiver;
    private TradeType type;
    private TradeItem offeredItem;
    private TradeItem requestedItem; // can be null
    private TradeStatus status = TradeStatus.PENDING;
    private boolean isSeenByReceiver = false;
}
