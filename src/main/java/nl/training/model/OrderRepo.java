package nl.training.model;

import nl.training.Exceptions.NoSuchOrderException;
import nl.training.Singleton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OrderRepo {
    private Map<Integer, Order> orders = new HashMap<>();

    public void loadAll() {
        for (Order order : Singleton.getSingleton().getDatabaseManager().getOrders().values()) {
            orders.put(order.getId(), order);
        }
    }

    public void writeAll() {

    }

    public void addOrder(Calendar date, int userId) {
        Order order = new Order(date, userId, DatabaseManager.getNextOrderId());
        orders.put(DatabaseManager.getNextOrderId(), order);
        DatabaseManager.setNextOrderId(order.getId() + 1);
    }

    public Order getOrder(int id) {
        if (orders.containsKey(id)) {
            return orders.get(id);
        } else {
            throw new NoSuchOrderException();
        }
    }

    public void addItemToOrder(int orderId, int itemId, int itemCount, Money itemPrice) {
        if (orders.containsKey(orderId)) {
            orders.get(orderId).addItem(itemId, itemCount, itemPrice);
        } else {
            throw new NoSuchOrderException();
        }
    }

    public Map<Integer, Integer> getItemsAndCounts(int orderId) {
        if (orders.containsKey(orderId)) {
            return orders.get(orderId).getItemsCount();
        } else {
            throw new NoSuchOrderException();
        }
    }
}
