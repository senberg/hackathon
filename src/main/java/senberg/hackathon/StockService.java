package senberg.hackathon;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;

@Service
public class StockService {
    LinkedHashMap<String, StockData> stocks = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        stocks.put("Handelsbanken B", new OmniValueStockData("/data/SHB-B-1990-11-21-2021-06-21.csv", "/data/SHB-B-1990-11-20-2021-06-21-events.csv", "SEK"));
        stocks.put("Ericsson B", new OmniValueStockData("/data/ERIC-B-1987-01-02-2021-06-21.csv", "/data/ERIC-B-1987-04-01-2021-06-21-events.csv", "SEK"));
        stocks.put("Tesla", new YahooStockData("/data/TSLA.csv", "/data/TSLA-events.csv", "USD"));
        stocks.put("Microsoft", new YahooStockData("/data/MSFT.csv", "/data/MSFT-events.csv", "USD"));
    }
}
