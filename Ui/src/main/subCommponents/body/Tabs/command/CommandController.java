package main.subCommponents.body.Tabs.command;

import command.Enum.CommandTypes;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import main.PrimMainController;
import mainLogic.MainLogic;


public class CommandController {


    @FXML
    private TitledPane commandHeader;

    @FXML
    private Label DateLabel;

    @FXML
    private Label CommandTypeLabel;

    @FXML
    private Label AmountLabel;

    @FXML
    private Label PriceLabel;

    @FXML
    private Label InitiatorLabel;

    private PrimMainController mainController;
    private MainLogic mainLogic ;

    @FXML
    private void initialize ()
    {
        commandHeader.textProperty().bind(PriceLabel.textProperty());
    }

    public void setMainController(PrimMainController mainController) {
        this.mainController = mainController;
    }

    public void setMainLogic(MainLogic mainLogic)
    {
        this.mainLogic = mainLogic;
    }

    public void setCommandDate(String date)
    {
        DateLabel.setText(date);
    }

    public void setCommandTypeLabel(CommandTypes commandT)
    {
        CommandTypeLabel.setText(commandT.toString());
    }
     public void setAmountLabel(int amount)
     {
         AmountLabel.setText(String.valueOf(amount));
     }
     public void setPriceLabel(String limit)
     {
         PriceLabel.setText(limit);
     }
     public void setInitiatorLabel(String initiator)
     {
         InitiatorLabel.setText(initiator);
     }

}
