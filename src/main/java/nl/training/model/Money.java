package nl.training.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money implements Comparable<Money> {

    private final BigDecimal amount;

    public Money(String in) {
        amount = new BigDecimal(in).setScale(2, RoundingMode.HALF_UP);
    }

    public Money(double in) {
        amount = new BigDecimal(in).setScale(2, RoundingMode.HALF_UP);
    }

    public Money(int in) {
        amount = new BigDecimal(in).setScale(2, RoundingMode.HALF_UP);
    }

    public Money(BigDecimal in) {
        amount = in.setScale(2, RoundingMode.HALF_UP);
        ;
    }

    @Override
    public String toString() {
        return amount.toString();
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Money) {
            return this.amount.equals(((Money) obj).amount);
        } else {
            return false;
        }
    }

    public Money add(Money toAdd) {
        return new Money(amount.add(toAdd.amount));
    }

    public Money mult(Money factor) {
        return new Money(amount.multiply(factor.amount));
    }

    @Override
    public int compareTo(Money o) {
        return amount.compareTo(o.amount);
    }
}
