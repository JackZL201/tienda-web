package com.tiendaweb.tienda.service;

import org.springframework.stereotype.Service;

@Service
public class DescuentoService {

    /**
     * Regla simple: 10% si el subtotal supera 3000.
     * Ajusta la lógica como necesites.
     */
    public double calcularDescuento(double subtotal) {
        if (subtotal >= 3000.0) {
            return subtotal * 0.10;
        }
        return 0.0;
    }
}
