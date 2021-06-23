package senberg.hackathon;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@JsonSerialize(using = StockDataSerializer.class)
public abstract class StockData {
    public static final int DATE_CELL = 0;

    public abstract int getClosingPriceCell();
    public abstract int getHeaderRows();
    public abstract String getSeparator();
    public abstract DecimalFormat getDecimalFormat();
    public abstract boolean reversedTimeline();
    public abstract boolean isAdjusted();
    public abstract int getDividendAmountCell();

    public Map<Date, Event> events = new HashMap<>();
    public List<DataPoint> data;
    public Map<String, String> stats = new HashMap<>();
    public String currency;

    public StockData(String priceFilename, String eventsFilename, String currency) {
        this.currency = currency;
        DecimalFormat decimalFormat = getDecimalFormat();

        try {
            List<String> eventLines = getLines(eventsFilename);

            for (String line : eventLines) {
                String[] cells = line.split(getSeparator());
                Date date = java.sql.Date.valueOf(cells[3]);

                switch (cells[2]) {
                    case "510":
                        events.put(date, new Dividend(currency, date, (BigDecimal) decimalFormat.parse(cells[getDividendAmountCell()])));
                        break;
                    case "520":
                        events.put(date, new CompanyDividend(currency, date, (BigDecimal) decimalFormat.parse(cells[5]), (BigDecimal) decimalFormat.parse(cells[getClosingPriceCell()]), cells[8]));
                        break;
                    case "710":
                        events.put(date, new Split(currency, date, (BigDecimal) decimalFormat.parse(cells[5])));
                        break;
                    case "720":
                        events.put(date, new ReverseSplit(currency, date, (BigDecimal) decimalFormat.parse(cells[5])));
                        break;
                    case "810":
                        events.put(date, new FundedEmission(currency, date, (BigDecimal) decimalFormat.parse(cells[5])));
                        break;
                    default:
                        throw new RuntimeException("Unhandled event. " + cells[2]);
                }
            }

             Set<Date> priceDates = new HashSet<>();
            List<String> priceLines = getLines(priceFilename);
            data = new ArrayList<>(priceLines.size());
            BigDecimal adjustedFactor = BigDecimal.ONE;

            for (int i = priceLines.size() - 1; i >= 0; i--) {
                String line = priceLines.get(i);
                String[] cells = line.split(getSeparator());
                Date date = java.sql.Date.valueOf(cells[DATE_CELL]);
                BigDecimal closingPrice = (BigDecimal) decimalFormat.parse(cells[getClosingPriceCell()]);

                if (!priceDates.add(date)) {
                    throw new RuntimeException("Two different prices for date " + date);
                }

                Event event = events.get(date);

                if (event != null) {
                    adjustedFactor = event.calculateAdjustedFactor(closingPrice, adjustedFactor);
                }

                if(isAdjusted()){
                    data.add(new DataPoint(currency, date, closingPrice.divide(adjustedFactor, 10, RoundingMode.HALF_UP), closingPrice));
                } else {
                    data.add(new DataPoint(currency, date, closingPrice, closingPrice.multiply(adjustedFactor)));
                }
            }

            if(isAdjusted()){
                BigDecimal finalAdjustedFactor = adjustedFactor;
                data.forEach(dataPoint -> dataPoint.closingPrice = dataPoint.closingPrice.multiply(finalAdjustedFactor));
                data.forEach(dataPoint -> dataPoint.totalReturn = dataPoint.totalReturn.multiply(finalAdjustedFactor));
            }

            BigDecimal initialClosingPrice = data.get(0).closingPrice;
            BigDecimal lastClosingPrice = data.get(data.size() - 1).closingPrice;
            BigDecimal lastTotalReturn = data.get(data.size() - 1).totalReturn;
            BigDecimal thousandFactor = new BigDecimal(1000).divide(initialClosingPrice, 10, RoundingMode.HALF_UP);
            BigDecimal years = getYearsDifference(data.get(0).date, data.get(data.size() - 1).date);
            BigDecimal pricePercentageReturn = lastClosingPrice.subtract(initialClosingPrice).divide(initialClosingPrice, 10, RoundingMode.HALF_UP);
            BigDecimal totalPercentageReturn = lastTotalReturn.subtract(initialClosingPrice).divide(initialClosingPrice, 10, RoundingMode.HALF_UP);
            BigDecimal priceAnnualizedReturn = getAnnualizedReturn(pricePercentageReturn, years);
            BigDecimal totalAnnualizedReturn = getAnnualizedReturn(totalPercentageReturn, years);
            stats.put("initialClosingPrice", initialClosingPrice.setScale(2, RoundingMode.HALF_UP).toString());
            stats.put("lastClosingPrice", lastClosingPrice.setScale(2, RoundingMode.HALF_UP).toString());
            stats.put("lastTotalReturn", lastTotalReturn.setScale(2, RoundingMode.HALF_UP).toString());
            stats.put("thousandFactor", thousandFactor.setScale(2, RoundingMode.HALF_UP).toString());
            stats.put("pricePercentageReturn", pricePercentageReturn.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString());
            stats.put("totalPercentageReturn", totalPercentageReturn.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString());
            stats.put("years", years.setScale(2, RoundingMode.HALF_UP).toString());
            stats.put("priceAnnualizedReturn", priceAnnualizedReturn.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString());
            stats.put("totalAnnualizedReturn", totalAnnualizedReturn.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString());
            stats.put("currency", currency);

            events.keySet().forEach(date -> {
                if (!priceDates.contains(date)) {
                    throw new RuntimeException("No price for the event date " + date);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse file " + priceFilename);
        }
    }

    private List<String> getLines(String filename) throws IOException, URISyntaxException {
        URL resource = getClass().getResource(filename);
        File file = new File(resource.toURI());
        List<String> lines = Files.readAllLines(file.toPath());
        lines = lines.subList(getHeaderRows(), lines.size());

        if(reversedTimeline()){
            Collections.reverse(lines);
        }

        return lines;
    }

    public BigDecimal getYearsDifference(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        BigDecimal diffInDays = new BigDecimal(TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS));
        return diffInDays.divide(new BigDecimal(365), 10, RoundingMode.HALF_UP);
    }

    public BigDecimal getAnnualizedReturn(BigDecimal returnPercentage, BigDecimal years){
        return BigDecimalMath.pow(returnPercentage.add(BigDecimal.ONE), BigDecimal.ONE.divide(years, 10, RoundingMode.HALF_UP)).subtract(BigDecimal.ONE);
    }
}
