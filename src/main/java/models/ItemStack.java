package models;

public class ItemStack {
    Item item;
    int amount;

    public ItemStack(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public void addStack(int amount) {
        amount += amount;
    }
}