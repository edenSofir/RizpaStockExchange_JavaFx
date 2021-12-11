package main.subCommponents.left.userTab.newStockS;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import main.subCommponents.left.StockUserData;
import mainLogic.MainLogic;

public class AccordionTabController extends StockUserData {

    @FXML
    private TitledPane TitlePaneLabel;

    @FXML
    private Label SymbolNameLabel;

    @FXML
    private Label QuantityLabel;

    @FXML
    private Label StockRateLabel;

    public SimpleIntegerProperty stockRateProperty;


    @FXML
    private void initialize() {
        TitlePaneLabel.textProperty().bind(SymbolNameLabel.textProperty());
    }

    public void setSymbolNameLabel(String symbolName) {
        SymbolNameLabel.setText(symbolName);
        this.symbolName.setValue(symbolName);
    }

    public void setQuantityLabel(int quantityLabel) {
        QuantityLabel.setText(String.valueOf(quantityLabel));
        this.quantity.set(quantityLabel);
    }

    public void setStockRateLabel(int stockRateLabel) {
        StockRateLabel.setText(String.valueOf(stockRateLabel));
        this.StockRate.set(stockRateLabel);
    }

}
