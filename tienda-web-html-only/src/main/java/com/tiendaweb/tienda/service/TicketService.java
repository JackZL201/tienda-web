package com.tiendaweb.tienda.service;

import com.tiendaweb.tienda.models.Carrito;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    public Map<String, Object> calcular(Carrito carrito, double iva, double descuentoPercent) {
        Map<String, Object> model = new HashMap<>();
        double subtotal = carrito.getSubtotal();
        double descuento = -(subtotal * (descuentoPercent/100.0));
        double ivaMonto = (subtotal + descuento) * (iva/100.0);
        double total = subtotal + descuento + ivaMonto;

        model.put("items", carrito.getItems());
        model.put("subtotal", subtotal);
        model.put("descuento", descuento);
        model.put("iva", ivaMonto);
        model.put("total", total);
        return model;
    }
}
