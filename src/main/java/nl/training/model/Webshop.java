package nl.training.model;

import java.math.BigDecimal;
import java.util.*;

public class Webshop {
    private int id;
    private Set<Integer> items= new HashSet<>();
    private Set<Integer> users= new HashSet<>();
    private Map<Integer, Set<Integer>> catalogPerYear = new HashMap<>();

    public Webshop(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Integer> getItems() {
        return items;
    }

    public void setItems(Set<Integer> items) {
        this.items = items;
    }

    public Set<Integer> getUsers() {
        return users;
    }

    public void setUsers(Set<Integer> users) {
        this.users = users;
    }

    public Map<Integer, Set<Integer>> getCatalogPerYear() {
        return catalogPerYear;
    }

    public void setCatalogPerYear(Map<Integer, Set<Integer>> catalogPerYear) {
        this.catalogPerYear = catalogPerYear;
    }

    public void addItem(int itemId, Calendar date) {
        items.add(itemId);
        int year = date.get(Calendar.YEAR);
        if (!catalogPerYear.containsKey(year)) {
            catalogPerYear.put(year, new HashSet<>());
        }
        catalogPerYear.get(year).add(itemId);
    }

    @Override
    public String toString() {
        return "Webshop{" +
                "id=" + id +
                ", users=" + users +
                ", items=" + items +
                "}\n";
    }

    /*public void addUser(String name, String residence) {
        User user = new User(name, residence);
        users.put(user.getId(), user);
    }*/

    /*public void addOrder(int userID, Calendar date) {
        Order order = new Order(date, userID);
        orders.put(order.getId(), order);
        users.get(userID).addOrder(order);

    }*/

    /*public void addItemToOrder(int orderID, int itemID, int count) {
        orders.get(orderID).addItem(items.get(itemID), count);
    }*/

    /*public void printWebshop() {
        System.out.println("webshop: ");
        System.out.println("    Items: ");
        for (int y:catalogPerYear.keySet()){
            System.out.println("        year: "+y);
            for (Item i : catalogPerYear.get(y).values()){
                i.printItem();
            }
        }
        System.out.println("users : ");
        for (User u : users.values()){
            u.printUser();
        }
        System.out.println("orders : ");
        for (Order o : orders.values()){
            o.printOrder();
        }

    }*/

}
