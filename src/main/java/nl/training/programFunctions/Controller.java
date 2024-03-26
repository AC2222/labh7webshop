package nl.training.programFunctions;

import nl.training.Exceptions.NoSuchItemException;
import nl.training.Exceptions.NoSuchOrderException;
import nl.training.Exceptions.NoSuchUserException;
import nl.training.Exceptions.NoSuchWebshopException;
import nl.training.Singleton;
import nl.training.model.*;

import java.util.*;

public class Controller {

    public static String order(boolean valid, int webshopId, int userId, int itemId, int count, int orderId) {
        Singleton s = Singleton.getSingleton();
        StringBuilder result = new StringBuilder();
        boolean inputValid = valid;
        Webshop webshop = tryFindWebShop(result, webshopId);
        User user = tryFindUser(result, userId);
        Item item = tryFindItem(result, itemId);
        if (webshop == null || user == null || item == null) {
            inputValid = false;
        }
        if (count < 1 && valid) {
            inputValid = false;
            result.append("count cannot be smaller than 1.\n");
        }
        if (webshop != null && user != null && !webshop.getUsers().contains(userId)) {
            inputValid = false;
            result.append("user not found in webshop.\n");
        }
        if (webshop != null && item != null && !webshop.getItems().contains(itemId)) {
            inputValid = false;
            result.append("item not found in webshop.\n");
        }
        if (orderId == DatabaseManager.getNextOrderId()) {
            if (inputValid) {
                s.getOrderRepo().addOrder(Calendar.getInstance(), userId);
                s.getUserRepo().addOrderToUser(userId, orderId);
                DatabaseManager.setNextOrderId(orderId + 1);
                result.append("added order with id :").append(orderId).append(".\n");
            }
        }
        if (inputValid) {
            if (user.getOrders().contains(orderId)) {
                Order order = tryFindOrder(result, orderId);
                if (order != null) {
                    order.addItem(itemId, count, item.getPrice());
                    result.append("added item to order.\n");
                }
            } else {
                result.append("order does not belong to user.\n");
            }
        }
        return result.toString();
    }

    public static String order(boolean valid, int webshopId, int userId, int itemId, int count) {
        return order(valid, webshopId, userId, itemId, count, DatabaseManager.getNextOrderId());
    }

    public static String getCatalogString(boolean valid, int webshopId, int year, SortingEnum sort, boolean ascending) {
        StringBuilder result = new StringBuilder();
        Set<Item> itemsSet = null;
        boolean isValid = valid;
        try {
            itemsSet = getCatalogPerYear(webshopId, year);
        } catch (RuntimeException e) {
            if (e instanceof NoSuchWebshopException) {
                result.append("no Webshop with id: ").append(webshopId).append(".\n");
                isValid = false;
            } else if (e instanceof NoSuchItemException) {
                result.append("webshop with id : ").append(webshopId).append(" broken, possessing nonexistent item id.\n");
                isValid =false;
            } else {
                result.append("unexpected error from getCatalogPerYear.\n");
                isValid = false;
            }
        }
        if (isValid) {
            if (itemsSet.isEmpty()) {
                result.append("no items found.\n");
            } else {
                result.append("items for year: ").append(year).append(" from webshop: ").append(webshopId).append(".\n");
                List<Item> itemList = sortCatalogSet(itemsSet, sort, ascending);
                for (Item item : itemList) {
                    result.append(item);
                }
            }
        }
        return result.toString();
    }

    public static String getCatalogString(boolean valid, int webshopId, int year, SortingEnum sort) {
        return getCatalogString(valid, webshopId, year, sort, true);
    }

    public static String getCatalogString(boolean valid, int webshopId, SortingEnum sort, boolean ascending) {
        StringBuilder result = new StringBuilder();
        Set<Item> itemsSet = null;
        boolean isValid = valid;
        try {
            itemsSet = getCatalog(webshopId);
        } catch (RuntimeException e) {
            if (e instanceof NoSuchWebshopException) {
                result.append("no Webshop with id: ").append(webshopId).append(".\n");
                isValid = false;
            } else if (e instanceof NoSuchItemException) {
                result.append("webshop with id : ").append(webshopId).append(" broken, possessing nonexistent item id.\n");
                isValid = false;
            } else {
                result.append("unexpected error from getCatalog.\n");
                isValid = false;
            }
        }
        if (isValid) {
            if (itemsSet.isEmpty()) {
                result.append("no items found.\n");
            } else {
                result.append("items from webshop: ").append(webshopId).append(".\n");
                List<Item> itemList = sortCatalogSet(itemsSet, sort, ascending);
                for (Item item : itemList) {
                    result.append(item);
                }
            }
        }
        return result.toString();
    }

    public static String getCatalogString(boolean valid, int webshopId, SortingEnum sort) {
        return getCatalogString(valid, webshopId, sort, true);
    }

    public static String getCatalogString(boolean valid, int webshopId, int year, boolean ascending) {
        return getCatalogString(valid, webshopId, year, SortingEnum.ID, ascending);
    }

    public static String getCatalogString(boolean valid, int webshopId, int year) {
        return getCatalogString(valid, webshopId, year, true);
    }

    public static String getCatalogString(boolean valid, int webshopId, boolean ascending) {
        return getCatalogString(valid, webshopId, SortingEnum.ID, ascending);
    }

    public static String getCatalogString(boolean valid, int webshopId) {
        return getCatalogString(valid, webshopId, true);
    }

    private static Set<Item> getCatalog(int webshopId) {
        Set<Item> result = new HashSet<>();
        Set<Integer> intSet = Singleton.getSingleton().getWebshopRepo().getWebshop(webshopId).getItems();
        for (int i : intSet) {
            result.add(Singleton.getSingleton().getItemRepo().getItem(i));
        }
        return result;
    }

    private static Set<Item> getCatalogPerYear(int webshopId, int year) {
        Set<Item> result = new HashSet<>();
        Set<Integer> intSet = Singleton.getSingleton().getWebshopRepo().getWebshop(webshopId).getCatalogPerYear().get(year);
        if (intSet != null) {
            for (int i : intSet) {
                result.add(Singleton.getSingleton().getItemRepo().getItem(i));
            }
        }
        return result;
    }

    private static List<Item> sortCatalogSet(Set<Item> in, SortingEnum methode, boolean ascending) {
        Map<Comparable, List<Object>> sortableMap = new HashMap<>();
        if (methode == SortingEnum.ID) {
            for (Item i : in) {
                if (sortableMap.containsKey(i.getId())) {
                    sortableMap.get(i.getId()).add(i);
                } else {
                    sortableMap.put(i.getId(), new ArrayList<>(List.of(new Item[]{i})));
                }
            }
        } else if (methode == SortingEnum.DATE) {
            for (Item i : in) {
                if (sortableMap.containsKey(i.getDate())) {
                    sortableMap.get(i.getDate()).add(i);
                } else {
                    sortableMap.put(i.getDate(), new ArrayList<>(List.of(new Item[]{i})));
                }
            }
        } else {
            for (Item i : in) {
                if (sortableMap.containsKey(i.getPrice())) {
                    sortableMap.get(i.getPrice()).add(i);
                } else {
                    sortableMap.put(i.getPrice(), new ArrayList<>(List.of(new Item[]{i})));
                }
            }
        }
        List<Item> sortableItemMap = new ArrayList<>();
        for (Object l : sortMap(sortableMap, ascending)) {
            for (Object o : (List<Object>) l) {
                sortableItemMap.add((Item) o);
            }
        }
        return sortableItemMap;
    }

    private static List<Order> sortOrderSet(Set<Order> in, SortingEnum methode, boolean ascending) {
        Map<Comparable, List<Object>> sortableMap = new HashMap<>();
        if (methode == SortingEnum.ID) {
            for (Order i : in) {
                if (sortableMap.containsKey(i.getId())) {
                    sortableMap.get(i.getId()).add(i);
                } else {
                    sortableMap.put(i.getId(), new ArrayList<>(List.of(new Order[]{i})));
                }
            }
        } else if (methode == SortingEnum.DATE) {
            for (Order i : in) {
                if (sortableMap.containsKey(i.getDate())) {
                    sortableMap.get(i.getDate()).add(i);
                } else {
                    sortableMap.put(i.getDate(), new ArrayList<>(List.of(new Order[]{i})));
                }
            }
        } else {
            for (Order i : in) {
                if (sortableMap.containsKey(i.getTotalPrice())) {
                    sortableMap.get(i.getTotalPrice()).add(i);
                } else {
                    sortableMap.put(i.getTotalPrice(), new ArrayList<>(List.of(new Order[]{i})));
                }
            }
        }
        List<Order> sortableItemMap = new ArrayList<>();
        for (Object l : sortMap(sortableMap, ascending)) {
            for (Object o : (List<Object>) l) {
                sortableItemMap.add((Order) o);
            }
        }
        return sortableItemMap;
    }

    private static List<User> sortUserSet(Set<User> in, SortingEnum methode, boolean ascending) {
        Map<Comparable, List<Object>> sortableMap = new HashMap<>();
        if (methode == SortingEnum.ID) {
            for (User i : in) {
                if (sortableMap.containsKey(i.getId())) {
                    sortableMap.get(i.getId()).add(i);
                } else {
                    sortableMap.put(i.getId(), new ArrayList<>(List.of(new User[]{i})));
                }
            }

        } else if (methode == SortingEnum.NAME) {
            for (User i : in) {
                if (sortableMap.containsKey(i.getName())) {
                    sortableMap.get(i.getName()).add(i);
                } else {
                    sortableMap.put(i.getName(), new ArrayList<>(List.of(new User[]{i})));
                }
            }
        } else {
            for (User i : in) {
                if (sortableMap.containsKey(i.getResidence())) {
                    sortableMap.get(i.getResidence()).add(i);
                } else {
                    sortableMap.put(i.getResidence(), new ArrayList<>(List.of(new User[]{i})));
                }
            }
        }
        List<User> sortableItemMap = new ArrayList<>();
        for (Object l : sortMap(sortableMap, ascending)) {
            for (Object o : (List<Object>) l) {
                sortableItemMap.add((User) o);
            }
        }
        return sortableItemMap;
    }

    private static List<Object> sortMap(Map<Comparable, List<Object>> in, boolean ascending) {
        List<Object> out = new ArrayList<>();
        if (ascending) {
            while (!in.isEmpty()) {
                Comparable toAdd = null;
                for (Comparable o : in.keySet()) {
                    if (toAdd == null) {
                        toAdd = o;
                    }
                    if (o.compareTo(toAdd) < 0) {
                        toAdd = o;
                    }
                }
                out.add(in.get(toAdd));
                in.remove(toAdd);
            }
        } else {
            while (!in.isEmpty()) {
                Comparable toAdd = null;
                for (Comparable o : in.keySet()) {
                    if (toAdd == null) {
                        toAdd = o;
                    }
                    if (o.compareTo(toAdd) > 0) {
                        toAdd = o;
                    }
                }
                out.add(in.get(toAdd));
                in.remove(toAdd);
            }
        }
        return out;
    }

    public static String getWebshops() {
        return String.valueOf(Singleton.getSingleton().getWebshopRepo().getWebshopIds());
    }

    public static String getUsersByWebshop(boolean valid, int webshopId, SortingEnum sort, boolean ascending) {
        StringBuilder out = new StringBuilder();
        Singleton s = Singleton.getSingleton();
        boolean inputValid = valid;
        Webshop webshop = tryFindWebShop(out, webshopId);
        if (webshop == null) {
            inputValid = false;
        }
        if (inputValid) {
            Set<User> users = new HashSet<>();
            try {
                Set<Integer> intSet = Singleton.getSingleton().getWebshopRepo().getWebshop(webshopId).getUsers();
                for (int i : intSet) {
                    try {
                        users.add(Singleton.getSingleton().getUserRepo().getUser(i));
                    } catch (NoSuchUserException e) {
                        out.append("no User with id: ").append(i).append(", which was find in: ").append(webshopId).append(".\n");
                        inputValid = false;
                    }
                }
            } catch (NoSuchWebshopException e) {
                out.append("no Webshop with id: ").append(webshopId).append(".\n");
                inputValid = false;
            }
            if (inputValid) {
                List<User> sortedUsers = sortUserSet(users, sort, ascending);
                if (sortedUsers.isEmpty()) {
                    out.append("no users found.\n");
                } else {
                    out.append("users from webshop: ").append(webshopId).append(".\n");
                    for (User user : sortedUsers) {
                        out.append(user);
                    }
                }
            }
        }
        return out.toString();
    }

    public static String getUsersByWebshop(boolean valid, int webshopId, SortingEnum sort) {
        return getUsersByWebshop(valid, webshopId, sort, true);
    }

    public static String getUsersByWebshop(boolean valid, int webshopId, boolean ascending) {
        return getUsersByWebshop(valid, webshopId, SortingEnum.ID, ascending);
    }

    public static String getUsersByWebshop(boolean valid, int webshopId) {
        return getUsersByWebshop(valid, webshopId, SortingEnum.ID, true);
    }

    public static String getOrders(boolean valid, int webshopId, int userId, SortingEnum sort, boolean ascending) {
        StringBuilder out = new StringBuilder();
        Singleton s = Singleton.getSingleton();
        boolean inputValid = valid;
        Webshop webshop = tryFindWebShop(out, webshopId);
        User user = tryFindUser(out, userId);
        if (webshop == null || user == null) {
            inputValid = false;
        } else if (!webshop.getUsers().contains(userId)) {
            out.append("user not part of webshop.\n");
            inputValid = false;
        }

        if (inputValid) {
            Set<Order> orders = new HashSet<>();
            try {
                Set<Integer> intSet = Singleton.getSingleton().getUserRepo().getOrders(userId);
                for (int i : intSet) {
                    try {
                        orders.add(Singleton.getSingleton().getOrderRepo().getOrder(i));
                    } catch (NoSuchUserException e) {
                        out.append("no Order with id: ").append(i).append(", which was find in: ").append(userId).append(".\n");
                        inputValid = false;
                    }
                }
            } catch (NoSuchWebshopException e) {
                out.append("no User with id: ").append(userId).append(".\n");
                inputValid = false;
            }
            if (inputValid) {
                List<Order> sortedOrders = sortOrderSet(orders, sort, ascending);
                if (sortedOrders.isEmpty()) {
                    out.append("no orders found.\n");
                } else {
                    out.append("orders from user: ").append(userId).append(".\n");
                    for (Order order : sortedOrders) {
                        out.append(order);
                    }
                }
            }
        }
        return out.toString();
    }

    public static String getOrders(boolean valid, int webshopId, int userId, SortingEnum sort) {
        return getOrders(valid, webshopId, userId, sort, true);
    }

    public static String getOrders(boolean valid, int webshopId, int userId, boolean ascending) {
        return getOrders(valid, webshopId, userId, SortingEnum.ID, ascending);
    }

    public static String getOrders(boolean valid, int webshopId, int userId) {
        return getOrders(valid, webshopId, userId, SortingEnum.ID, true);
    }

    public static String getOrder(boolean valid, int webshopId, int userId, int orderId, SortingEnum sort, boolean ascending) {
        StringBuilder out = new StringBuilder();
        Singleton s = Singleton.getSingleton();
        boolean inputValid = valid;
        Webshop webshop = tryFindWebShop(out, webshopId);
        User user = tryFindUser(out, userId);
        Order order = tryFindOrder(out, orderId);
        if (webshop == null || user == null || order == null) {
            inputValid = false;
        } else if (!webshop.getUsers().contains(userId)) {
            out.append("user not part of webshop.\n");
            inputValid = false;
        } else if (!user.getOrders().contains(orderId)) {
            out.append("order not part of user.\n");
            inputValid = false;
        }
        if (inputValid) {
            out.append("order id : ").append(order.getId()).append(" date : ").append(order.getDate().getTime()).append(" from user : ").append(order.getUserId()).append(" total price : ").append(order.getTotalPrice()).append(".\n");
            Set<Item> items = new HashSet<>();
            try {
                Set<Integer> intSet = Singleton.getSingleton().getOrderRepo().getItemsAndCounts(orderId).keySet();
                for (int i : intSet) {
                    try {
                        items.add(Singleton.getSingleton().getItemRepo().getItem(i));
                    } catch (NoSuchUserException e) {
                        out.append("no Item with id: ").append(i).append(", which was find in: ").append(orderId).append(".\n");
                        inputValid = false;
                    }
                }
            } catch (NoSuchWebshopException e) {
                out.append("no Order with id: ").append(orderId).append(".\n");
                inputValid = false;
            }
            if (inputValid) {
                List<Item> sortedItems = sortCatalogSet(items, sort, ascending);
                if (sortedItems.isEmpty()) {
                    out.append("no items found.\n");
                } else {
                    for (Item item : sortedItems) {
                        out.append(item);
                        out.append("count : ").append(order.getItemsCount().get(item.getId())).append(" total price : ")
                                .append(new Money(order.getItemsCount().get(item.getId())).mult(item.getPrice())).append(".\n");
                    }
                }
            }
        }
        return out.toString();
    }

    public static String getOrder(boolean valid, int webshopId, int userId, int orderId, SortingEnum sort) {
        return getOrder(valid, webshopId, userId, orderId, sort, true);
    }

    public static String getOrder(boolean valid, int webshopId, int userId, int orderId, boolean ascending) {
        return getOrder(valid, webshopId, userId, orderId, SortingEnum.ID, ascending);
    }

    public static String getOrder(boolean valid, int webshopId, int userId, int orderId) {
        return getOrder(valid, webshopId, userId, orderId, SortingEnum.ID, true);
    }

    private static Webshop tryFindWebShop(StringBuilder outputString, int id) {
        Webshop webshop = null;
        try {
            webshop = Singleton.getSingleton().getWebshopRepo().getWebshop(id);
        } catch (NoSuchWebshopException e) {
            outputString.append("no Webshop with id: ").append(id).append(".\n");
        }
        return webshop;
    }

    private static User tryFindUser(StringBuilder outputString, int id) {
        User user = null;
        try {
            user = Singleton.getSingleton().getUserRepo().getUser(id);
        } catch (NoSuchUserException e) {
            outputString.append("no User with id: ").append(id).append(".\n");
        }
        return user;
    }

    private static Item tryFindItem(StringBuilder outputString, int id) {
        Item item = null;
        try {
            item = Singleton.getSingleton().getItemRepo().getItem(id);
        } catch (NoSuchItemException e) {
            outputString.append("no Item with id: ").append(id).append(".\n");
        }
        return item;
    }

    private static Order tryFindOrder(StringBuilder outputString, int id) {
        Order order = null;
        try {
            order = Singleton.getSingleton().getOrderRepo().getOrder(id);
        } catch (NoSuchOrderException e) {
            outputString.append("no order with id: ").append(id).append(".\n");
        }
        return order;
    }
}
