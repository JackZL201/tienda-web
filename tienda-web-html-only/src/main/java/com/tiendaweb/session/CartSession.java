package com.tiendaweb.session;

import com.tiendaweb.tienda.models.Carrito;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class CartSession {
    private final Carrito carrito = new Carrito();
    public Carrito get() { return carrito; }
}
