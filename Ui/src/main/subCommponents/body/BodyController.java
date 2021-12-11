package main.subCommponents.body;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import main.PrimMainController;
import main.subCommponents.body.chart.ChartController;
import mainLogic.MainLogic;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class BodyController {

    private PrimMainController mainController;
    private MainLogic mainLogic;

    @FXML
    private AnchorPane fullBody;

    @FXML
    private ComboBox<String> SelectStockButton;

    @FXML
    private Accordion BuyAccordion;

    @FXML
    private Accordion SellAccordion;

    @FXML
    private Accordion TransactionAccordion;

    @FXML
    private Button chartButton;


    private SimpleBooleanProperty EnableComponent;

    private SimpleBooleanProperty enableChartButton ;

    private List<Pair<String,Integer>> chartData ;

    private Map<String,Integer> initPrice2Symbol ;


    public Accordion getBuyAccordion() {
        return BuyAccordion;
    }

    public Accordion getSellAccordion() {
        return SellAccordion;
    }

    public Accordion getTransactionAccordion() {
        return TransactionAccordion;
    }

    public ComboBox<String> getSelectStockButton() {
        return SelectStockButton;
    }

    public void setMainController(PrimMainController mainController) {
        this.mainController = mainController;
    }

    public SimpleBooleanProperty EnableComponentProperty() {
        return EnableComponent;
    }

    public BodyController() {
        EnableComponent = new SimpleBooleanProperty();
        enableChartButton = new SimpleBooleanProperty();
    }

    @FXML
    private void initialize() {
        fullBody.getStylesheets().add("/main/subCommponents/body/bodyStyleSheets/bodyController.css");
        EnableComponent.bind(fullBody.disableProperty().not());
        chartButton.disableProperty().bind(enableChartButton.not());
    }


    public void setComboString(String symbol)
    {
        SelectStockButton.getItems().add(symbol);
    }

    @FXML
    void createList(ActionEvent event) {
        this.mainLogic.createListsForStocks(SelectStockButton.getSelectionModel().getSelectedItem());
        mainLogic.selectedBodyCBProperty().set(SelectStockButton.getSelectionModel().getSelectedItem());
        enableChartButton.set(true);

    }

    public void setMainLogic(MainLogic mainLogic) {
        this.mainLogic = mainLogic;
    }

    @FXML
    void openChart(ActionEvent event)  {
        try {
            Stage chartWindow = new Stage();
            chartWindow.initModality(Modality.APPLICATION_MODAL);
            chartWindow.setTitle("chart window");

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL popUpFxml = getClass().getResource("/main/subCommponents/body/chart/chartFxml.fxml");
            fxmlLoader.setLocation(popUpFxml);
            GridPane root = fxmlLoader.load(popUpFxml.openStream());

            ChartController chartController = fxmlLoader.getController();
            chartController.setSymbol2Price(initPrice2Symbol);
            chartController.setMapForChar(chartData,SelectStockButton.getSelectionModel().getSelectedItem());
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/main/subCommponents/body/chart/chart.css");
            chartWindow.setScene(scene);
            chartWindow.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void setMap2Chart(List<Pair<String,Integer>> map) {
        this.chartData = map ;
    }

    public void setStock2InitPrice(Map<String, Integer> symbol2Price) {
        this.initPrice2Symbol = symbol2Price ;
    }

    public void changeDarkSkin()
    {
        fullBody.getStylesheets().clear();
        fullBody.getStylesheets().add("/main/subCommponents/body/bodyStyleSheets/bodyControllerDark.css");
    }

    public void changeDefaultSkin()
    {
        fullBody.getStylesheets().clear();
        fullBody.getStylesheets().add("/main/subCommponents/body/bodyStyleSheets/bodyController.css");
    }

    public void changeLightSkin()
    {
        fullBody.getStylesheets().clear();
        fullBody.getStylesheets().add("/main/subCommponents/body/bodyStyleSheets/bodyControllerLight.css");
    }
}
