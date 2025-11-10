package tienda.service;

import tienda.models.Carrito;
import tienda.models.Descuento;

public class TicketService {
    public static class Totales {
        public double subtotal, descuento, iva, total;
    }
    public Totales calcularTotales(Carrito carrito, Descuento desc) {
        Totales t = new Totales();
        t.subtotal = carrito.getItems().stream().mapToDouble(i -> i.getSubtotal()).sum();
        t.descuento = (desc != null ? desc.getMonto() : 0.0);
        double base = Math.max(0, t.subtotal - t.descuento);
        t.iva = base * 0.16;
        t.total = base + t.iva;
        return t;
    }
}
