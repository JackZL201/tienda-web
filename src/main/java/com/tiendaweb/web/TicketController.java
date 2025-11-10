package com.tiendaweb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import tienda.models.Carrito;
import tienda.models.Descuento;
import tienda.service.TicketService;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Controller
public class TicketController {

    @Autowired private TicketService ticketService;
    @Autowired private org.thymeleaf.TemplateEngine templateEngine;

    // Ver el ticket renderizado en HTML
    @GetMapping("/ticket")
    public String verTicket(@RequestParam String nombre,
                            @RequestParam String email,
                            Model model) {
        Carrito carrito = ticketService.getCarritoActual();          // usa tu flujo real
        Descuento desc   = ticketService.getDescuentoAplicado();     // idem
        TicketService.Totales tot = ticketService.calcularTotales(carrito, desc);

        model.addAttribute("nombre", nombre);
        model.addAttribute("email", email);
        model.addAttribute("carrito", carrito);
        model.addAttribute("desc", desc);
        model.addAttribute("tot", tot);
        return "ticket"; // templates/ticket.html
    }

    // Descargar el ticket como archivo HTML
    @GetMapping("/ticket/download/html")
    public ResponseEntity<byte[]> descargarHtml(@RequestParam String nombre,
                                                @RequestParam String email) {
        Carrito carrito = ticketService.getCarritoActual();
        Descuento desc   = ticketService.getDescuentoAplicado();
        TicketService.Totales tot = ticketService.calcularTotales(carrito, desc);

        var ctx = new org.thymeleaf.context.Context(Locale.getDefault());
        ctx.setVariable("nombre", nombre);
        ctx.setVariable("email", email);
        ctx.setVariable("carrito", carrito);
        ctx.setVariable("desc", desc);
        ctx.setVariable("tot", tot);

        String html = templateEngine.process("ticket", ctx);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ticket_" + nombre.replaceAll("\\s+","_") + ".html")
                .contentType(MediaType.TEXT_HTML)
                .body(html.getBytes(StandardCharsets.UTF_8));
    }
}
