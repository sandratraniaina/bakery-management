package mg.sandratra.bakery.enums;

public enum IngredientType {
    ADD_INS("Add-ins"),
    FLOUR("Flour"),
    BASE("Base");

    private final String displayValue;

    IngredientType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
