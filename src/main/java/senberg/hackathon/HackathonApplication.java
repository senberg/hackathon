package senberg.hackathon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@SpringBootApplication
@RestController
public class HackathonApplication {

    @Autowired
    StockService stockService;

    public static void main(String[] args) {
        SpringApplication.run(HackathonApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

    @GetMapping("/getStocks")
    public Set<String> getStocks() {
        return stockService.stocks.keySet();
    }

    @GetMapping("/getStockData")
    public StockData getStockData(@RequestParam String name) {
        return stockService.stocks.get(name);
    }
}
