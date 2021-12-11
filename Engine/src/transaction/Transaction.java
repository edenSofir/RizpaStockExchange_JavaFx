package transaction;

import command.Command;
import command.Enum.TradingTypes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionDate;
    private int amount;
    private int price;
    private int totalValue;
    private String buyerName;
    private String sellerName;


    public Transaction(String transactionDate, int amount, int price, int totalValue, Command counterCommand, Command currCommand) {
        this.transactionDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        this.amount = amount;
        this.price = price;
        this.totalValue = totalValue;
        findInitiators(counterCommand, currCommand);
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public Transaction() {
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionDate='" + transactionDate + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", totalValue=" + totalValue +
                ", buyerName='" + buyerName + '\'' +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }

    private void findInitiators(Command counterCommand, Command currCommand) {
        if (counterCommand.getTradingT() == TradingTypes.BUY) {
            this.buyerName = counterCommand.getInitiatorName();
            this.sellerName = currCommand.getInitiatorName();
        } else {

            this.buyerName = currCommand.getInitiatorName();
            this.sellerName = counterCommand.getInitiatorName();
        }
    }

}
