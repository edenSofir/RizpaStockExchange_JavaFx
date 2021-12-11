package user;

import Exceptions.SymbolNotExists;
import command.Enum.TradingTypes;
import scheme2.genreteClasses.RseHoldings;
import scheme2.genreteClasses.RseItem;
import stock.Stock;
import stock.Stocks;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Holdings {

    private List<Item> userHoldings ;
    private Stocks allStocks ;

    public Holdings(RseHoldings rseHoldings , Stocks stocks) {
        userHoldings = new LinkedList<>();
        allStocks = stocks ;
        createUserHoldings(rseHoldings.getRseItem());
    }


    public void createUserHoldings (List<RseItem> itemListUser)
    {
        for(RseItem currItem : itemListUser)
        {
            userHoldings.add(new Item(currItem.getSymbol(),currItem.getQuantity()));
        }
    }

    public List<Item> getUserHoldings() {
        return userHoldings;
    }

    public int calTotalValHoldings()  {
        int TotalVal = 0 ;
        try {
            for (Item currItem : userHoldings) {
                Stock stock = allStocks.checkSymbolInAllStocks(currItem.getStockSymbol());
                TotalVal = TotalVal + (stock.getStockPrice()*currItem.getQuantity());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return TotalVal ;
    }

    public void updateHoldings(TradingTypes tradingType, String symbolName , int quantity)
    {
        Boolean flag = false ;

        for(Item item : userHoldings)
        {
            if(item.getStockSymbol().equals(symbolName))
            {
                if(tradingType == TradingTypes.BUY) {
                    item.setQuantity(item.getQuantity() + quantity);
                    flag = true;
                }
                else
                {
                    if(item.getQuantity() - quantity == 0 )
                        userHoldings.remove(item);
                    else
                        item.setQuantity(item.getQuantity() - quantity);
                    flag = true ;
                }
            }
        }
        if(!flag) //the stock dose not exists in the list the user must buy - add to the holdings
        {
            userHoldings.add(new Item(symbolName, quantity));
        }

    }

}
