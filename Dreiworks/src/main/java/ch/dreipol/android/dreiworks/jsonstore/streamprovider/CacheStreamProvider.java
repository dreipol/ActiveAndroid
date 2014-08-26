package ch.dreipol.android.dreiworks.jsonstore.streamprovider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by melbic on 26/08/14.
 *
 * For test purposes only the provider caches the output stream to reuse it as input stream.
 * */
public class CacheStreamProvider implements InputOutputStreamProvider {

    private ByteArrayOutputStream mByteArrayOutputStream;

    public CacheStreamProvider() {
        mByteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public InputStream getInputStream(String fileName) throws IOException {
        return new ByteArrayInputStream(mByteArrayOutputStream.toByteArray());
    }

    @Override
    public OutputStream getOutputStream(String fileName) throws IOException {
        return mByteArrayOutputStream;
    }
}
