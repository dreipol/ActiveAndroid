package ch.dreipol.android.blinq.services.model;

import java.util.Arrays;

/**
 * Created by melbic on 25/08/14.
 */
public enum GenderInterests {
    FEMALE,
    MALE,
    BOTH;

    public String toString() {
        return Arrays.toString(toArray());
    }

    public String[] toArray() {
        String[] result = new String[0];
        switch (this) {
            case FEMALE:
                result = new String[]{"f"};
                break;
            case MALE:
                result = new String[]{"m"};
                break;
            case BOTH:
                result = new String[]{"f", "m"};
                break;
        }
        return result;
    }

}

