package com.tiendaweb.tienda.service;

import com.tiendaweb.tienda.models.Producto;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatalogoService {

    private final Map<String, Producto> byCode = new LinkedHashMap<>();

    @PostConstruct
    public void load() {
        try {
            ClassPathResource res = new ClassPathResource("catalogo.csv");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty() || line.startsWith("#")) continue;
                    String[] p = line.split(";");
                    if (p.length < 5) continue;
                    Producto prod = new Producto(p[0], p[1], p[2], Double.parseDouble(p[3]), p[4]);
                    byCode.put(prod.getCodigo(), prod);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar catalogo.csv", e);
        }
    }

    public List<String> categorias() {
        return byCode.values().stream().map(Producto::getCategoria).distinct().sorted().collect(Collectors.toList());
    }

    public Collection<Producto> todos() { return byCode.values(); }

    public List<Producto> productosDe(String categoria) {
        return byCode.values().stream().filter(p -> p.getCategoria().equalsIgnoreCase(categoria)).collect(Collectors.toList());
    }

    public Optional<Producto> findByCodigo(String codigo) {
        return Optional.ofNullable(byCode.get(codigo));
    }
}
