package ch.dreipol.android.blinq.services;

public enum ServerStatus {
    INITIAL("initial"),
    PENDING("pending"),
    CANCEL("cancel"),
    SUCCESS("success");

    private String text;

    ServerStatus(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static ServerStatus fromString(String text) {
        if (text != null) {
            for (ServerStatus s : ServerStatus.values()) {
                if (text.equalsIgnoreCase(s.text)) {
                    return s;
                }
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
