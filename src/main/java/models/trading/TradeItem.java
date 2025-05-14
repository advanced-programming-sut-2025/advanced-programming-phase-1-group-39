package models.trading;

public class TradeItem {
    private String itemName;
    private int amount;
    private int price;

    public TradeItem(String itemName, int amount, int price) {
        this.itemName = itemName;
        this.amount = amount;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }
}
