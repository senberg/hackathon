package senberg.hackathon;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EventSerializer extends StdSerializer<Event> {
    protected EventSerializer() {
        super(Event.class);
    }

    @Override
    public void serialize(Event event, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeNumber(event.date.getTime());
        jsonGenerator.writeString(event.getDescription());
        jsonGenerator.writeEndArray();
    }
}
