package senberg.hackathon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class CompanyDividend extends Event {
    // If the factor is 10, 10 original stocks are necessary to get one new stock.
    BigDecimal factor;
    BigDecimal value;
    String name;

    public CompanyDividend(String currency, Date date, BigDecimal factor, BigDecimal value, String name) {
        super(currency, date);
        this.factor = factor;
        this.value = value;
        this.name = name;
    }

    // Example: factor 10 dividend of one new stock worth 87kr.
    // eg. 10 original stocks needed to get one new, which is valued at 87kr.
    @Override
    public BigDecimal calculateAdjustedFactor(BigDecimal lastPrice, BigDecimal adjustedFactor) {
        BigDecimal newStockAmount = adjustedFactor.divide(factor, 10, RoundingMode.HALF_UP);
        BigDecimal newStockValuePercentage = newStockAmount.multiply(value).divide(lastPrice, 10, RoundingMode.HALF_UP);
        BigDecimal stockFactor = BigDecimal.ONE.add(newStockValuePercentage);
        return adjustedFactor.multiply(stockFactor);
    }

    @Override
    public String getDescription() {
        return "A new stock was given out. " + name + ", valued at " + value.divide(factor, 2, RoundingMode.HALF_UP) + " " + currency + ".";
    }
}
