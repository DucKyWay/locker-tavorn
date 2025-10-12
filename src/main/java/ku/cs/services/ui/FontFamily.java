package ku.cs.services.ui;

public enum FontFamily {
    BAI_JAMJUREE("Bai Jamjuree"),
    SARABUN("Sarabun");

    private final String familyName;

    FontFamily(String name) {
        this.familyName = name;
    }

    public String getFamilyName() {
        return familyName;
    }
}
