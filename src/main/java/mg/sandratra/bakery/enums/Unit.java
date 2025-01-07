package mg.sandratra.bakery.enums;

import lombok.Getter;

@Getter
public enum Unit {
    KG("KG"), G("G"), L("L"), ML("ML");

    private final String value;

    Unit(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name();
    }
}
