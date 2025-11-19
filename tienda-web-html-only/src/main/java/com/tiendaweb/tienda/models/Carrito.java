package com.tiendaweb.tienda.models;

import java.util.*;

public class Carrito {
    private final Map<String, ItemCarrito> items = new LinkedHashMap<>();

    public Collection<ItemCarrito> getItems() { return items.values(); }

    public void add(Producto p, int cantidad) {
        ItemCarrito it = items.get(p.getCodigo());
        if (it == null) items.put(p.getCodigo(), new ItemCarrito(p, cantidad));
        else it.setCantidad(it.getCantidad() + cantidad);
    }

    public void remove(String codigo) { items.remove(codigo); }

    public double getSubtotal() {
        return items.values().stream().mapToDouble(ItemCarrito::getImporte).sum();
    }
}
