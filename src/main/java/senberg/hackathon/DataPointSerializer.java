package senberg.hackathon;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class DataPointSerializer extends StdSerializer<DataPoint> {
    protected DataPointSerializer() {
        super(DataPoint.class);
    }

    @Override
    public void serialize(DataPoint dataPoint, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeNumber(dataPoint.date.getTime());
        jsonGenerator.writeNumber(dataPoint.closingPrice);
        jsonGenerator.writeNumber(dataPoint.totalReturn);
        jsonGenerator.writeEndArray();
    }
}
