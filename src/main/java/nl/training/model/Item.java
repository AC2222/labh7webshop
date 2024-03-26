package nl.training.model;

import java.math.BigDecimal;
import java.util.Calendar;

public class Item {
    private int id;
    private Money price;
    private String description;
    private Calendar date;

    public Item(Money price, String description, Calendar date, int id) {
        this.price = price;
        this.description = description;
        this.date = date;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", date=" + date.getTime() +
                "}\n";
    }

    /*
    public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getDate() {
        return date;
    }

    public void printItem() {
        System.out.println("Item id : " + id + " price : " + price + " date : " + date.getTime().toString() + " discription : " + description);
    }

     */
}
