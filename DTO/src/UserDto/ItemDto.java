package UserDto;

public class ItemDto {
    private String stockSymbol;
    private int quantity ;

    public void ItemDto(String stockSymbolDto , int quantityDto)
    {
        this.stockSymbol = stockSymbolDto ;
        this.quantity = quantityDto;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }
}
