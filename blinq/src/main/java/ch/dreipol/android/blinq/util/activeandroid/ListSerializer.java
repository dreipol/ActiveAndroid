package ch.dreipol.android.blinq.util.activeandroid;

import com.activeandroid.serializer.TypeSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melbic on 21/08/14.
 */
public class ListSerializer extends SerializableTypeSerializer<ArrayList<Serializable>> {
    @Override
    public Class<?> getDeserializedType() {
        return List.class;
    }
}
