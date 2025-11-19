package com.tiendaweb.web;

import com.tiendaweb.session.CartSession;
import com.tiendaweb.tienda.service.CatalogoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final CatalogoService catalogo;
    private final CartSession cartSession;

    public WebController(CatalogoService catalogo, CartSession cartSession) {
        this.catalogo = catalogo;
        this.cartSession = cartSession;
    }

    @GetMapping("/")
    public String index(@RequestParam(value="cat", required=false) String cat, Model model){
        model.addAttribute("categorias", catalogo.categorias());
        model.addAttribute("cat", cat);
        model.addAttribute("productos", cat == null ? catalogo.todos() : catalogo.productosDe(cat));
        model.addAttribute("carrito", cartSession.get());
        return "index";
    }

    @PostMapping("/cart/add/{codigo}")
    public String add(@PathVariable String codigo){
        catalogo.findByCodigo(codigo).ifPresent(p -> cartSession.get().add(p, 1));
        return "redirect:/";
    }

    @PostMapping("/cart/remove/{codigo}")
    public String remove(@PathVariable String codigo){
        cartSession.get().remove(codigo);
        return "redirect:/";
    }
}
