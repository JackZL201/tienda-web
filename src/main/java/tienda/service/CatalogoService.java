package tienda.service;

import tienda.models.Categoria;
import tienda.models.Producto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CatalogoService {
    private final Map<Categoria, List<Producto>> cache = new EnumMap<>(Categoria.class);

    public CatalogoService() {
        cargarDesdeCSV();
    }

    private void cargarDesdeCSV() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/catalogo.csv")),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) continue;
                String[] p = line.split(",", -1);
                Categoria sub = Categoria.valueOf(p[0].trim());
                String nombre = p[1].trim();
                double precio = Double.parseDouble(p[2].trim());
                cache.computeIfAbsent(sub, k -> new ArrayList<>())
                        .add(new Producto(nombre, precio, sub));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error cargando catalogo.csv", e);
        }
    }

    public Map<Categoria, List<Producto>> obtenerCatalogo(Categoria subcat) {
        Map<Categoria, List<Producto>> m = new EnumMap<>(Categoria.class);
        m.put(subcat, cache.getOrDefault(subcat, List.of()));
        return m;
    }
}
