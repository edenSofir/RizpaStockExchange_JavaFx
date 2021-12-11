package main.subCommponents.left;

public interface StockData {
    String getSymbolStock();
    void setSymbolStock(String symbolStock);

    int getQuantity();
    void setQuantity(int quantity);

    int getStockRate();
    void setStockRate(int stockRate);
}
