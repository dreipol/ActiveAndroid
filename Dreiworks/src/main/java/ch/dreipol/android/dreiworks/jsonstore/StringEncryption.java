package ch.dreipol.android.dreiworks.jsonstore;

/**
 * Created by melbic on 26/08/14.
 */
public interface StringEncryption {

    public byte[] encrypt(String plainText, String IV) throws EncryptionException;

    public String decrypt(byte[] encryptedString, String IV) throws EncryptionException;

}
