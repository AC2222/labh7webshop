package nl.training.model;

import nl.training.Exceptions.NoSuchOrderException;
import nl.training.Exceptions.NoSuchUserException;
import nl.training.Singleton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserRepo {
    private Map<Integer,User> users = new HashMap<>();

    public void loadAll(){
        for(User user : Singleton.getSingleton().getDatabaseManager().getUsers().values()){
            users.put(user.getId(),user);
        }
    }

    public void writeAll(){

    }

    public void addUser(String name , String residence){
        User user = new User(name, residence, DatabaseManager.getNextUserId());
        users.put(DatabaseManager.getNextUserId(),user);
        DatabaseManager.setNextUserId(user.getId()+1);
    }

    public User getUser(int id){
        if (users.containsKey(id)) {
            return users.get(id);
        }else{
            throw new NoSuchUserException();
        }
    }

    public void addOrderToUser(int userId, int orderId){
        if (users.containsKey(userId)) {
            users.get(userId).getOrders().add(orderId);
        }else{
            throw new NoSuchUserException();
        }
    }

    public Set<Integer> getOrders(int userId){
        if (users.containsKey(userId)) {
            return users.get(userId).getOrders();
        }else{
            throw new NoSuchUserException();
        }
    }
}
