package tienda.service;

import tienda.models.Carrito;
import tienda.models.Descuento;

public class DescuentoService {

    // Un solo descuento por compra (ejemplo: 10% si subtotal >= 3000)
    public Descuento calcularDescuentoUnico(Carrito carrito, String cupon) {
        double subtotal = carrito.getItems().stream().mapToDouble(it -> it.getSubtotal()).sum();
        if (subtotal >= 3000) {
            return new Descuento("Descuento por volumen (10%)", subtotal * 0.10);
        }
        return null;
    }
}
