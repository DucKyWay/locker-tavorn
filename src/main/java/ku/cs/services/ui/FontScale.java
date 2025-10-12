package ku.cs.services.ui;

public enum FontScale {
    REGULAR(14, "ปกติ"),   // baseline 14px
    LARGE(16, "ใหญ่");     // baseline 16px

    private final int baseSize;
    private final String label;

    FontScale(int baseSize, String label) {
        this.baseSize = baseSize;
        this.label = label;
    }

    public int getBaseSize() {
        return baseSize;
    }

    public String getLabel() {
        return label;
    }
}
