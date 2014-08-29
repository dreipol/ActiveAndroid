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
    private String mSuffix;

    public AndroidStreamProvider(Context context, String suffix) {

        mContext = context;
        mSuffix = "_" + suffix;
    }

    @Override
    public InputStream getInputStream(String fileName) throws IOException {
        return mContext.openFileInput(addSuffix(fileName));
    }

    @Override
    public OutputStream getOutputStream(String fileName) throws IOException {
        return mContext.openFileOutput(addSuffix(fileName), Context.MODE_PRIVATE);
    }

    @Override
    public void removeFile(String fileName) {
        mContext.deleteFile(addSuffix(fileName));
    }

    @Override
    public void clear()  {
        for (String fileName : mContext.fileList()) {
            if (fileName.endsWith(mSuffix)) {
                mContext.deleteFile(fileName);
            }
        }
    }

    private String addSuffix(String fileName) {

        return fileName + mSuffix;
    }

}
