package ch.dreipol.android.dreiworks.jsonstore;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import ch.dreipol.android.dreiworks.jsonstore.streamprovider.InputOutputStreamProvider;


/**
 * Created by melbic on 26/08/14.
 */
public class JsonStore implements IJsonStore {

    private Gson mGson;
    private InputOutputStreamProvider mProvider;
    private StringEncryption mEncryption;


    public JsonStore(Gson gson, InputOutputStreamProvider provider) {
        this(gson, provider, new NullEncryption());
    }

    public JsonStore(Gson gson, InputOutputStreamProvider provider, StringEncryption encryption) {
        mGson = gson;
        mProvider = provider;
        mEncryption = encryption;
    }

    private static String getFileName(String key) {
        return key + ".json";
    }

    @Override
    public <T> void put(String key, T value) throws IOException {
        final String jsonString = mGson.toJson(value);
        final String fileName = getFileName(key);
        readWrite(mProvider.getOutputStream(fileName), new StreamFunction<OutputStream, Void>() {
            @Override
            public Void call(OutputStream stream) throws IOException, EncryptionException {
                byte[] bytes = mEncryption.encrypt(jsonString, fileName);
                stream.write(bytes);
                return null;
            }

        });
    }

    @Override
    public <T> T get(String key, final Class<T> clazz) throws IOException {
        return getByType(key, clazz);
    }

    @Override
    public <T> T get(String key, final TypeToken<T> type) throws IOException {
        return getByType(key, type.getType());
    }

    @Override
    public <T> T getByType(String key, final Type type) throws IOException {
        final String fileName = getFileName(key);
        return readWrite(mProvider.getInputStream(fileName), new StreamFunction<InputStream, T>() {
            @Override
            public T call(InputStream inputStream) throws IOException, EncryptionException {
                byte[] bytes = ByteStreams.toByteArray(inputStream);
                String decryptedString = mEncryption.decrypt(bytes, fileName);
                return mGson.fromJson(decryptedString, type);
            }
        });
    }

    @Override
    public void remove(String fileName) {
        mProvider.removeFile(fileName);
    }

    private <C extends Closeable, R> R readWrite(C c, StreamFunction<C, R> func) throws IOException {
        Closer closer = Closer.create();
        try {
            C fis = closer.register(c);
            return func.call(fis);
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    interface StreamFunction<I, O> {
        public O call(I stream) throws IOException, EncryptionException;
    }
}
