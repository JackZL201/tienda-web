package tienda.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import tienda.models.*;
import tienda.service.*;
import tienda.util.FormatUtil;

@Controller
public class WebController {

    private final CatalogoService catalogoService = new CatalogoService();
    private final DescuentoService descuentoService = new DescuentoService();
    private final TicketService ticketService = new TicketService();

    @GetMapping("/")
    public String home() {
        return "index"; // opcional
    }

    // Vista del ticket en HTML
    @GetMapping("/ticket")
    public String ticketHtml(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String subcategoria,
            @RequestParam(required = false) String producto,
            @RequestParam(required = false, defaultValue = "1") int cantidad,
            @RequestParam(required = false) String cupon,
            Model model) {

        // Construir carrito breve de ejemplo (o usa tus params)
        Carrito carrito = new Carrito();
        if (producto != null) {
            Producto p = catalogoService.buscarProductoPorNombre(producto);
            if (p != null) carrito.agregar(p, cantidad);
        }

        Descuento desc = descuentoService.calcularDescuentoUnico(carrito, cupon);
        TicketService.Totales tot = ticketService.calcularTotales(carrito, desc);

        model.addAttribute("nombre", nombre);
        model.addAttribute("email", email);
        model.addAttribute("carrito", carrito);
        model.addAttribute("descuento", desc);
        model.addAttribute("tot", tot);
        model.addAttribute("fmt", new FormatUtil()); // para helper de moneda

        return "ticket";
    }

    // Descargar el HTML del ticket (Content-Disposition: attachment)
    @GetMapping("/ticket/download/html")
    @ResponseBody
    public org.springframework.http.ResponseEntity<String> downloadTicketHtml(
            @RequestParam String nombre,
            @RequestParam String email) {

        String html = """
          <!doctype html>
          <html lang="es"><head><meta charset="utf-8"><title>Ticket</title>
          <style>body{font-family:Arial;margin:24px} .logo{max-height:72px}</style>
          </head><body>
            <div style="text-align:center">
              <img class="logo" src="/img/logo.png" alt="logo"/>
              <h2>Ticket de compra</h2>
            </div>
            <p><b>Cliente:</b> %s<br><b>Email:</b> %s</p>
            <p>Este ticket fue generado como HTML descargable.</p>
          </body></html>
        """.formatted(nombre, email);

        return org.springframework.http.ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=ticket.html")
                .body(html);
    }
}
