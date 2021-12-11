package main;

import command.Command;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import main.subCommponents.body.BodyController;
import main.subCommponents.body.Tabs.command.CommandController;
import main.subCommponents.body.Tabs.transection.TransactionController;
import main.subCommponents.exapetion.ExceptionController;
import main.subCommponents.header.HeaderController;
import main.subCommponents.left.userTab.UserTabController;
import mainLogic.MainLogic;
import transaction.Transaction;
import user.Item;
import user.User;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PrimMainController {

    @FXML
    private HeaderController headerComponentController;
    @FXML
    private AnchorPane headerComponent;
    @FXML
    private BodyController bodyComponentController;
    @FXML
    private TabPane leftComponent;
    @FXML
    private AnchorPane bodyComponent;

    private Stage primaryStage;
    private MainLogic mainLogic;
    private ExceptionController exceptionController;
    private Stage exceptionStage;
    private AnchorPane rootExcaption;
    private SimpleBooleanProperty doneHeader;
    private SimpleBooleanProperty setOk;
    private Map<String, UserTabController> UserToTailController;
    private Map<String, SimpleIntegerProperty> integerPropertyMap;
    private Map<String, CommandController> commandSellToController;
    private Map<String, CommandController> commandBuyToController;
    private Map<String, TransactionController> transactionToController;
    private List<Pair<String,Integer>> mapChart;
    private Map<String, Integer> initMap;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        headerComponentController.setPrimaryStage(primaryStage);
        headerComponentController.setMainLogic(this.mainLogic);
        headerComponentController.setExceptionController(this.exceptionController);
        bodyComponentController.EnableComponentProperty().bind(setOk);
        bodyComponentController.setMainLogic(this.mainLogic);
    }


    @FXML
    public void initialize() {
        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            bodyComponentController.setMainController(this);
        }
    }

    public SimpleBooleanProperty doneHeaderProperty() {
        return doneHeader;
    }

    public PrimMainController() {
        this.mainLogic = new MainLogic(this);
        doneHeader = new SimpleBooleanProperty(false);
        setOk = new SimpleBooleanProperty(false);
        UserToTailController = new HashMap<>();
        integerPropertyMap = new HashMap<>();
        commandSellToController = new HashMap<>();
        commandBuyToController = new HashMap<>();
        transactionToController = new HashMap<>();
        mapChart = new ArrayList<>();
        initMap = new HashMap<>();
    }

    public void setStock(Map.Entry<String, Integer> currStock) {
        bodyComponentController.setComboString(currStock.getKey());
        addEntryToStock2Rate(currStock);
    }

    private void addEntryToStock2Rate(Map.Entry<String, Integer> currStock) {
        integerPropertyMap.put(currStock.getKey(), new SimpleIntegerProperty(currStock.getValue()));
    }

    public void updateStockRate(String symbolName, int stockRate) {
        SimpleIntegerProperty propInteger = integerPropertyMap.get(symbolName);
        propInteger.set(stockRate);
    }

    public void setUser(User currUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("/main/subCommponents/left/userTab/UserTab.fxml");
            loader.setLocation(mainFXML);
            Tab singleUser = loader.load();

            UserTabController userTabController = loader.getController();
            userTabController.setMainController(this);
            userTabController.setMainLogic(mainLogic);
            userTabController.setMapOfAllStocks(integerPropertyMap);
            userTabController.setUserNameLabel(currUser.getNameOfUser());
            userTabController.setCurrencyLabel(currUser.getTotalValueHoldings());
            for (Item currItem : currUser.getHoldings().getUserHoldings()) {
                userTabController.setHoldings(currItem.getQuantity(), currItem.getStockSymbol(), findRateAccordingSymbol(currItem.getStockSymbol()));
            }
            leftComponent.getTabs().add(singleUser);
            UserToTailController.put(currUser.getNameOfUser(), userTabController);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLists(List<Command> commandsBuyList, List<Command> commandSellList, List<Transaction> transactionsList) {
        if (!bodyComponentController.getTransactionAccordion().getPanes().isEmpty())
            bodyComponentController.getTransactionAccordion().getPanes().removeAll(bodyComponentController.getTransactionAccordion().getPanes());
        if (!bodyComponentController.getBuyAccordion().getPanes().isEmpty())
            bodyComponentController.getBuyAccordion().getPanes().removeAll(bodyComponentController.getBuyAccordion().getPanes());
        if (!bodyComponentController.getSellAccordion().getPanes().isEmpty())
            bodyComponentController.getSellAccordion().getPanes().removeAll(bodyComponentController.getSellAccordion().getPanes());
        setCommandBuyList(commandsBuyList);
        setCommandSellList(commandSellList);
        setTransactionsList(transactionsList);
    }

    public void setCommandBuyList(List<Command> commandsBuyList) {
        try {
            for (Command currCommand : commandsBuyList) {
                FXMLLoader loader = new FXMLLoader();
                URL mainFXML = getClass().getResource("/main/subCommponents/body/Tabs/command/CommandTab.fxml");
                loader.setLocation(mainFXML);
                TitledPane singleCommand = loader.load();

                CommandController commandAccordionController = loader.getController();
                commandAccordionController.setMainController(this);
                commandAccordionController.setMainLogic(mainLogic);

                commandAccordionController.setCommandDate(currCommand.getCommandDate());
                commandAccordionController.setAmountLabel(currCommand.getStockAmount());
                commandAccordionController.setCommandTypeLabel(currCommand.getCommandT());
                commandAccordionController.setInitiatorLabel(currCommand.getInitiatorName());
                commandAccordionController.setPriceLabel(String.valueOf(currCommand.getPrice()));
                bodyComponentController.getBuyAccordion().getPanes().add(singleCommand);
                commandBuyToController.put(currCommand.getCommandDate(), commandAccordionController);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCommandSellList(List<Command> commandsSellList) {
        try {
            for (Command currCommand : commandsSellList) {
                FXMLLoader loader = new FXMLLoader();
                URL mainFXML = getClass().getResource("/main/subCommponents/body/Tabs/command/CommandTab.fxml");
                loader.setLocation(mainFXML);
                TitledPane singleCommand = loader.load();

                CommandController CommandAccordionController = loader.getController();
                CommandAccordionController.setMainController(this);
                CommandAccordionController.setMainLogic(mainLogic);
                CommandAccordionController.setCommandDate(currCommand.getCommandDate());
                CommandAccordionController.setAmountLabel(currCommand.getStockAmount());
                CommandAccordionController.setCommandTypeLabel(currCommand.getCommandT());
                CommandAccordionController.setInitiatorLabel(currCommand.getInitiatorName());
                CommandAccordionController.setPriceLabel(String.valueOf(currCommand.getPrice()));
                bodyComponentController.getSellAccordion().getPanes().add(singleCommand);
                commandSellToController.put(currCommand.getCommandDate(), CommandAccordionController);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setTransactionsList(List<Transaction> transactionList) {
        try {
            mapChart.clear();
            for (Transaction transaction : transactionList) {
                FXMLLoader loader = new FXMLLoader();
                URL mainFXML = getClass().getResource("/main/subCommponents/body/Tabs/transection/TransactionTab.fxml");
                loader.setLocation(mainFXML);
                TitledPane singleTransaction = loader.load();

                TransactionController transactionAccordionController = loader.getController();
                transactionAccordionController.setDateLabel(transaction.getTransactionDate());
                transactionAccordionController.setPriceLabel(transaction.getPrice());
                transactionAccordionController.setAmountLabel(transaction.getAmount());
                transactionAccordionController.setCycleLabel(transaction.getTotalValue());
                transactionAccordionController.setInitiatorNameLabel(transaction.getBuyerName(), transaction.getSellerName());
                bodyComponentController.getTransactionAccordion().getPanes().add(singleTransaction);
                transactionToController.put(transaction.getTransactionDate(), transactionAccordionController);
                mapChart.add(new Pair(transaction.getTransactionDate(), transaction.getPrice()));
            }
            bodyComponentController.setMap2Chart(this.mapChart);
            bodyComponentController.setStock2InitPrice(this.initMap);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public SimpleIntegerProperty findRateAccordingSymbol(String symbolStock) {
        return integerPropertyMap.get(symbolStock);
    }

    public void updateDataAfterTrade(User currUser) {
        if (!UserToTailController.isEmpty()) {
            UserTabController userTabController = UserToTailController.get(currUser.getNameOfUser());
            userTabController.setCurrencyLabel(currUser.getHoldings().calTotalValHoldings());
            userTabController.getAccordionList().getPanes().removeAll(userTabController.getAccordionList().getPanes());
            for (Item currItem : currUser.getHoldings().getUserHoldings()) {
                userTabController.setHoldings(currItem.getQuantity(), currItem.getStockSymbol(), findRateAccordingSymbol(currItem.getStockSymbol()));
            }
        }
    }

    public void createExceptionWindow() throws IOException {
        exceptionStage = new Stage();
        exceptionStage.initModality(Modality.APPLICATION_MODAL);
        exceptionStage.setTitle("Error!");

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL popUpFxml = getClass().getResource("/main/subCommponents/exapetion/exaptionWindow.fxml");
        fxmlLoader.setLocation(popUpFxml);
        this.rootExcaption = fxmlLoader.load(popUpFxml.openStream());
        this.exceptionController = fxmlLoader.getController();
        this.mainLogic.setExceptionStage(this.exceptionStage);
        this.mainLogic.setExceptionRoot(this.rootExcaption);
        this.mainLogic.setExceptionController(this.exceptionController);
    }


    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {

        headerComponentController.getMessageLabelTask().textProperty().bind(aTask.messageProperty());

        headerComponentController.getTaskProgressBar().progressProperty().bind(aTask.progressProperty());

        headerComponentController.getPercentLabel().textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        this.headerComponentController.getMessageLabelTask().textProperty().unbind();
        headerComponentController.getPercentLabel().textProperty().unbind();
        headerComponentController.getTaskProgressBar().progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }

    public void SetInitStockMap(Map<String, Integer> initMap) {
        this.initMap = initMap;
    }

    public void clearUI() {
        UserToTailController.clear();
        commandBuyToController.clear();
        commandSellToController.clear();
        transactionToController.clear();
        if(!leftComponent.getTabs().isEmpty())
        {
            leftComponent.getTabs().removeAll(leftComponent.getTabs());
        }
        if(!bodyComponentController.getSelectStockButton().getItems().isEmpty())
        {
            bodyComponentController.getSelectStockButton().getItems().removeAll(bodyComponentController.getSelectStockButton().getItems());
        }
    }

    public void setDarkSkin() {
        primaryStage.getScene().getStylesheets().clear();
        primaryStage.getScene().getStylesheets().add("/main/primeMainStylesheets/primeMainDark.css");
        leftComponent.getStylesheets().clear();
        leftComponent.getStylesheets().add("/main/subCommponents/left/userTab/userTabStyleSheets/userTabDark.css");
        headerComponentController.changeDarkSkin();
        bodyComponentController.changeDarkSkin();

    }

    public void setDefultSkin() {
        primaryStage.getScene().getStylesheets().clear();
        primaryStage.getScene().getStylesheets().add("/main/primeMainStylesheets/primeMain.css");
        leftComponent.getStylesheets().clear();
        leftComponent.getStylesheets().add("/main/subCommponents/left/userTab/userTabStyleSheets/userTab.css");
        headerComponentController.changeDefaultSkin();
        bodyComponentController.changeDefaultSkin();
    }

    public void seLightSkin() {
        primaryStage.getScene().getStylesheets().clear();
        primaryStage.getScene().getStylesheets().add("/main/primeMainStylesheets/primMainLight.css");
        leftComponent.getStylesheets().clear();
        leftComponent.getStylesheets().add("/main/subCommponents/left/userTab/userTabStyleSheets/userTabLight.css");
        headerComponentController.changeLightSkin();
        bodyComponentController.changeLightSkin();

    }
}
