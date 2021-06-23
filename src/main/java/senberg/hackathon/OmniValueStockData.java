package senberg.hackathon;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class OmniValueStockData extends StockData {
    public OmniValueStockData(String priceFilename, String eventsFilename, String currency) {
        super(priceFilename, eventsFilename, currency);
    }

    @Override
    public int getClosingPriceCell() {
        return 6;
    }

    @Override
    public int getHeaderRows() {
        return 2;
    }

    public String getSeparator() {
        return ";";
    }

    public DecimalFormat getDecimalFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        String pattern = "#,##0.0#";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        return decimalFormat;
    }

    @Override
    public boolean reversedTimeline() {
        return false;
    }

    @Override
    public boolean isAdjusted() {
        return false;
    }

    @Override
    public int getDividendAmountCell() {
        return 6;
    }
}
