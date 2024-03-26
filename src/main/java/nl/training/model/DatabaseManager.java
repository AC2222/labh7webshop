package nl.training.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private static int nextItemId= 0;
    private Map<Integer, Item> items = new HashMap<>();
    private static int nextUserId= 0;
    private Map<Integer, User> users = new HashMap<>();
    private static int nextOrderId= 0;
    private Map<Integer, Order> orders = new HashMap<>();
    private static int nextWebshopId= 0;
    private Map<Integer, Webshop> webshops = new HashMap<>();
    private Map<Integer,Map<Integer,Item>> itemsPerYear = new HashMap<>();

    public void genDummyData(){
        int n = 1000;
        int n2 = 10;
        Webshop webshop = new Webshop(nextWebshopId++);
        webshops.put(webshop.getId(), webshop);
        for (int i =0;i<n;i++) {
            Calendar cal = Calendar.getInstance();
            cal.set((int) (Math.random() * 23 + 2000), (int) (Math.random() * 12 + 1), (int) (Math.random() * 20 + 1));
            addItem(new Money(Math.random() * 100), "test"+i, cal,webshop.getId());
        }
        for (int i =0;i<n2;i++) {
            addUser("test"+i,"test", webshop.getId());
        }
        System.out.println("dummy loaded");
    }

    private void addUser(String name, String residence, int webshopId) {
        User user = new User(name, residence, nextUserId++);
        users.put(user.getId(),user);
        webshops.get(webshopId).getUsers().add(user.getId());
    }

    public void addItem(Money money, String desc, Calendar date, int webshopId){
        if (webshops.containsKey(webshopId)) {
            Item item = new Item(money, desc, date, nextItemId++);
            items.put(item.getId(), item);
            int year = item.getDate().get(Calendar.YEAR);
            if (!itemsPerYear.containsKey(year)) {
                itemsPerYear.put(year, new HashMap<>());
            }
            itemsPerYear.get(year).put(item.getId(), item);
            webshops.get(webshopId).addItem(item.getId(), item.getDate());
        }
    }

    public Item getItem(int id){
        return items.get(id);
    }

    public User getUser(int id){
        return users.get(id);
    }

    public Webshop getWebshop(int id){
        return webshops.get(id);
    }

    public Order getOrder(int id){
        return orders.get(id);
    }

    public Map<Integer, Item> getItems() {
        return items;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public Map<Integer, Order> getOrders() {
        return orders;
    }

    public Map<Integer, Webshop> getWebshops() {
        return webshops;
    }

    public Map<Integer, Map<Integer, Item>> getItemsPerYear() {
        return itemsPerYear;
    }

    public static int getNextItemId() {
        return nextItemId;
    }

    public static void setNextItemId(int nextItemId) {
        DatabaseManager.nextItemId = nextItemId;
    }

    public static int getNextUserId() {
        return nextUserId;
    }

    public static void setNextUserId(int nextUserId) {
        DatabaseManager.nextUserId = nextUserId;
    }

    public static int getNextOrderId() {
        return nextOrderId;
    }

    public static void setNextOrderId(int nextOrderId) {
        DatabaseManager.nextOrderId = nextOrderId;
    }

    public static int getNextWebshopId() {
        return nextWebshopId;
    }

    public static void setNextWebshopId(int nextWebshopId) {
        DatabaseManager.nextWebshopId = nextWebshopId;
    }
}
