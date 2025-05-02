package models;

public class ItemStack {
    Item item;
    int amount;

    public ItemStack(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public void addStack(int amount) {
        amount += amount;
    }
}
