package com.tiendaweb.tienda.service;

import com.tiendaweb.tienda.models.Producto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoService {
    // Tu implementación real (Mongo/CSV/memoria)…
    public List<Producto> getProductos() { /* ... */ return List.of(); }
    public List<String> getCategorias() { /* ... */ return List.of(); }

    public Producto buscarProductoPorNombre(String nombre) {
        return getProductos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }
}
