package main.subCommponents.left;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import main.Main;
import mainLogic.MainLogic;
import user.Item;

public class StockUserData implements StockData{

    protected SimpleStringProperty symbolName;
    protected SimpleIntegerProperty quantity;
    protected SimpleIntegerProperty StockRate;
    private MainLogic mainLogic ;

    public StockUserData() {
        symbolName = new SimpleStringProperty();
        quantity = new SimpleIntegerProperty();
        StockRate = new SimpleIntegerProperty();
    }

    @Override
    public void setSymbolStock(String symbolStock) {
        this.symbolName = new SimpleStringProperty(symbolStock);
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = new SimpleIntegerProperty(quantity);
    }


    @Override
    public void setStockRate(int stockRate) {
        this.StockRate = new SimpleIntegerProperty(stockRate);
    }

    public String getSymbolStock() {
        return symbolName.get();
    }

    public SimpleStringProperty symbolNameProperty() {
        return symbolName;
    }

    @Override
    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    @Override
    public int getStockRate() {
        return StockRate.get();
    }

    public SimpleIntegerProperty stockRateProperty() {
        return StockRate;
    }

    public void setMainLogic(MainLogic mainLogic) {
        this.mainLogic = mainLogic ;
    }
}
