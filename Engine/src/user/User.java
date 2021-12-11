package user;

import UserDto.UserDto;
import scheme2.genreteClasses.RseUser;
import stock.Stocks;

public class User {
    private String nameOfUser ;
    private Holdings holdings ;
    private int TotalValueHoldings ;
    private UserDto userDto;


    public User(RseUser user , Stocks stocks) {
        this.nameOfUser = user.getName();
        holdings = new Holdings(user.getRseHoldings() , stocks);
        TotalValueHoldings = holdings.calTotalValHoldings();
        this.userDto = new UserDto(this.nameOfUser ,this.holdings , this.TotalValueHoldings);   /**/
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public Holdings getHoldings() {
        return holdings;
    }

    public void setHoldings(Holdings holdings) {
        this.holdings = holdings;
    }

    public int getTotalValueHoldings() {
        return TotalValueHoldings;
    }

    public void setTotalValueHoldings(int totalValueHoldings) {
        TotalValueHoldings = totalValueHoldings;
    }
}

