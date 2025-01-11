package mg.sandratra.bakery.enums;

public enum ProductType {
    VIENNOISERIE("VIENNOISERIE"),
    AUTRE("AUTRE");

    private final String value;

    ProductType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
