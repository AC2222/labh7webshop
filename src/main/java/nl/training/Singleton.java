package nl.training;

import nl.training.model.*;
import nl.training.programFunctions.Controller;

public class Singleton {
    private static Singleton singleton = new Singleton();
    private DatabaseManager databaseManager = new DatabaseManager();
    private ItemRepo itemRepo = new ItemRepo();
    private OrderRepo orderRepo = new OrderRepo();
    private UserRepo userRepo = new UserRepo();
    private WebshopRepo webshopRepo = new WebshopRepo();
    private Controller controller = new Controller();

    public static Singleton getSingleton() {
        return singleton;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ItemRepo getItemRepo() {
        return itemRepo;
    }

    public OrderRepo getOrderRepo() {
        return orderRepo;
    }

    public UserRepo getUserRepo() {
        return userRepo;
    }

    public WebshopRepo getWebshopRepo() {
        return webshopRepo;
    }

    public Controller getController() {
        return controller;
    }
}
