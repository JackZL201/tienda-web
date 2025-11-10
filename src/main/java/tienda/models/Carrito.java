package tienda.models;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private final List<ItemCarrito> items = new ArrayList<>();
    public List<ItemCarrito> getItems() { return items; }

    public void agregar(Producto p, int cantidad) {
        for (ItemCarrito it : items) {
            if (it.getProducto().getNombre().equals(p.getNombre())) {
                it.setCantidad(it.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new ItemCarrito(p, cantidad));
    }
}
