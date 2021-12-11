package mainLogic;

import Exceptions.*;
import StocksD.StockDto;
import TransactionD.TransactionsDto;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.PrimMainController;
import main.subCommponents.exapetion.ExceptionController;
import menu.MenuEngine;
import stock.Stock;
import transaction.Transaction;
import uiTask.collectDateFromXmlTask;
import user.User;
import user.Users;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MainLogic {

    private SimpleStringProperty filePath;
    private PrimMainController mainController;
    private MenuEngine menuEngine;
    private StockDto stockDto;
    private SimpleStringProperty messageInfo;
    private ExceptionController exceptionController;
    private Stage exceptionStage;
    private AnchorPane rootException;
    private SimpleStringProperty selectedBodyCB;
    private SimpleBooleanProperty loadedSuccessful;
    private Task<Boolean> currentRunningTask;

    public MenuEngine getMenuEngine() {
        return menuEngine;
    }

    public MainLogic(PrimMainController mainController) {
        filePath = new SimpleStringProperty();
        messageInfo = new SimpleStringProperty();
        this.mainController = mainController;
        this.selectedBodyCB = new SimpleStringProperty();
        this.loadedSuccessful = new SimpleBooleanProperty(false);
    }

    public SimpleStringProperty selectedBodyCBProperty() {
        return selectedBodyCB;
    }

    public void setMenuEngine(MenuEngine menuEngine) {
        this.menuEngine = menuEngine;
    }

    public SimpleStringProperty filePathProperty() {
        return filePath;
    }

    public SimpleBooleanProperty loadedSuccessfulProperty() {
        return loadedSuccessful;
    }

    public void LoadData(){
        try {
            menuEngine.readSystemInfoFile(filePath.get());
            mainController.clearUI();
            mainController.SetInitStockMap(menuEngine.createSetOfStocksAndPrice());
            collectDateFromXml(mainController::setUser,mainController::setStock,menuEngine.createCollectionOfUsers(),menuEngine.createSetOfStocksAndPrice()
                    ,() -> {
                        loadedSuccessful.set(true);}); //check why he dosn't do this action
            loadedSuccessful.set(true);

        } catch (Exception e) {
            if(loadedSuccessful.getValue().equals(true))
                 loadedSuccessful.set(false);
            exceptionController.setMessageException(e.getMessage());
            Scene scene = new Scene(rootException);
            exceptionStage.setScene(scene);
            exceptionStage.show();
        }
    }

    public void createTradeExe(int buyOrSell, int lmtOrMkt, String symbolName, int amount, int limit, String userName) {
        String buyOrSellMood;
        String MktOrLmtMood;
        try {
            if (buyOrSell == 1)
                buyOrSellMood = "buy";
            else
                buyOrSellMood = "sell";
            if (lmtOrMkt == 1)
                MktOrLmtMood = "LMT";
            else
                MktOrLmtMood = "MKT";

            TransactionsDto transactionsDto = menuEngine.tradingExecution(buyOrSell, symbolName, amount, limit, lmtOrMkt, userName);
            if (!transactionsDto.getTransactionList().isEmpty()) {
                int number = transactionsDto.getTransactionList().size();
                mainController.updateStockRate(symbolName,transactionsDto.getTransactionList().get(number-1).getPrice());
                this.updateAllUsers(menuEngine.getUsers());

                createMessageForTransaction(transactionsDto.getTransactionList(), amount, userName, MktOrLmtMood, buyOrSellMood, limit, symbolName); //need to add here the buyOrSellMood and MktOrLmtMood
            } else {
                if(MktOrLmtMood.equals("MKT")) {
                    messageInfo.setValue("greetings! ," + userName + "\n" + "the current command that you committed was successfully entered to the " + buyOrSellMood + " list!" +
                            "\n" + "details about the command: \n" + "1.command type: " + buyOrSellMood + " \n 2.LMT / MKT: " + MktOrLmtMood + "\n 3. command symbol name: " + symbolName +
                            "\n 4.the amount of shares: " + amount + "\n 5.limit: - ");
                }
                else
                    messageInfo.setValue("greetings! ," + userName + "\n" + "the current command that you committed was successfully entered to the " + buyOrSellMood + " list!" +
                            "\n" + "details about the command: \n" + "1.command type: " + buyOrSellMood + " \n 2.LMT / MKT: " + MktOrLmtMood + "\n 3. command symbol name: " + symbolName +
                            "\n 4.the amount of shares: " + amount + "\n 5.limit: " + limit);
            }
            if (symbolName.equals(selectedBodyCB.getValue())) {
                this.stockDto = menuEngine.showListOfCommands(symbolName);
                mainController.setLists(stockDto.getCommands().getCommandsBuyList(), stockDto.getCommands().getCommandsSellList(), stockDto.getTransactions().getTransactionList());
                //mainController.updateBodyList(menuEngine.showCurrStock(symbolName));//need to change it to DTO //TODO
            }
        } catch (Exception e) {
            exceptionController.setMessageException(e.getMessage());
            Scene scene = new Scene(rootException);
            exceptionStage.setScene(scene);
            exceptionStage.show();
        }
    }

    public SimpleStringProperty messageInfoProperty() {
        return messageInfo;
    }

    public void createListsForStocks(String symbolStock) {
        try {
            this.stockDto = menuEngine.showListOfCommands(symbolStock);
            this.mainController.setLists(stockDto.getCommands().getCommandsBuyList(), stockDto.getCommands().getCommandsSellList(), stockDto.getTransactions().getTransactionList());
        } catch (Exception e) {
            exceptionController.setMessageException(e.getMessage());
            Scene scene = new Scene(rootException);
            exceptionStage.setScene(scene);
            exceptionStage.show();
        }
    }

    private void createMessageForTransaction(List<Transaction> transactionList, int amount, String userName, String MktOrLmtMood, String buyOrSellMood, int limit, String symbolName) {
        int totalAmount = 0;
        for (Transaction transaction : transactionList) {
            totalAmount = totalAmount + transaction.getAmount();
        }
        if (totalAmount == amount) {
            String message;
            message = "greetings," + userName + "!\n" + "the current command that you committed was fully executed , here is all the transactions that has been made: \n";
            for (Transaction transaction : transactionList) {
                message = message.concat(transaction.toString());
            }
            this.messageInfo.set(message);
        } else {
            String message;
            if(MktOrLmtMood.equals("MKT")) {
                message = "great," + userName + " ! \n" + "the current command that you committed was partly executed , here is all the transactions that has been made and the command that entered to the" +
                        buyOrSellMood + "list! \n" + "details about the command: \n" + "1.command type: " + buyOrSellMood + " \n 2.LMT / MKT: " + MktOrLmtMood + "\n 3. command symbol name: " + symbolName +
                        "\n 4.the amount of shares: " + (amount - totalAmount) + "\n 5.limit: - " + "here is all the transactions that has been made:";
            }
            else
                message = "great," + userName + " ! \n" + "the current command that you committed was partly executed , here is all the transactions that has been made and the command that entered to the" +
                        buyOrSellMood + "list! \n" + "details about the command: \n" + "1.command type: " + buyOrSellMood + " \n 2.LMT / MKT: " + MktOrLmtMood + "\n 3. command symbol name: " + symbolName +
                        "\n 4.the amount of shares: " + (amount - totalAmount) + "\n 5.limit: " + limit + "here is all the transactions that has been made:";
            for (Transaction transaction : transactionList) {
                message = message.concat(transaction.toString());
            }
            this.messageInfo.set(message);

        }
    }

    public void setExceptionStage(Stage exceptionStage) {
        this.exceptionStage = exceptionStage;
    }

    public void setExceptionRoot(AnchorPane rootExcaption) {
        this.rootException = rootExcaption;
    }

    public void setExceptionController(ExceptionController exceptionController) {
        this.exceptionController = exceptionController;
    }

    public void checkAmountInEngine(String amount) {
        try {
            checkString(amount);
            this.menuEngine.checkAmount(Integer.parseInt(amount));

        } catch (Exception e) {
            exceptionController.setMessageException(e.getMessage());
            Scene scene = new Scene(rootException);
            exceptionStage.setScene(scene);
            exceptionStage.show();
        }
    }


    private void checkString(String amount) throws AmountDifferentThenInteger {
        for (int i = 0; i < amount.length(); i++) {
            if (amount.charAt(i) < '0' || amount.charAt(i) > '9') {
                String message = String.format("the given text : %s is not a number , please write a number that bigger then zero!", amount);
                throw new AmountDifferentThenInteger(message);
            }
        }
    }

    public User Name2User(String userName) {
        return menuEngine.getUsers().getUsersList().get(userName);
    }

    public void ApplyNotOk(String message)
    {
        exceptionController.setMessageException(message);
        Scene scene = new Scene(rootException);
        exceptionStage.setScene(scene);
        exceptionStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished( event -> exceptionStage.close() );
        delay.play();
    }

    public void collectDateFromXml(Consumer<User> consumerUser,Consumer<Map.Entry<String ,Integer>> consumerStock , Collection<User> users,Map<String,Integer> stocks, Runnable onFinish) {

        currentRunningTask = new collectDateFromXmlTask(filePath.get(),stocks,users,consumerUser,consumerStock); //need to create it

        mainController.bindTaskToUIComponents(currentRunningTask,onFinish);

        new Thread(currentRunningTask).start();

    }

    public void updateAllUsers(Users users)
    {
        users.getUsersList().values().forEach(user->mainController.updateDataAfterTrade(user));
    }


    /*
    public Map<String,Integer> createSymbol2Price ()
    {

        Map<String,Integer> symbol2price = new HashMap<>();
        for(Map.Entry<String, Stock> curr : menuEngine.getTempStock().entrySet())
        {
            symbol2price.put(curr.getKey(),curr.getValue().getStockPrice());
        }
        return symbol2price ;
    }
    */
}
