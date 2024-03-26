package nl.training.model;

import java.util.*;

public class Order {
    private int id;
    private int userId;
    private Money totalPrice = new Money("0");
    private Set<Integer> items;
    private Map<Integer, Integer> itemsCount;
    private Calendar date;

    public Order(Calendar date, int userId, int id) {
        this.date = date;
        this.userId = userId;
        this.id = id;
        items = new HashSet<>();
        itemsCount = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public Set<Integer> getItems() {
        return items;
    }

    public Map<Integer, Integer> getItemsCount() {
        return itemsCount;
    }

    public Calendar getDate() {
        return date;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    /*public int getId() {
        return id;
    }*/

    public void addItem(int itemId, int count, Money itemPrice) {
        if (items.contains(itemId)) {
            itemsCount.replace(itemId, itemsCount.get(itemId) + count);
        } else {
            items.add(itemId);
            itemsCount.put(itemId, count);
        }
        totalPrice = totalPrice.add(itemPrice.mult(new Money(count)));
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", items=" + items +
                ", itemsCount=" + itemsCount +
                ", date=" + date.getTime() +
                ", totalPrice=" + totalPrice +
                "}\n";
    }

    /*
    public void printOrder() {
        String result = "order id : " + id + " user id : " + userId + " date : " + date.getTime().toString() + " totalprice : " + getTotalPrice() + "\n";
        for (Item i : items.values()) {
            result += "[ item id : " + i.getId() + " count : " + itemsCount.get(i.getId()) + "], ";
        }
        System.out.println(result);
    }

    public BigDecimal getTotalPrice() {
        BigDecimal result = new BigDecimal(0);
        for (Item i : items.values()) {
            result = result.add(i.getPrice().multiply(new BigDecimal(itemsCount.get(i.getId())))).setScale(2, RoundingMode.HALF_UP);
        }
        return result;
    }*/
}
