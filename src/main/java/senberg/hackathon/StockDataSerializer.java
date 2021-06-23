package senberg.hackathon;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StockDataSerializer extends StdSerializer<StockData> {
    protected StockDataSerializer() {
        super(StockData.class);
    }

    @Override
    public void serialize(StockData stockData, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("data");
            jsonGenerator.writeStartArray();
                stockData.data.forEach(data -> {
                    try {
                        jsonGenerator.writeObject(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            jsonGenerator.writeEndArray();
            jsonGenerator.writeFieldName("events");
            jsonGenerator.writeStartArray();
                stockData.events.values().stream().sorted(Event::compareDates).forEach(event -> {
                    try {
                        jsonGenerator.writeObject(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            jsonGenerator.writeEndArray();
            jsonGenerator.writeFieldName("stats");
            jsonGenerator.writeObject(stockData.stats);
        jsonGenerator.writeEndObject();
    }
}
