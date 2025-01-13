package mg.sandratra.bakery.enums;

public enum Role {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    STAFF("STAFF");

    private String displayValue;

    Role(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getRoleName() {
        return displayValue;
    }

    public String getDisplayValue() {
        return name();
    }

    @Override
    public String toString() {
        return displayValue;
    }
}