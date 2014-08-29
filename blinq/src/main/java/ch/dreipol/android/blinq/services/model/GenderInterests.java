package ch.dreipol.android.blinq.services.model;

/**
 * Created by melbic on 25/08/14.
 */
public enum GenderInterests {
    NO_INTERESTS(""),
    FEMALE("f"),
    MALE("m"),
    BOTH("f,m");
    private final String text;

    private GenderInterests(String s) {
        this.text = s;
    }

    public String toString() {
        return this.text;
    }

    public String[] toArray() {
        return text.split(",");
    }


    public static GenderInterests create(String[] strings) {
        GenderInterests interests;
        switch (strings.length) {
            case 0:
                interests = NO_INTERESTS;
                break;
            case 1:
                interests = fromString(strings[0]);
                break;
            case 2:
            default:
                interests = GenderInterests.BOTH;
                break;
        }
        return interests;
    }

    public static GenderInterests fromString(String text) {
        if (text != null) {
            for (GenderInterests i : GenderInterests.values()) {
                if (text.equalsIgnoreCase(i.text)) {
                    return i;
                }
            }
        }
        return null;
    }
}

