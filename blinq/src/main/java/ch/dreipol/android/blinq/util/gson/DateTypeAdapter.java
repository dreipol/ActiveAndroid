package ch.dreipol.android.blinq.util.gson;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by melbic on 20/08/14.
 */
public class DateTypeAdapter extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == Date.class ? (TypeAdapter<T>) new DateTypeAdapter() : null;
        }
    };


    private final DateFormat blinqOnlyDateFormat = buildBlinqDateFormat();
    private final DateFormat blinqFormat = buildBlinqFormat();
    private static DateFormat buildBlinqFormat() {
        DateFormat blinqFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ZZZ");
        blinqFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return blinqFormat;
    }
    private static DateFormat buildBlinqDateFormat() {
        DateFormat blinqFormat = new SimpleDateFormat("MM/dd/yyyy");
        blinqFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return blinqFormat;
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return deserializeToDate(in.nextString());
    }

    private synchronized Date deserializeToDate(String json) {
        try {
            return blinqFormat.parse(json);
        } catch (ParseException ignored) {
        }
        try {
            return blinqOnlyDateFormat.parse(json);
        } catch (ParseException e) {
            throw new JsonSyntaxException(json, e);
        }
    }

    @Override
    public synchronized void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        String dateFormatAsString = blinqFormat.format(value);
        out.value(dateFormatAsString);
    }
}
