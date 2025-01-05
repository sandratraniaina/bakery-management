package mg.sandratra.bakery.enums;

public enum Unit {
    KG, G, L, ML;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
