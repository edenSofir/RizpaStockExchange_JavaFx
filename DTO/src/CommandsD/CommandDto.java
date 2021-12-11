package CommandsD;

import command.Enum.CommandTypes;
import command.Enum.TradingTypes;

public class CommandDto {


    private final TradingTypes tradingT;
    private final CommandTypes commandT;
    private final String symbol;
    private final int price;
    private final int stockAmount;
    private final String commandDate;
    private final String InitiatorName ;

    public CommandDto(TradingTypes tradingT, CommandTypes commandT, String symbol, int price, int stockAmount, String commandDate,String initiatorName) {
        this.tradingT = tradingT;
        this.commandT = commandT;
        this.symbol = symbol;
        this.price = price;
        this.stockAmount = stockAmount;
        this.commandDate = commandDate;
        this.InitiatorName = initiatorName ;
    }

    public TradingTypes getTradingT() {
        return tradingT;
    }

    public CommandTypes getCommandT() {
        return commandT;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrice() {
        return price;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public String getCommandDate() {
        return commandDate;
    }

    @Override
    public String toString() {
        return "CommandDto{" +
                "tradingT=" + tradingT +
                ", commandT=" + commandT +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", stockAmount=" + stockAmount +
                ", commandDate='" + commandDate + '\'' +
                ", InitiatorName='" + InitiatorName + '\'' +
                '}';
    }
}
