package nl.training.userinterface;

import nl.training.programFunctions.Controller;
import nl.training.programFunctions.SortingEnum;

import java.util.*;

public class UserInterface {
    private Set<String> validActionKeywords;

    private boolean running = true;
    private static final String HELP_KEYWORD1 = "?";
    private static final String HELP_KEYWORD2 = "help";
    private static final String EXIT_KEYWORD1 = "exit";
    private static final String ORDER_ITEM_KEYWORD1 = "order";
    private static final String PRINT_CATALOG_PER_YEAR_KEYWORD1 = "cat";
    private static final String PRINT_CATALOG_PER_YEAR_KEYWORD2 = "catalog";
    private static final String PRINT_WEBSHOPS_DATA_KEYWORD1 = "p_webs";
    private static final String PRINT_WEBSHOPS_DATA_KEYWORD2 = "p_webshops";
    private static final String PRINT_USERS_KEYWORD1 = "p_users";
    private static final String PRINT_ORDERS_KEYWORD1 = "p_orders";
    private static final String PRINT_ORDER_KEYWORD1 = "p_order";
    private final static String helpString = """
            usage = "keyword args (req args in order followed by opt args markt by flag seperated by space.
            ie: order 1 1 1 1 -o 1), flags are case sensitive."
            
            Exit = exit
                        
            ?/help = this list
                        
            order = order item
                args:
                    webshop id  - int id of a webshop                                       - req
                    user id     - int id of the user                                        - req
                    item id     - int id of the item                                        - req
                    item count  - int amount of items to order                              - req
                    -o order id - int id of the order to add to, if non make new            - opt
                    
            cat/catalog = print catalog
                args:
                    webshop id  - int id of a webshop                                       - req
                    -y year     - int limit output to this year                             - opt
                    -s sort     - string sort by "date", "price", "id" if non sort by id    - opt
                    -a ascending- bool ascending or descending sort if non, ascending       - opt
                        
            p_webs/p_webshops = print webshop ids
                        
            p_users = print catalog
                args:
                    webshop id  - int id of a webshop                                       - req
                    -s sort     - string sort by "name", "res", "id" if non sort by id      - opt
                    -a ascending- bool ascending or descending sort if non, ascending       - opt
                        
            p_orders = print orders
                args:
                    webshop id  - int id of a webshop                                       - req
                    user id     - int id of the user                                        - req
                    -s sort     - string sort by "date", "price", "id" if non sort by id    - opt
                    -a ascending- bool ascending or descending sort if non, ascending       - opt
                    
            p_order = print order
                args:
                    webshop id  - int id of a webshop                                       - req
                    user id     - int id of the user                                        - req
                    user id     - int id of the user                                        - req
                    -s sort     - string sort by "date", "price", "id" if non sort by id    - opt
                    -a ascending- bool ascending or descending sort if non, ascending       - opt
                        
                    """;
    private Map<String, String> responseKeywords;

    private String decipherLine(String input) {
        String result = "";
        String args = "";
        input = input.trim();
        /*int firstSpace = input.indexOf(' ');
        if (firstSpace == -1){
            firstSpace = input.indexOf('\t');
        }if (firstSpace == -1){
            firstSpace = input.indexOf('\f');
        }if (firstSpace == -1){
            firstSpace = input.indexOf('\r');
        }*/
        String keyWord = input.split("\\s")[0].trim();
        if (input.length()>keyWord.length()) {
            args = input.substring(keyWord.length()).trim();
        }
        if (responseKeywords.containsKey(keyWord.toLowerCase())) {
            result = responseKeywords.get(keyWord.toLowerCase());
        } else if (validActionKeywords.contains(keyWord.toLowerCase())) {

            switch (keyWord.toLowerCase()) {
                case EXIT_KEYWORD1:
                    running = false;
                    break;
                case ORDER_ITEM_KEYWORD1: {
                    List<Object> reqs = new ArrayList<>();
                    reqs.add(0);
                    reqs.add(0);
                    reqs.add(0);
                    reqs.add(0);
                    List<Object> opts = new ArrayList<>();
                    opts.add(0);
                    List<String> optFlags = new ArrayList<>();
                    optFlags.add("-o");
                    InputArgsBasicChecker checker = new InputArgsBasicChecker(reqs, opts, optFlags);
                    System.out.print(checker.check(args));
                    if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true})) {
                        System.out.print(Controller.order(checker.getValid(), (Integer) reqs.get(0), (Integer) reqs.get(1),
                                (Integer) reqs.get(2), (Integer) reqs.get(3), (Integer) opts.get(0)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false})) {
                        System.out.print(Controller.order(checker.getValid(), (Integer) reqs.get(0), (Integer) reqs.get(1),
                                (Integer) reqs.get(2), (Integer) reqs.get(3)));
                    } else {
                        System.out.println("optional checking went wrong, missed all conds");
                    }
                }
                break;
                case PRINT_CATALOG_PER_YEAR_KEYWORD1:
                case PRINT_CATALOG_PER_YEAR_KEYWORD2: {
                    List<Object> reqs = new ArrayList<>();
                    reqs.add(0);
                    List<Object> opts = new ArrayList<>();
                    opts.add(0);
                    opts.add("");
                    opts.add(false);
                    List<String> optFlags = new ArrayList<>();
                    optFlags.add("-y");
                    optFlags.add("-s");
                    optFlags.add("-a");
                    InputArgsBasicChecker checker = new InputArgsBasicChecker(reqs, opts, optFlags);
                    System.out.print(checker.check(args));
                    SortingEnum searchArg = null;
                    if (checker.getUsedOpts()[1]) {
                        switch (((String) opts.get(1)).toLowerCase()) {
                            case "date":
                                searchArg = SortingEnum.DATE;
                                break;
                            case "price":
                                searchArg = SortingEnum.PRICE;
                                break;
                            case "id":
                                searchArg = SortingEnum.ID;
                                break;
                            default:
                                searchArg = SortingEnum.ID;
                                checker.setValid(false);
                                System.out.println("invalid search arg: " + (String) opts.get(1));
                        }
                    }
                    if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true, true, false})) {
                        System.out.print(Controller.getCatalogString(checker.getValid(), (Integer) reqs.get(0),
                                (Integer) opts.get(0), searchArg));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true, false, false})) {
                        System.out.print(Controller.getCatalogString(checker.getValid(), (Integer) reqs.get(0),
                                (Integer) opts.get(0)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false, true, false})) {
                        System.out.print(Controller.getCatalogString(checker.getValid(), (Integer) reqs.get(0), searchArg));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true, true, true})) {
                        System.out.print(Controller.getCatalogString(checker.getValid(), (Integer) reqs.get(0),
                                (Integer) opts.get(0), searchArg, (boolean) opts.get(2)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true, false, true})) {
                        System.out.print(Controller.getCatalogString(checker.getValid(), (Integer) reqs.get(0),
                                (Integer) opts.get(0), (boolean) opts.get(2)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false, true, true})) {
                        System.out.print(Controller.getCatalogString(checker.getValid(), (Integer) reqs.get(0), searchArg,
                                (boolean) opts.get(2)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false, false, true})) {
                        System.out.print(Controller.getCatalogString(checker.getValid(), (Integer) reqs.get(0),
                                (boolean) opts.get(2)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false, false, false})) {
                        System.out.print(Controller.getCatalogString(checker.getValid(), (Integer) reqs.get(0)));
                    } else {
                        System.out.println("optional checking went wrong, missed al conds");
                    }
                }
                break;
                case PRINT_WEBSHOPS_DATA_KEYWORD1:
                case PRINT_WEBSHOPS_DATA_KEYWORD2: {
                    System.out.println(Controller.getWebshops());
                }
                break;
                case PRINT_USERS_KEYWORD1: {
                    List<Object> reqs = new ArrayList<>();
                    reqs.add(0);
                    List<Object> opts = new ArrayList<>();
                    opts.add("");
                    opts.add(false);
                    List<String> optFlags = new ArrayList<>();
                    optFlags.add("-s");
                    optFlags.add("-a");
                    InputArgsBasicChecker checker = new InputArgsBasicChecker(reqs, opts, optFlags);
                    System.out.print(checker.check(args));
                    SortingEnum searchArg = null;
                    if (checker.getUsedOpts()[0]) {
                        switch (((String) opts.get(0)).toLowerCase()) {
                            case "name":
                                searchArg = SortingEnum.NAME;
                                break;
                            case "res":
                                searchArg = SortingEnum.RESIDENCE;
                                break;
                            case "id":
                                searchArg = SortingEnum.ID;
                                break;
                            default:
                                searchArg = SortingEnum.ID;
                                checker.setValid(false);
                                System.out.println("invalid search arg: " + (String) opts.get(0));
                        }
                    }
                    if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true, true})) {
                        System.out.print(Controller.getUsersByWebshop(checker.getValid(), (Integer) reqs.get(0), searchArg, (Boolean) opts.get(1)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true, false})) {
                        System.out.print(Controller.getUsersByWebshop(checker.getValid(), (Integer) reqs.get(0), searchArg));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false, true})) {
                        System.out.print(Controller.getUsersByWebshop(checker.getValid(), (Integer) reqs.get(0), (Boolean) opts.get(1)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false, false})) {
                        System.out.print(Controller.getUsersByWebshop(checker.getValid(), (Integer) reqs.get(0)));
                    } else {
                        System.out.println("optional checking went wrong, missed al conds");
                    }
                }
                break;
                case PRINT_ORDERS_KEYWORD1: {
                    List<Object> reqs = new ArrayList<>();
                    reqs.add(0);
                    reqs.add(0);
                    List<Object> opts = new ArrayList<>();
                    opts.add("");
                    opts.add(false);
                    List<String> optFlags = new ArrayList<>();
                    optFlags.add("-s");
                    optFlags.add("-a");
                    InputArgsBasicChecker checker = new InputArgsBasicChecker(reqs, opts, optFlags);
                    System.out.print(checker.check(args));
                    SortingEnum searchArg = null;
                    if (checker.getUsedOpts()[0]) {
                        switch (((String) opts.get(0)).toLowerCase()) {
                            case "date":
                                searchArg = SortingEnum.DATE;
                                break;
                            case "price":
                                searchArg = SortingEnum.TOTAL_PRICE;
                                break;
                            case "id":
                                searchArg = SortingEnum.ID;
                                break;
                            default:
                                searchArg = SortingEnum.ID;
                                checker.setValid(false);
                                System.out.println("invalid search arg: " + (String) opts.get(0));
                        }
                    }
                    if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true, true})) {
                        System.out.print(Controller.getOrders(checker.getValid(), (Integer) reqs.get(0), (Integer) reqs.get(1),
                                searchArg, (Boolean) opts.get(1)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true, false})) {
                        System.out.print(Controller.getOrders(checker.getValid(), (Integer) reqs.get(0), (Integer) reqs.get(1),
                                searchArg));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false, true})) {
                        System.out.print(Controller.getOrders(checker.getValid(), (Integer) reqs.get(0), (Integer) reqs.get(1),
                                (Boolean) opts.get(1)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false, false})) {
                        System.out.print(Controller.getOrders(checker.getValid(), (Integer) reqs.get(0), (Integer) reqs.get(1)));
                    } else {
                        System.out.println("optional checking went wrong, missed al conds");
                    }
                }
                break;
                case PRINT_ORDER_KEYWORD1: {
                    List<Object> reqs = new ArrayList<>();
                    reqs.add(0);
                    reqs.add(0);
                    reqs.add(0);
                    List<Object> opts = new ArrayList<>();
                    opts.add("");
                    opts.add(false);
                    List<String> optFlags = new ArrayList<>();
                    optFlags.add("-s");
                    optFlags.add("-a");
                    InputArgsBasicChecker checker = new InputArgsBasicChecker(reqs, opts, optFlags);
                    System.out.print(checker.check(args));
                    SortingEnum searchArg = null;
                    if (checker.getUsedOpts()[0]) {
                        switch (((String) opts.get(0)).toLowerCase()) {
                            case "date":
                                searchArg = SortingEnum.DATE;
                                break;
                            case "price":
                                searchArg = SortingEnum.PRICE;
                                break;
                            case "id":
                                searchArg = SortingEnum.ID;
                                break;
                            default:
                                searchArg = SortingEnum.ID;
                                checker.setValid(false);
                                System.out.println("invalid search arg: " + (String) opts.get(0));
                        }
                    }
                    if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true, true})) {
                        System.out.print(Controller.getOrder(checker.getValid(), (Integer) reqs.get(0), (Integer) reqs.get(1),
                                (Integer) reqs.get(2), searchArg, (Boolean) opts.get(1)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{true, false})) {
                        System.out.print(Controller.getOrder(checker.getValid(), (Integer) reqs.get(0), (Integer) reqs.get(1),
                                (Integer) reqs.get(2), searchArg));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false, true})) {
                        System.out.print(Controller.getOrder(checker.getValid(), (Integer) reqs.get(0), (Integer) reqs.get(1),
                                (Integer) reqs.get(2), (Boolean) opts.get(1)));
                    } else if (Arrays.equals(checker.getUsedOpts(), new boolean[]{false, false})) {
                        System.out.print(Controller.getOrder(checker.getValid(), (Integer) reqs.get(0), (Integer) reqs.get(1),
                                (Integer) reqs.get(2)));
                    } else {
                        System.out.println("optional checking went wrong, missed al conds");
                    }
                }
                break;
                default:
                    throw new RuntimeException("keyword found in list but no case was made");
            }
        } else {
            if (keyWord.isBlank()) {
                result = "empty input.\n";
            } else {
                result = "\"" + keyWord + "\" is an invalid Keyword, use \"help\" for help.\n";
                Set<String> validKeywords = new HashSet<>(validActionKeywords);
                validKeywords.addAll(responseKeywords.keySet());
                HashMap<Double, List<String>> suggestions = new HashMap<>();
                for (String s : validKeywords) {
                    int longest = Math.max(s.length(), keyWord.length());
                    double sim = (double) editDistance(s, keyWord) / longest;
                    if (sim < 0.3) {
                        if (!suggestions.containsKey(sim)) {
                            suggestions.put(sim, new ArrayList<>());
                        }
                        suggestions.get(sim).add(s);
                    }
                }
                if (!suggestions.isEmpty()) {
                    List<Map.Entry<Double, List<String>>> suggestionslist = new ArrayList<>(suggestions.entrySet());
                    suggestionslist.sort(Map.Entry.comparingByKey());
                    result += "did you mean?: \n";
                    for (Map.Entry<Double, List<String>> entry : suggestionslist) {
                        for (String sugg : entry.getValue()) {
                            result += sugg + "\n";
                        }
                    }
                }
            }
        }
        return result;
    }

    public void buildKeywordCollections() {
        responseKeywords = new HashMap<>();
        responseKeywords.put(HELP_KEYWORD1, helpString);
        responseKeywords.put(HELP_KEYWORD2, helpString);
        validActionKeywords = new HashSet<>();
        validActionKeywords.add(EXIT_KEYWORD1);
        validActionKeywords.add(ORDER_ITEM_KEYWORD1);
        validActionKeywords.add(PRINT_CATALOG_PER_YEAR_KEYWORD1);
        validActionKeywords.add(PRINT_CATALOG_PER_YEAR_KEYWORD2);
        validActionKeywords.add(PRINT_WEBSHOPS_DATA_KEYWORD1);
        validActionKeywords.add(PRINT_WEBSHOPS_DATA_KEYWORD2);
        validActionKeywords.add(PRINT_USERS_KEYWORD1);
        validActionKeywords.add(PRINT_ORDERS_KEYWORD1);
        validActionKeywords.add(PRINT_ORDER_KEYWORD1);
    }

    public void start() {
        buildKeywordCollections();
        Scanner in = new Scanner(System.in);
        while (running) {
            System.out.print(decipherLine(in.nextLine()));
        }
    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
