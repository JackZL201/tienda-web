package com.tiendaweb.tienda.service;

import com.tiendaweb.session.CartSession;
import com.tiendaweb.tienda.models.Carrito;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final CartSession cartSession;
    private final DescuentoService descuentoService;

    public TicketService(CartSession cartSession, DescuentoService descuentoService) {
        this.cartSession = cartSession;
        this.descuentoService = descuentoService;
    }

    /** Carrito de la sesión actual */
    public Carrito getCarritoActual() {
        return cartSession.getCarrito();
    }

    /** Descuento aplicado según el subtotal actual del carrito */
    public double getDescuentoAplicado() {
        Carrito c = getCarritoActual();
        double subtotal = (c != null) ? c.getSubtotal() : 0.0;
        return descuentoService.calcularDescuento(subtotal);
    }
}
