package nl.training.model;

import nl.training.Exceptions.NoSuchItemException;
import nl.training.Singleton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ItemRepo {
    private Map<Integer,Item> items = new HashMap<>();

    public void loadAll(){
        for(Item item : Singleton.getSingleton().getDatabaseManager().getItems().values()){
            items.put(item.getId(),item);
        }
    }

    public void writeAll(){

    }

    public void addItem(Money price, String disc, Calendar date){
        Item item = new Item(price,disc,date,DatabaseManager.getNextItemId());
        items.put(DatabaseManager.getNextItemId(),item);
        DatabaseManager.setNextItemId(item.getId()+1);
    }

    public Item getItem(int id){
        if(items.containsKey(id)) {
            return items.get(id);
        }else{
            throw new NoSuchItemException();
        }
    }
}
