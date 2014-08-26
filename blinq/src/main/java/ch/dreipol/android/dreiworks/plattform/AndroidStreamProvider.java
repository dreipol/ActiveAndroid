package ch.dreipol.android.dreiworks.plattform;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.dreipol.android.dreiworks.jsonstore.streamprovider.InputOutputStreamProvider;

/**
 * Created by melbic on 26/08/14.
 */
public class AndroidStreamProvider implements InputOutputStreamProvider {

    private Context mContext;

    public AndroidStreamProvider(Context context) {

        mContext = context;
    }

    @Override
    public InputStream getInputStream(String fileName) throws IOException {
        return mContext.openFileInput(fileName);
    }

    @Override
    public OutputStream getOutputStream(String fileName) throws IOException {
        return mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
    }
}
