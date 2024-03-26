package nl.training.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {
    private int id;
    private Set<Integer> orders;
    private String name;
    private String residence;

    public User(String name, String residence, int id) {
        this.name = name;
        this.residence = residence;
        this.id = id;
        orders = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Integer> getOrders() {
        return orders;
    }

    public void setOrders(Set<Integer> orders) {
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", orders=" + orders +
                ", name='" + name + '\'' +
                ", residence='" + residence + '\'' +
                "}\n";
    }

    /*public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getResidence() {
        return residence;
    }

    public void addOrder(Order order) {
        orders.put(order.getId(), order);
    }

    public void printUser() {
        String result = "userid : " + id + " username : " + name + " residence : " + residence + " Orders : ";
        for (Order o : orders.values()) {
            result += " [Order Id :" + o.getId() + "], ";
        }
        System.out.println(result);
    }*/
}
