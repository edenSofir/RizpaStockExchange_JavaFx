package user;

public class Item {

    private String stockSymbol;
    private int quantity ;

    public Item(String stockSymbol, int quantity) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
