package models.trading;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TradeManager {
    private static List<Trade> trades = new ArrayList<>();

    public static void addTrade(Trade trade) {
        trades.add(trade);
    }

    public static List<Trade> getTradesForUser(String username) {
        return trades.stream()
                .filter(t -> t.getSender().getUsername().equalsIgnoreCase(username)
                        && t.getStatus() == TradeStatus.PENDING)
                .collect(Collectors.toList());
    }

    public static List<Trade> getTradeHistory(String username) {
        return trades.stream()
                .filter(t -> t.getSender().getUsername().equalsIgnoreCase(username)
                        || t.getReceiver().getUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }

    public static Trade getTradeById(int id) {
        return trades.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }
}
