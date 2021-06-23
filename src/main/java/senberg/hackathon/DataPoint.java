package senberg.hackathon;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

@JsonSerialize(using = DataPointSerializer.class)
public class DataPoint {
    String currency;
    Date date;
    BigDecimal closingPrice;
    BigDecimal totalReturn;

    public DataPoint(String currency, Date date, BigDecimal closingPrice, BigDecimal totalReturn) {
        this.currency = currency;
        this.date = date;
        this.closingPrice = closingPrice;
        this.totalReturn = totalReturn;
    }
}
