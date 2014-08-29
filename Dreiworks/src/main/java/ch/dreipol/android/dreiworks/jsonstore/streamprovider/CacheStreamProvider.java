package ch.dreipol.android.dreiworks.jsonstore.streamprovider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by melbic on 26/08/14.
 * <p/>
 * For test purposes only the provider caches the output stream to reuse it as input stream.
 */
public class CacheStreamProvider implements InputOutputStreamProvider {

    private HashMap<String, ByteArrayOutputStream> mByteArrayMap;

    public CacheStreamProvider() {
        mByteArrayMap = new HashMap<String, ByteArrayOutputStream>();
    }

    @Override
    public InputStream getInputStream(String fileName) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = mByteArrayMap.get(fileName);

        if (byteArrayOutputStream == null) {
            throw new IOException("The corresponding outputStream doesn't exist.");
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public OutputStream getOutputStream(String fileName) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = mByteArrayMap.get(fileName);
        if (byteArrayOutputStream == null) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            mByteArrayMap.put(fileName, byteArrayOutputStream);
        }
        return byteArrayOutputStream;
    }

    @Override
    public void removeFile(String fileName) {
        mByteArrayMap.remove(fileName);
    }

    @Override
    public void clear() {
        mByteArrayMap.clear();
    }
}
