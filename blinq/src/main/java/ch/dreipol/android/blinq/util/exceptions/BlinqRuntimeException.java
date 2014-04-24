package ch.dreipol.android.blinq.util.exceptions;

/**
 * Created by phil on 24.04.14.
 */
public class BlinqRuntimeException extends RuntimeException{
    public BlinqRuntimeException(String detailMessage) {
        super("BLINQ Runtime Exception: " + detailMessage);
    }
}
