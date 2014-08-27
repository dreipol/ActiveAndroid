package ch.dreipol.android.blinq.services.model;

import java.util.Arrays;

/**
 * Created by melbic on 25/08/14.
 */
public enum GenderInterests {
    NO_INTERESTS(""),
    FEMALE("f"),
    MALE("m"),
    BOTH("f,m");
    private final String interestedIn;

    private GenderInterests(String s) {
        this.interestedIn = s;
    }

    public String toString() {
        return this.interestedIn;
    }

    public String[] toArray() {
        return interestedIn.split(",");
    }


    public static GenderInterests createFromArray(String[] strings) {
        GenderInterests interests;
        switch (strings.length) {
            case 0:
                interests = NO_INTERESTS;
                break;
            case 1:
                interests = GenderInterests.valueOf(strings[0]);
                break;
            case 2:
            default:
                interests = GenderInterests.BOTH;
                break;
        }
        return interests;
    }
}

