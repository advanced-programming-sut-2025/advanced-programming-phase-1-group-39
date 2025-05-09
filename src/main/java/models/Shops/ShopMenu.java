package models.Shops;

public interface ShopMenu {
    void handleCommand(String command);
    String showAllProducts();
    String showAvailableProducts();
    void purchase(String product, int quantity);
    void addProduct(String product);
}
