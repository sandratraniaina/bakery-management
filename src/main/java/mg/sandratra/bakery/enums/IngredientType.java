package mg.sandratra.bakery.enums;

public enum IngredientType {
    ADD_INS("Add-ins"),
    FLOUR("Flour"),
    BASE("Base");

    private final String displayValue;
    private final String value;

    IngredientType(String displayValue) {
        this.displayValue = displayValue;
        this.value = name();
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public String getValue() {
        return value;
    } 
}
