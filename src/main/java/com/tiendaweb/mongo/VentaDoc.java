package com.tiendaweb.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document("ventas")
public class VentaDoc {
  @Id public String id;
  public String nombre;
  public String email;
  public List<Item> items;
  public double subtotal;
  public double descuento;
  public double iva;
  public double total;
  public Date fecha = new Date();
  public String pdfNombre;

  public static class Item {
    public String producto;
    public int cantidad;
    public double subtotal;
  }
}
