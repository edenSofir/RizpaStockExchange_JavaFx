
package main.subCommponents.body.Tabs.transection;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class TransactionController {

    @FXML
    private TitledPane HeaderTab;

    @FXML
    private Label DateLabel;

    @FXML
    private Label AmountLabel;

    @FXML
    private Label PriceLabel;

    @FXML
    private Label BuyLabel;

    @FXML
    private Label SellLabel;

    @FXML
    private Label cycleLabel;


    @FXML
    private void initialize(){
        HeaderTab.textProperty().bind(DateLabel.textProperty());
    }

    public void setDateLabel(String dateLabel) {
        DateLabel.setText(dateLabel);
    }

    public void setAmountLabel(int amountLabel) {
        AmountLabel.setText(String.valueOf(amountLabel));
    }

    public void setPriceLabel(int priceLabel) {
        PriceLabel.setText(String.valueOf(priceLabel));
    }

    public void setInitiatorNameLabel(String buyInitiator , String sellInitiator) {
        BuyLabel.setText(buyInitiator);
        SellLabel.setText(sellInitiator);
    }

    public void setCycleLabel(int cycle)
    {
        cycleLabel.setText(String.valueOf(cycle));
    }

}
