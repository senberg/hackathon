package senberg.hackathon;

import java.math.BigDecimal;
import java.util.Date;

public class FundedEmission extends Event{
    BigDecimal factor;

    public FundedEmission(String currency, Date date, BigDecimal factor) {
        super(currency, date);
        this.factor = factor;
    }


    // funded emission factor 1, eg for every stock you get a new stock.
    // old adjusted factor 100, new price roughly half, eg. new adjusted factor should be 200
    @Override
    public BigDecimal calculateAdjustedFactor(BigDecimal lastPrice, BigDecimal adjustedFactor) {
        return adjustedFactor.multiply(BigDecimal.ONE.add(factor));
    }

    @Override
    public String getDescription() {
        return "Company cash was converted into more stocks, giving out " + factor + " extra stock for each old stock.";
    }
}
