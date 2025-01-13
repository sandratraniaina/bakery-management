package mg.sandratra.bakery.enums;

public enum Role {
    ADMIN("Admin"),
    MANAGER("Manager"),
    STAFF("Staff");

    private final String displayValue;

    Role(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getRoleName() {
        return displayValue;
    }

    @Override
    public String toString() {
        return displayValue;
    }
}