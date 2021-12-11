package command;

import TransactionD.TransactionDto;
import TransactionD.TransactionsDto;
import command.Enum.TradingTypes;
import stock.Stock;
import transaction.Transaction;
import transaction.Transactions;
import user.User;
import user.Users;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Commands {

    private List<Command> commandsBuyList;
    private List<Command> commandsSellList;
    private int TotalCycleBuyCommand;
    private int TotalCycleSellCommand;
    private TransactionsDto transactionsDto;

    public Commands() {
        commandsSellList = new LinkedList<>();
        commandsBuyList = new LinkedList<>();
        TotalCycleBuyCommand = 0;
        TotalCycleSellCommand = 0;
    }

    public int getTotalCycleBuyCommand() {
        return TotalCycleBuyCommand;
    }

    public void setTotalCycleBuyCommand(int totalCycleBuyCommand) {
        TotalCycleBuyCommand = totalCycleBuyCommand;
    }

    public int getTotalCycleSellCommand() {
        return TotalCycleSellCommand;
    }

    public void setTotalCycleSellCommand(int totalCycleSellCommand) {
        TotalCycleSellCommand = totalCycleSellCommand;
    }

    public List<Command> getCommandsBuyList() {
        return commandsBuyList;
    }

    public void setCommandsBuyList(List<Command> commandsBuyList) {
        this.commandsBuyList = commandsBuyList;
    }

    public List<Command> getCommandsSellList() {
        return commandsSellList;
    }

    public void setCommandsSellList(List<Command> commandsSellList) {
        this.commandsSellList = commandsSellList;
    }

    public TransactionsDto compareSellOrBuyLmt(int tradingType, Command currCommand, Stock currStock, Users users) {
        switch (tradingType) {
            case 1://i'm buying
                return createMatchForLmt(currStock.getCommands().commandsSellList, currCommand, currStock, tradingType, users);
            case 2: //i'm selling
                return createMatchForLmt(currStock.getCommands().commandsBuyList, currCommand, currStock, tradingType, users);
        }
        return null;
    }

    public TransactionsDto compareSellOrBuyMkt(int tradingType, Command currCommand, Stock currStock, Users users) {
        switch (tradingType) {
            case 1:
                return createMatchForMkt(currStock.getCommands().commandsSellList, currCommand, currStock, tradingType, users);
            case 2:
                return createMatchForMkt(currStock.getCommands().commandsBuyList, currCommand, currStock, tradingType, users);
        }
        return null;
    }

    public TransactionsDto createMatchForMkt(List<Command> commandList, Command currCommand, Stock currStock, int tradingType, Users users) {
        List<Transaction> newTransections = new LinkedList<>() ;
        int totalAmount = 0;
        Iterator<Command> iterator = commandList.iterator();
        if (commandList.isEmpty()) {
            currStock.putNewCommandInCommands(currCommand, tradingType);

        } else {
            while ((iterator.hasNext()) && (currCommand.getStockAmount() > 0)) { //newCommand = listCommand , currCommand == counterCommand
                Command newCommand = iterator.next();
                User counterUser = users.symbol2User(newCommand.getInitiatorName());
                Transaction transaction = creatTransaction(newCommand, currCommand, currStock);
                counterUser.getHoldings().updateHoldings(newCommand.getTradingT(),newCommand.getSymbol(),transaction.getAmount());//need to do the user current stock - currcomand.getstockamount
                counterUser.setTotalValueHoldings(counterUser.getHoldings().calTotalValHoldings());
                totalAmount = totalAmount + transaction.getAmount();
                newTransections.add(transaction);
                if (newCommand.getStockAmount() == 0) {
                    iterator.remove();
                }
            }
            if (currCommand.getStockAmount() > 0) {
                currStock.putNewCommandInCommands(currCommand, tradingType);
            }
            else
            { //check if freforemed proprely
                User currUser = users.symbol2User(currCommand.getInitiatorName());
                currUser.getHoldings().updateHoldings(currCommand.getTradingT(),currCommand.getSymbol(),totalAmount);
                currUser.setTotalValueHoldings(currUser.getHoldings().calTotalValHoldings());
            }
        }

        return new TransactionsDto(newTransections,0);
    }


    public void SortBuyList() {
        commandsBuyList.sort(new Command.BuyComparator());
    }

    public void SortSellList() {
        commandsSellList.sort(new Command.SellComparator());
    }

    public TransactionsDto createMatchForLmt(List<Command> commandList, Command currCommand, Stock currStock, int tradingType ,Users users) {
        List<Transaction> newTransections = new LinkedList<>() ;
        int totalAmount = 0;
        Iterator<Command> iterator = commandList.iterator();
        if (commandList.isEmpty()) {
            currStock.putNewCommandInCommands(currCommand, tradingType);
        } else {
            while ((iterator.hasNext()) && (currCommand.getStockAmount() > 0)) {
                Command newCommand = iterator.next();
                User newUser = users.symbol2User(newCommand.getInitiatorName());
                if ((newCommand.getPrice() <= currCommand.getPrice()) && tradingType == 1) {
                    Transaction transaction = creatTransaction(newCommand, currCommand, currStock);
                    totalAmount = totalAmount + transaction.getAmount();
                    newUser.getHoldings().updateHoldings(newCommand.getTradingT(),newCommand.getSymbol(), transaction.getAmount());
                    newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings());
                    newTransections.add(transaction);
                    if (newCommand.getStockAmount() == 0) {
                        iterator.remove();
                    }
                } else if ((newCommand.getPrice() >= currCommand.getPrice()) && tradingType == 2) {
                    Transaction transaction = creatTransaction(newCommand, currCommand, currStock);
                    totalAmount = totalAmount + transaction.getAmount();
                    newTransections.add(transaction);
                    if (newCommand.getStockAmount() == 0) {
                        newUser.getHoldings().updateHoldings(newCommand.getTradingT() , newCommand.getSymbol() ,transaction.getAmount());
                        newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings()); //146+147 necessary
                        iterator.remove();
                    }
                    else
                    {
                        newUser.getHoldings().updateHoldings(newCommand.getTradingT() , newCommand.getSymbol() , transaction.getAmount());
                        newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings()); //152+156 necssery
                    }
                }
            }
            if (currCommand.getStockAmount() > 0) {
                currStock.putNewCommandInCommands(currCommand, tradingType);
                User currUser = users.symbol2User(currCommand.getInitiatorName());
                currUser.getHoldings().updateHoldings(currCommand.getTradingT(),currCommand.getSymbol(),totalAmount);
                currUser.setTotalValueHoldings(currUser.getHoldings().calTotalValHoldings());
            }
            else
            {
                User currUser = users.symbol2User(currCommand.getInitiatorName());
                currUser.getHoldings().updateHoldings(currCommand.getTradingT(),currCommand.getSymbol(),totalAmount);
                currUser.setTotalValueHoldings(currUser.getHoldings().calTotalValHoldings());
            }
        }
        return new TransactionsDto(newTransections,0);
    }


    public Transaction creatTransaction(Command CounterOrder, Command currCommand, Stock currStock) {
        Transaction newTransaction = null;
        int finalStockAmount = CounterOrder.getStockAmount() - currCommand.getStockAmount();

        if (finalStockAmount >= 0) {
            if(currCommand.getTradingT().equals(TradingTypes.SELL))
                newTransaction = new Transaction("", currCommand.getStockAmount(), currCommand.getPrice(), currCommand.getStockAmount() * CounterOrder.getPrice(), CounterOrder, currCommand);
            else
                newTransaction = new Transaction("", currCommand.getStockAmount(), CounterOrder.getPrice(), currCommand.getStockAmount() * CounterOrder.getPrice(), CounterOrder, currCommand);
            currStock.putNewTransactionInTransactions(newTransaction);

            currStock.getTransactions().setSumOfCycleTran(currStock.getTransactions().getSumOfCycleTran() + (currCommand.getStockAmount() * CounterOrder.getPrice()));
            CounterOrder.setStockAmount(finalStockAmount);
            currCommand.setStockAmount(0);
            return newTransaction;

        } else {
            newTransaction = new Transaction("", CounterOrder.getStockAmount(), CounterOrder.getPrice(), CounterOrder.getStockAmount() * CounterOrder.getPrice(), CounterOrder, currCommand);
            currStock.putNewTransactionInTransactions(newTransaction);

            currStock.getTransactions().setSumOfCycleTran(currStock.getTransactions().getSumOfCycleTran() + (CounterOrder.getStockAmount() * CounterOrder.getPrice()));
            CounterOrder.setStockAmount(0);
            currCommand.setStockAmount(finalStockAmount * (-1));
            return newTransaction;
        }

    }

    @Override
    public String toString() {
        return "The list of commands are: " +
                "commandsBuyList: " + commandsBuyList +
                "\n commandsSellList=" + commandsSellList;
    }
}


