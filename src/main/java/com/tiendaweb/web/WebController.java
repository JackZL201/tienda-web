package com.tiendaweb.web;

import com.tiendaweb.mongo.VentaDoc;
import com.tiendaweb.mongo.VentaRepository;
import com.tiendaweb.session.CartSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tienda.models.*;
import tienda.service.CatalogoService;
import tienda.service.DescuentoService;
import tienda.service.TicketService;
import tienda.util.FormatUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

  private final CatalogoService catalogoService = new CatalogoService();
  private final DescuentoService descuentoService = new DescuentoService();
  private final TicketService ticketService = new TicketService();
  private final CartSession cart;
  private final VentaRepository ventas;

  public WebController(CartSession cart, VentaRepository ventas) {
    this.cart = cart;
    this.ventas = ventas;
  }

  @GetMapping("/")
  public String index(@RequestParam(required=false) Categoria categoria,
                      @RequestParam(required=false) Categoria subcat,
                      Model model) {

    List<Categoria> principales = List.of(
      Categoria.LECHE, Categoria.YOGURT, Categoria.MANTEQUILLA_MARGARINA,
      Categoria.SNACKS, Categoria.LIMPIEZA, Categoria.DETERGENTES,
      Categoria.LAVATRASTES, Categoria.BEBIDAS
    );

    List<Categoria> subcats = List.of();
    if (categoria != null) {
      switch (categoria) {
        case LECHE -> subcats = List.of(Categoria.LECHE_ENTERA, Categoria.LECHE_DESLACTOSADA, Categoria.LECHE_SABORIZADA);
        case YOGURT -> subcats = List.of(Categoria.YOGURT_BEBIBLE, Categoria.YOGURT_NATURAL, Categoria.YOGURT_GRIEGO);
        case SNACKS -> subcats = List.of(Categoria.GALLETAS, Categoria.BOTANAS, Categoria.PASTELITOS);
        default -> subcats = List.of(categoria);
      }
      if (subcat == null && !subcats.isEmpty()) subcat = subcats.get(0);
    }

    List<Producto> productos = List.of();
    if (subcat != null) {
      Map<Categoria, List<Producto>> map = catalogoService.obtenerCatalogo(subcat);
      productos = map.getOrDefault(subcat, List.of());
    }

    Descuento desc = descuentoService.calcularDescuentoUnico(cart.getCarrito(), null);
    TicketService.Totales tot = ticketService.calcularTotales(cart.getCarrito(), desc);

    model.addAttribute("principales", principales);
    model.addAttribute("categoria", categoria);
    model.addAttribute("subcats", subcats);
    model.addAttribute("subcat", subcat);
    model.addAttribute("productos", productos);
    model.addAttribute("items", cart.getCarrito().getItems());
    model.addAttribute("format", new FormatUtil());
    model.addAttribute("desc", desc);
    model.addAttribute("tot", tot);

    return "index";
  }

  @PostMapping("/add")
  public String add(@RequestParam Categoria subcat,
                    @RequestParam String productoNombre,
                    @RequestParam int cantidad) {
    Map<Categoria, List<Producto>> map = catalogoService.obtenerCatalogo(subcat);
    Producto elegido = map.getOrDefault(subcat, List.of())
                          .stream().filter(p -> p.getNombre().equals(productoNombre))
                          .findFirst().orElse(null);
    if (elegido != null) cart.getCarrito().agregar(elegido, cantidad);
    return "redirect:/?categoria="+subcat.getRaiz()+"&subcat="+subcat;
  }

  @PostMapping("/remove")
  public String remove(@RequestParam int index,
                       @RequestParam(required=false) Categoria categoria,
                       @RequestParam(required=false) Categoria subcat) {
    if (index>=0 && index<cart.getCarrito().getItems().size())
      cart.getCarrito().getItems().remove(index);
    return "redirect:/?categoria="+(categoria!=null?categoria:"")+"&subcat="+(subcat!=null?subcat:"");
  }

  @PostMapping("/checkout")
  public @ResponseBody FileSystemResource checkout(@RequestParam String nombre,
                                                   @RequestParam String email,
                                                   HttpServletResponse resp) throws Exception {

    Descuento desc = descuentoService.calcularDescuentoUnico(cart.getCarrito(), null);
    TicketService.Totales tot = ticketService.calcularTotales(cart.getCarrito(), desc);

    String filename = "ticket_" + nombre.trim().replaceAll("\\s+","_") + ".pdf";
    File out = File.createTempFile("ticket_", ".pdf");
    new tienda.pdf.PdfBoxGenerator().generateTicket(out.getAbsolutePath(), nombre, email, cart.getCarrito(), desc, tot);

    VentaDoc v = new VentaDoc();
    v.nombre = nombre; v.email = email;
    v.subtotal = tot.subtotal; v.descuento = tot.descuento; v.iva = tot.iva; v.total = tot.total;
    v.pdfNombre = filename;
    v.items = new ArrayList<>();
    for (var it : cart.getCarrito().getItems()) {
      var vi = new VentaDoc.Item();
      vi.producto = it.getProducto().getNombre();
      vi.cantidad = it.getCantidad();
      vi.subtotal = it.getSubtotal();
      v.items.add(vi);
    }
    ventas.save(v);

    resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
    resp.setContentType(MediaType.APPLICATION_PDF_VALUE);

    cart.getCarrito().getItems().clear();

    return new FileSystemResource(out);
  }
}
