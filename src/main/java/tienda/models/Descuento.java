package tienda.models;

public class Descuento {
    private final String etiqueta;
    private final double monto;
    public Descuento(String etiqueta, double monto) { this.etiqueta = etiqueta; this.monto = monto; }
    public String getEtiqueta() { return etiqueta; }
    public double getMonto() { return monto; }
}
