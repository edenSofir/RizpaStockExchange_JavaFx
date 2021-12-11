package UserDto;

import StocksD.StocksDto;
import user.Item;

import java.util.List;

public class HoldingsDto {
    private final List<ItemDto> userHoldings ;
    private final StocksDto allStocks ;

   public HoldingsDto(List<ItemDto> currUserHoldings , StocksDto currStocks)
   {
       this.allStocks = currStocks ;
       this.userHoldings = currUserHoldings ;
   }

    public List<ItemDto> getUserHoldings() {
        return userHoldings;
    }

    public StocksDto getAllStocks() {
        return allStocks;
    }
}
