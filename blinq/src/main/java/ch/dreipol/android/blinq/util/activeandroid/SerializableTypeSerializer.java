package ch.dreipol.android.blinq.util.activeandroid;

import com.activeandroid.serializer.TypeSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import ch.dreipol.android.blinq.services.model.MutualData;

/**
 * Created by melbic on 21/08/14.
 */
public abstract class SerializableTypeSerializer<T extends Serializable> extends TypeSerializer {

    @Override
    public Class<?> getSerializedType() {
        return byte[].class;
    }

    @Override
    public byte[] serialize(Object data) {
        if (data == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(baos).writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    @Override
    public T deserialize(Object data) {
        if (data == null) {
            return null;
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream((byte[]) data);
        Object readObject;
        try {
            readObject = new ObjectInputStream(inputStream).readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return (T) readObject;
    }
}