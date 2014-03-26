package ch.dreipol.android.blinq.services;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Samuel Bichsel, dreipol GmbH on 29.01.14.
 */
public class ConnectionSignatureCreator {
    private final static String SECRET = "[NSURL URLWithString:@https://beta.server.blinqapp.ch/]";
    private final static String ALGORITHM = "HmacSHA256";

    public static String signatureForCredentials(ICredentials credentials) {
        String data = String.format("userId=%s&token=%s", credentials.getUserID(), credentials.getUserID());

        return hmacsha256(data, SECRET);
    }

    public static String hmacsha256(String data, String key) {
        String output = "";
        try {
            byte[] bytes;
            SecretKeySpec keySpec = new SecretKeySpec(SECRET.getBytes(), ALGORITHM);
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(keySpec);
            bytes = mac.doFinal(key.getBytes());
            output = convertBytesToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    private static String convertBytesToString(byte[] bytes) {
        StringBuilder hash = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                hash.append('0');
            }
            hash.append(hex);
        }
        return hash.toString();
    }

//    public static void main(String[] args) {
//        String k = "7dedc6f9c541617b";
//        String r = "5ac9563ca6b14115f2f2846c56176d71030bd132c5817532daf202cf4b47383c";
//
//        String t = tokenForKey(k);
//        Boolean b= t.equalsIgnoreCase(r);
//        int i = 0;
//    }
}

