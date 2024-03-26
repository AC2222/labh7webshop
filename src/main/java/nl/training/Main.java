package nl.training;

import nl.training.model.Webshop;
import nl.training.userinterface.UserInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        /*Webshop webshop = new Webshop();
        dummyData(webshop,100,5);
        webshop.printWebshop();
        Calendar cal = Calendar.getInstance();
        cal.set((int) (Math.random() * 10 + 2000), (int) (Math.random() * 12 + 1), (int) (Math.random() * 20 + 1));
        webshop.addOrder(0, cal);
        webshop.addItemToOrder(0,0,10);
        webshop.addItemToOrder(0,9,10);
        webshop.printWebshop();*/

        UserInterface userInterface = new UserInterface();
        Singleton.getSingleton().getDatabaseManager().genDummyData();
        Singleton.getSingleton().getWebshopRepo().loadAll();
        Singleton.getSingleton().getItemRepo().loadAll();
        Singleton.getSingleton().getUserRepo().loadAll();
        Singleton.getSingleton().getOrderRepo().loadAll();
        userInterface.start();
    }
}