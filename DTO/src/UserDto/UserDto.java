package UserDto;

import user.Holdings;

public class UserDto {

    private final String nameOfUser;
    private final Holdings holdings;
    private final int TotalValueHoldings;

    public UserDto(String nameOfUser, Holdings currHoldings, int TotalValueHoldings) {
        this.nameOfUser = nameOfUser;
        this.holdings = currHoldings;
        this.TotalValueHoldings = TotalValueHoldings;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public Holdings getHoldings() {
        return holdings;
    }

    public int getTotalValueHoldings() {
        return TotalValueHoldings;
    }
}