package com.tiendaweb.tienda.models;

public class Producto {
    private final String nombre;
    private final double precio;
    private final Categoria categoria;

    public Producto(String nombre, double precio, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public Categoria getCategoria() { return categoria; }

    @Override public String toString() { return nombre + " - $" + precio; }
}
