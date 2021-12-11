package main.subCommponents.left.userTab;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import main.PrimMainController;
import main.subCommponents.left.popWindows.TradeController;
import main.subCommponents.left.userTab.newStockS.AccordionTabController;
import mainLogic.MainLogic;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserTabController {

    @FXML
    private Tab LabelUserTabComponent;
    @FXML
    private Label UserNameLabel;
    @FXML
    private Label CurrencyLabel;
    @FXML
    private Button TradeButton;
    @FXML
    private TabPane fullUser;
    @FXML
    private Accordion AccordionList;

    private PrimMainController mainController;
    private Map<String, AccordionTabController> AccordionToTailController;
    private Map<String, SimpleIntegerProperty> allStocksMap;
    private MainLogic mainLogic ;
    private SimpleIntegerProperty stockRate;



    @FXML
    void onSelectionChanged() {
        if(mainController!= null && this.mainLogic!=null)
            mainController.updateDataAfterTrade(mainLogic.Name2User(getUserNameLabel().getText()));
    }

    @FXML
    void pushedTradeButton(ActionEvent event) throws IOException {
        Stage newWindow = new Stage();
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setTitle("Create new trade");

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL popUpFxml = getClass().getResource("/main/subCommponents/left/popWindows/newTradeWindow.fxml");
        fxmlLoader.setLocation(popUpFxml);
        ScrollPane root = fxmlLoader.load(popUpFxml.openStream());

        TradeController tradeController = fxmlLoader.getController();
        tradeController.setMainLogic(mainLogic);
        tradeController.setUserController(this);
        tradeController.setMapOfStocks(allStocksMap);
        tradeController.setUserPlaceHolder(UserNameLabel.getText());
        tradeController.setStage(newWindow);
        Scene scene = new Scene(root);
        newWindow.setScene(scene);
        newWindow.show();
    }


    @FXML
    private void initialize() {
        LabelUserTabComponent.textProperty().bind(UserNameLabel.textProperty());

    }

    public void setMainController(PrimMainController mainController) {
        this.mainController = mainController;
    }

    public void setUserNameLabel(String nameOfUser) {
        UserNameLabel.setText(nameOfUser);
    }

    public void setCurrencyLabel(int totalValueHoldings) {
        CurrencyLabel.setText(String.valueOf(totalValueHoldings));
    }

    public void setHoldings(int quantity, String SymbolName, SimpleIntegerProperty stockRate) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("/main/subCommponents/left/userTab/newStockS/AcordionTab.fxml");
            loader.setLocation(mainFXML);
            TitledPane singleItem = loader.load();

            AccordionTabController accordionTabController = loader.getController();
            accordionTabController.setMainLogic(mainLogic);
            accordionTabController.setSymbolNameLabel(SymbolName);
            accordionTabController.setQuantityLabel(quantity);
            accordionTabController.setStockRateLabel(stockRate.getValue());
            AccordionList.getPanes().add(singleItem);
            AccordionToTailController.put(SymbolName, accordionTabController);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Accordion getAccordionList() {
        return AccordionList;
    }

    public UserTabController() {
        this.AccordionToTailController = new HashMap<>();
        this.allStocksMap = new HashMap<>();
    }


    public void setMapOfAllStocks(Map<String, SimpleIntegerProperty> integerPropertyMap) {
        allStocksMap = integerPropertyMap;
    }

    public Set<String> getAccordionToTailController() {
        return AccordionToTailController.keySet();
    }

    public Map<String, AccordionTabController> getAccordionsControllers()
    {
        return  AccordionToTailController ;
    }


    public int symbol2Amount(String symbolName)
    {
        AccordionTabController currentTail = AccordionToTailController.get(symbolName);
        if(currentTail != null) {
            return currentTail.getQuantity();
        }
        else
            return 0;
    }

    public void setMainLogic(MainLogic mainLogic) {
        this.mainLogic = mainLogic ;
    }

    public Label getUserNameLabel() {
        return UserNameLabel;
    }
}



