package com.tiendaweb.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import tienda.models.Carrito;

@Component
@SessionScope
public class CartSession {
    private final Carrito carrito = new Carrito();
    public Carrito getCarrito() { return carrito; }
}
