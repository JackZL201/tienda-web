package com.tiendaweb.web;

import com.tiendaweb.session.CartSession;
import com.tiendaweb.tienda.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TicketController {

    private final TicketService ticketService;
    private final CartSession cartSession;

    public TicketController(TicketService ticketService, CartSession cartSession) {
        this.ticketService = ticketService;
        this.cartSession = cartSession;
    }

    @GetMapping("/ticket")
    public String ticket(Model model){
        var data = ticketService.calcular(cartSession.get(), 16.0, 10.0);
        model.addAllAttributes(data);
        return "ticket";
    }
}
