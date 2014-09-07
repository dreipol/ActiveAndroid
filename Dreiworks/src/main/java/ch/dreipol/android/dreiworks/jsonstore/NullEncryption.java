package ch.dreipol.android.dreiworks.jsonstore;

import java.nio.charset.Charset;

/**
 * Created by melbic on 26/08/14.
 *
 * Null implementation of the Encryption interface.
 */
public class NullEncryption implements StringEncryption {

    private static Charset getCharset() {
        return Charset.forName("UTF-8");
    }

    @Override
    public byte[] encrypt(String plainText, String IV) {
        return plainText.getBytes(getCharset());
    }


    @Override
    public String decrypt(byte[] encryptedBytes, String IV) {
        return new String(encryptedBytes, getCharset());
    }
}
