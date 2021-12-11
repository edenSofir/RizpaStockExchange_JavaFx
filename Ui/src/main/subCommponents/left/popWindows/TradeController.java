package main.subCommponents.left.popWindows;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.PrimMainController;
import main.subCommponents.left.userTab.UserTabController;
import mainLogic.MainLogic;
import menu.MenuEngineImpl;

import java.util.Map;

import static java.lang.String.format;

public class TradeController {
    @FXML
    private RadioButton BuyRadioButton;

    @FXML
    private ToggleGroup TradeType;

    @FXML
    private RadioButton SellRadioButton;

    @FXML
    private ComboBox<String> StockComboBox;

    @FXML
    private Label UserPlaceHolder;

    @FXML
    private RadioButton LmtRadioButton;

    @FXML
    private ToggleGroup CommandType;

    @FXML
    private RadioButton MktRadioButton;

    @FXML
    private Button ApplyButton;

    @FXML
    private Label TextRes;

    @FXML
    private Label LimitWordLabel;

    @FXML
    private TextField writeLimitHolder;

    @FXML
    private TextField AmountTextHolder;

    @FXML
    private Slider SliderAmount;

    @FXML
    private Label amountSlider;

    @FXML
    private Label textInformation;

    @FXML
    private Button closeWinButton;

    private MenuEngineImpl menuEngine;
    private PrimMainController mainController;
    private UserTabController currentUser;
    private Map<String, SimpleIntegerProperty> allStocksMap;
    private ObservableList<String> listOfStocks;
    private Stage currentStage;

    private SimpleIntegerProperty maxValOfStock;
    private MainLogic mainLogic;
    private SimpleStringProperty messageError;


    @FXML
    private void initialize() {
        AmountTextHolder.visibleProperty().bind(SliderAmount.visibleProperty().not());
        SliderAmount.visibleProperty().bind(SellRadioButton.selectedProperty());
        StockComboBox.setItems(listOfStocks);
        amountSlider.textProperty().bind(SliderAmount.valueProperty().asString("%.0f"));
        amountSlider.visibleProperty().bind(SellRadioButton.selectedProperty());
        SliderAmount.maxProperty().bind(maxValOfStock);
        ApplyButton.disableProperty().bind(SliderAmount.pressedProperty());
    }

    public TradeController() {
        this.listOfStocks = FXCollections.observableArrayList();
        this.maxValOfStock = new SimpleIntegerProperty();
        this.messageError = new SimpleStringProperty();

    }

    public void setMainController(PrimMainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void ApplyButtonPushed() {
        Boolean flag = checkApplyButton();
        if (!flag) {
            mainLogic.ApplyNotOk(messageError.getValue());
            currentStage.close();
        } else {
            int BuyOrSell;
            int LmtOrMkt;
            int amount;
            int limit;
            String userName = currentUser.getUserNameLabel().getText();
            String symbolName = StockComboBox.getSelectionModel().getSelectedItem();
            if (BuyRadioButton.isSelected()) {
                BuyOrSell = 1;
                amount = Integer.parseInt(AmountTextHolder.getText());
            } else {
                BuyOrSell = 2;
                amount = Integer.parseInt(amountSlider.getText());
            }
            if (LmtRadioButton.isSelected()) {
                LmtOrMkt = 1;
                limit = Integer.parseInt(writeLimitHolder.getText()); // price in menuEngine
            } else {
                LmtOrMkt = 2;
                limit = 1;
            }
            mainLogic.createTradeExe(BuyOrSell, LmtOrMkt, symbolName, amount, limit, userName);
            ApplyButton.visibleProperty().set(false);
            textInformation.setText(mainLogic.messageInfoProperty().getValue());
            closeWinButton.visibleProperty().set(true);
        }
    }


    public void setMainLogic(MainLogic mainLogic) {
        this.mainLogic = mainLogic;
    }

    @FXML
    void BuyActionType() {
        BuyRadioButton.setSelected(true);
        StockComboBox.disableProperty().set(false);
        setStockComboBoxForBuy();
    }

    @FXML
    void PressLmtButton() {
        LmtRadioButton.setSelected(true);
        MktRadioButton.setSelected(false);
        writeLimitHolder.disableProperty().set(false);

    }


    @FXML
    void SellActionType() {
        SellRadioButton.setSelected(true);
        StockComboBox.disableProperty().set(false);
        setStockComboForSell();

    }

    @FXML
    void WriteAmountAction() {
        this.mainLogic.checkAmountInEngine(AmountTextHolder.getText());
        //this.ApplyButton.disableProperty().set(false); //why it is bound?? is this is because the css ?
    }

    @FXML
    void WriteLimitAction() {
        this.mainLogic.checkAmountInEngine(writeLimitHolder.getText());
    }

    @FXML
    void pressMktButton() {
        MktRadioButton.setSelected(true);
        LmtRadioButton.setSelected(false);
        writeLimitHolder.disableProperty().set(true);
    }

    @FXML
    void SelectStockBox() {
        maxValOfStock.set(currentUser.symbol2Amount(StockComboBox.getSelectionModel().getSelectedItem()));
        LmtRadioButton.disableProperty().set(false);
        MktRadioButton.disableProperty().set(false);
    }

    public void setUserPlaceHolder(String currUser) {
        UserPlaceHolder.setText(currUser);
    }

    public void setUserController(UserTabController userTabController) {
        this.currentUser = userTabController;
    }

    public void setMapOfStocks(Map<String, SimpleIntegerProperty> integerPropertyMap) {
        allStocksMap = integerPropertyMap;
    }

    private void setStockComboBoxForBuy() {
        deleteComboStock();
        listOfStocks.addAll(allStocksMap.keySet());
    }

    private void setStockComboForSell() {
        deleteComboStock();
        listOfStocks.addAll(currentUser.getAccordionToTailController());
    }

    private void deleteComboStock() {
        listOfStocks.clear();
    }

    public void setStage(Stage currStage) {
        this.currentStage = currStage;
    }

    @FXML
    void closeStage() {
        textInformation.setText("");
        currentStage.close();
    }

    private Boolean checkApplyButton() //if every one of the fields is enterd
    {
        if (StockComboBox.getSelectionModel().getSelectedItem() == null || (!BuyRadioButton.isSelected() && !SellRadioButton.isSelected()) || (!LmtRadioButton.isSelected() && !MktRadioButton.isSelected()) ) {
            messageError.setValue("sorry ! you didn't fill out all the details , the window will be closed now");
            return false;
        }
        if (BuyRadioButton.isSelected() && AmountTextHolder.getText().equals("")) {
            messageError.setValue("sorry ! you didn't fill out all the details , the window will be closed now");
            return false;
        }
        if (SellRadioButton.isSelected() && SliderAmount.getValue() <= 0 ) {
            messageError.setValue("sorry ! you didn't fill out all the details , the window will be closed now");
            return false;
        }
        if (LmtRadioButton.isSelected() && writeLimitHolder.getText().equals("") ) {
            messageError.setValue("sorry ! you didn't fill out all the details , the window will be closed now");
            return false;
        }
        return true;
    }
}


