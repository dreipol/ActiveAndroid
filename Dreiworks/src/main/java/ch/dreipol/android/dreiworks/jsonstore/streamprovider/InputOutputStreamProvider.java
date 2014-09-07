package ch.dreipol.android.dreiworks.jsonstore.streamprovider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by melbic on 26/08/14.
 */
public interface InputOutputStreamProvider {
    public InputStream getInputStream(String fileName) throws IOException;

    public OutputStream getOutputStream(String fileName) throws IOException;

    void removeFile(String fileName);

    void clear();
}
