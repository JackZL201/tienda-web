package com.tiendaweb.web;

import com.tiendaweb.tienda.models.Carrito;
import com.tiendaweb.tienda.models.Producto;
import com.tiendaweb.tienda.service.CatalogoService;
import com.tiendaweb.tienda.service.TicketService;
import com.tiendaweb.session.CartSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final CatalogoService catalogoService;
    private final TicketService ticketService;
    private final CartSession cartSession;

    public WebController(CatalogoService catalogoService, TicketService ticketService, CartSession cartSession) {
        this.catalogoService = catalogoService;
        this.ticketService = ticketService;
        this.cartSession = cartSession;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("categorias", catalogoService.getCategorias()); // si lo tienes
        model.addAttribute("productos", catalogoService.getProductos());   // si lo tienes
        model.addAttribute("carrito", cartSession.getCarrito());
        return "index";
    }

    @PostMapping("/agregar")
    public String agregarProducto(@RequestParam("nombre") String nombre) {
        Producto p = catalogoService.buscarProductoPorNombre(nombre); // <-- EXISTE en CatalogoService
        if (p != null) {
            cartSession.agregarProducto(p, 1);
        }
        return "redirect:/";
    }

    @PostMapping("/limpiar")
    public String limpiarCarrito() {
        cartSession.limpiar();
        return "redirect:/";
    }

    @GetMapping("/ticket")
    public String verTicket(Model model) {
        Carrito carrito = ticketService.getCarritoActual();
        double subtotal = (carrito != null) ? carrito.getSubtotal() : 0.0;
        double descuento = ticketService.getDescuentoAplicado();
        double iva = subtotal * 0.16;
        double total = subtotal - descuento + iva;

        model.addAttribute("carrito", carrito);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("descuento", descuento);
        model.addAttribute("iva", iva);
        model.addAttribute("total", total);
        return "ticket";
    }
}
