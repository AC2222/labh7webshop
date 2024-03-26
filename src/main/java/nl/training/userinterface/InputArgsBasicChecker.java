package nl.training.userinterface;

import java.util.Arrays;
import java.util.List;

public class InputArgsBasicChecker {
    private Boolean valid = true;
    private List<Object> reqs;
    private List<Object> opts;
    private boolean[] usedOpts;
    private List<String> optFlags;

    public InputArgsBasicChecker(List<Object> reqs, List<Object> opts, List<String> optFlags) {
        this.reqs = reqs;
        this.opts = opts;
        this.optFlags = optFlags;
        this.usedOpts = new boolean[opts.size()];
        if (opts.size()!=optFlags.size()){
            throw new RuntimeException("optFlags and opts diff size.");
        }
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public List<Object> getReqs() {
        return reqs;
    }

    public List<Object> getOpts() {
        return opts;
    }

    public List<String> getOptFlags() {
        return optFlags;
    }

    public boolean[] getUsedOpts() {
        return usedOpts;
    }

    /**
     * sets the fields of this instance to the seperated, type checked and flag checked args given
     *
     * @param args: string arguments
     * @return: string containing message(s)
     */
    public String check(String args) {
        StringBuilder result = new StringBuilder();
        args = args.trim();
        String[] argsArray = removeBlankStrings(args.split("\\s"));
        for (int i = 0; i < argsArray.length; i++) {
            argsArray[i] = argsArray[i].trim();
        }
        if (argsArray.length < reqs.size()) {
            result.append("invalid nr of args, expected minimum of: ").append(reqs.size()).append(" got: ").append(argsArray.length).append(".\n");
            valid = false;
        }
        for (int i = 0; i < argsArray.length && i < reqs.size(); i++) {
            if (reqs.get(i) instanceof Integer) {
                try {
                    reqs.set(i, Integer.parseInt(argsArray[i]));
                } catch (NumberFormatException e) {
                    result.append("invalid arg at pos ").append(i).append(" expected int, got: ").append(argsArray[i]).append("\n");
                    reqs.set(i, 0);
                    valid = false;
                }
            } else if (reqs.get(i) instanceof Boolean) {
                if (argsArray[i].equalsIgnoreCase("true") || argsArray[i].equalsIgnoreCase("false")) {
                    reqs.set(i, Boolean.parseBoolean(argsArray[i]));
                } else {
                    result.append("invalid arg at pos ").append(i).append(" expected bool, got: ").append(argsArray[i]).append("\n");
                    opts.set(i, false);
                    valid = false;
                }
            } else if (reqs.get(i) instanceof String) {
                reqs.set(i, argsArray[i]);
            } else {
                throw new RuntimeException("malformed argsInputHandler req list");
            }
        }
        if (argsArray.length > reqs.size()) {
            String[] optArgsArray = Arrays.stream(argsArray).toList().subList(reqs.size(), argsArray.length).toArray(new String[argsArray.length - reqs.size()]);
            if (optArgsArray.length % 2 != 0) {
                result.append("uneven optional args, match every flag with a value?\n");
                valid = false;
            } else {
                for (int i = 0; i < optArgsArray.length; i += 2) {
                    int foundIndex = optFlags.indexOf(optArgsArray[i]);
                    if (foundIndex < 0) {
                        result.append("invalid flag at pos ").append(i + reqs.size()).append("\n");
                        valid = false;
                    } else {
                        usedOpts[foundIndex] = true;
                        if (opts.get(foundIndex) instanceof Integer) {
                            try {
                                opts.set(foundIndex, Integer.parseInt(optArgsArray[i + 1]));
                            } catch (NumberFormatException e) {
                                result.append("invalid arg at pos ").append(i + 1 + reqs.size()).append(" expected int, got: ").append(optArgsArray[i + 1]).append("\n");
                                reqs.set(foundIndex, 0);
                                valid = false;
                            }
                        } else if (opts.get(foundIndex) instanceof Boolean) {
                            if (optArgsArray[i + 1].equalsIgnoreCase("true") || optArgsArray[i + 1].equalsIgnoreCase("false")) {
                                opts.set(foundIndex, Boolean.parseBoolean(optArgsArray[i + 1]));
                            } else {
                                result.append("invalid arg at pos ").append(i).append(" expected bool, got: ").append(optArgsArray[i + 1]).append("\n");
                                opts.set(foundIndex, false);
                                valid = false;
                            }
                        } else if (opts.get(foundIndex) instanceof String) {
                            opts.set(foundIndex, optArgsArray[i + 1]);

                        } else {
                            throw new RuntimeException("malformed argsInputHandler opts list");
                        }
                    }
                }
            }
        }
        return result.toString();
    }

    private String[] removeBlankStrings(String[] in) {
        int i = 0;
        for (String s : in) {
            if (!s.isBlank()) {
                i++;
            }
        }
        String[] out = new String[i];
        i = 0;
        for (String s : in) {
            if (!s.isBlank()) {
                out[i++] = s;
            }
        }
        return out;
    }
}
