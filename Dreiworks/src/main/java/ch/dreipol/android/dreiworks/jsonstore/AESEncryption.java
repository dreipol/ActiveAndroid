package ch.dreipol.android.dreiworks.jsonstore;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by melbic on 26/08/14.
 * <p/>
 * Inspired by https://gist.github.com/bricef/2436364
 */

public class AESEncryption implements StringEncryption {


    final static private String mEncryptionKey = "Json(value)28723405257303;Hello3";
    @Override
    public byte[] encrypt(String plainText, String IV) throws EncryptionException {
        try {
            return encrypt(plainText, mEncryptionKey, IV);
        } catch (Exception e) {
            throw new EncryptionException("The encryption wasn't successful.", e);
        }
    }

    @Override
    public String decrypt(byte[] encryptedString, String IV) throws EncryptionException {
        try {
            return decrypt(encryptedString, mEncryptionKey, IV);
        } catch (Exception e) {
            throw new EncryptionException("The decryption wasn't successful.", e);
        }
    }


    private static byte[] encrypt(String plainText, String encryptionKey, String IV) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = getCipher(encryptionKey, IV, Cipher.ENCRYPT_MODE);
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    private static Cipher getCipher(String encryptionKey, String IV, int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        byte[] bytes = getIVBytes(IV);
        cipher.init(mode, key, new IvParameterSpec(bytes));
        return cipher;
    }

    private static String decrypt(byte[] cipherText, String encryptionKey, String IV) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = getCipher(encryptionKey, IV, Cipher.DECRYPT_MODE);
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }

    private static byte[] getIVBytes(String IV) throws UnsupportedEncodingException {
        final int length = 16;
        byte[] bytes = IV.getBytes("UTF-8");
        if (bytes.length < length) {
            return getIVBytes(IV + IV);
        } else if (bytes.length > length) {
            bytes = Arrays.copyOf(bytes, length);
        }
        return bytes;
    }
}
