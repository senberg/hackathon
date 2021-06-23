package senberg.hackathon;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class YahooStockData extends StockData {
    public YahooStockData(String priceFilename, String eventsFilename, String currency) {
        super(priceFilename, eventsFilename, currency);
    }

    @Override
    public int getClosingPriceCell() {
        return 4;
    }

    @Override
    public int getHeaderRows() {
        return 1;
    }

    public String getSeparator(){
        return ",";
    }

    public DecimalFormat getDecimalFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.0#";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        return decimalFormat;
    }

    @Override
    public boolean reversedTimeline() {
        return true;
    }

    @Override
    public boolean isAdjusted() {
        return true;
    }

    @Override
    public int getDividendAmountCell() {
        return 6;
    }
}
