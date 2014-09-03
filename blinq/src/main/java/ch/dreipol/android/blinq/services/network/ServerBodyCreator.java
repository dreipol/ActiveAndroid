package ch.dreipol.android.blinq.services.network;

import java.util.HashMap;

/**
 * Created by melbic on 03/09/14.
 */
public abstract class ServerBodyCreator {

    public static HashMap<String, Object> create(String key, Object value) {
        HashMap<String, Object> body = new HashMap<String, Object>(4);
        body.put(key, value);
        return body;
    }
}
