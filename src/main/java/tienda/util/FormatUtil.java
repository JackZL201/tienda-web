package tienda.util;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtil {
    public String moneda(double v) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es","MX"));
        return nf.format(v);
    }
}
