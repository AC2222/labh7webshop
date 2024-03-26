package nl.training.model;

import nl.training.Exceptions.NoSuchUserException;
import nl.training.Exceptions.NoSuchWebshopException;
import nl.training.Singleton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebshopRepo {
    private HashMap<Integer,Webshop> webshops = new HashMap<>();


    public void loadAll(){
        for(Webshop webshop : Singleton.getSingleton().getDatabaseManager().getWebshops().values()){
            webshops.put(webshop.getId(),webshop);
        }
    }

    public Set<Integer> getWebshopIds(){
        return webshops.keySet();
    }

    public void writeAll(){

    }

    public void addWebshop(){
        Webshop webshop = new Webshop(DatabaseManager.getNextUserId());
        webshops.put(DatabaseManager.getNextWebshopId(),webshop);
        DatabaseManager.setNextWebshopId(webshop.getId()+1);
    }

    public Webshop getWebshop(int id){
        if (webshops.containsKey(id)) {
            return webshops.get(id);
        }else{
            throw new NoSuchWebshopException();
        }
    }

    public void addUserToWebshop(int webshopId, int userId){
        if (webshops.containsKey(webshopId)) {
            webshops.get(webshopId).getUsers().add(userId);
        }else{
            throw new NoSuchWebshopException();
        }
    }

    public Set<Integer> getUsers(int webshopId){
        if (webshops.containsKey(webshopId)) {
            return webshops.get(webshopId).getUsers();
        }else{
            throw new NoSuchWebshopException();
        }
    }

    public void addItem(int webshopId,int itemId, Calendar date){
        if (webshops.containsKey(webshopId)) {
            webshops.get(webshopId).addItem(itemId, date);
        }else{
            throw new NoSuchWebshopException();
        }
    }

    public Set<Integer> getItems(int webshopId){
        if (webshops.containsKey(webshopId)) {
            return webshops.get(webshopId).getItems();
        }else{
            throw new NoSuchWebshopException();
        }
    }

    public Set<Integer> getItemsPerYear(int webshopId, int year){
        if (webshops.containsKey(webshopId)) {
            return webshops.get(webshopId).getCatalogPerYear().get(year);
        }else{
            throw new NoSuchWebshopException();
        }
    }
}
