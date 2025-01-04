package mg.sandratra.bakery.util.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Option {
    private String value;
    private String text;

    public Option(String value, String text) {
        this.value = value;
        this.text = text;
    }
}