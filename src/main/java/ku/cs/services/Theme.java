package ku.cs.services;

public enum Theme {
    LIGHT("themes/light-theme.css"),
    DARK("themes/dark-theme.css");

    private final String cssFile;

    Theme(String cssFile) {
        this.cssFile = cssFile;
    }

    public String getCssFile() {
        return cssFile;
    }
}
