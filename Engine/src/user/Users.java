package user;

import Exceptions.SymbolNotExists;
import Exceptions.UserNameIsAlreadyExists;
import UserDto.UsersDto;
import scheme2.genreteClasses.RseItem;
import scheme2.genreteClasses.RseUser;
import stock.Stock;
import stock.Stocks;

import javax.swing.text.StyledEditorKit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Users {

   private Map<String,User> usersList ;

    public Users() {
        usersList = new HashMap<>();
    }

    public Map<String,User> createUsersList(List<RseUser> rseUsers , Stocks stocks) throws UserNameIsAlreadyExists, SymbolNotExists {
        Map<String,User>  tempMap = new HashMap<>();
        for(RseUser currUser : rseUsers)
        {
            if(checkUserName(currUser.getName(),tempMap)) //check if the current user is already exist in the XML file
            {
                if(checkUserHoldings(currUser.getRseHoldings().getRseItem() , stocks , currUser.getName())) {
                    tempMap.put(currUser.getName(), new User(currUser , stocks));
                }
            }
        }
        return tempMap ;
    }


    public Boolean checkUserHoldings(List<RseItem> ListRseItem ,Stocks stocks ,String userName ) throws SymbolNotExists {
        for( RseItem currItem : ListRseItem)
        {
            if(!stocks.checkSymbolInStocks(currItem.getSymbol())) {
                String message = String.format("sorry " + userName + "! the symbol name: %s is not exist in our stocks", currItem.getSymbol());
                throw new SymbolNotExists(message);
            }
        }
        return true ; //all user symbols exist in our stocks
    }

    public Boolean checkUserName(String userName,Map<String,User>  tempMap) throws UserNameIsAlreadyExists {
        for (String currUserName : tempMap.keySet()) {
            if (userName.equalsIgnoreCase(currUserName))
            {
                String message = String.format("the user name: %s is already exist in the current XML file", currUserName);
                throw new UserNameIsAlreadyExists(message);
            }
        }
        return true;
    }

    public Map<String, User> getUsersList() {
        return usersList;
    }

    public User symbol2User(String symbolName)
    {
        return usersList.get(symbolName);
    }

    public void setUsersList(Map<String, User> usersList) {
        this.usersList = usersList;
    }

    public void clear() {
        this.usersList.clear();
    }
}
