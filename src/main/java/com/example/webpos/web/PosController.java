package com.example.webpos.web;

import com.example.webpos.biz.PosService;
import com.example.webpos.model.Cart;
import com.example.webpos.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class PosController {
    private HttpSession session;

    private PosService posService;

    @Autowired
    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Cart getCart() {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @Autowired
    public void setPosService(PosService posService) {
        this.posService = posService;
    }

    @GetMapping("/")
    public String pos(Model model) {
        Cart cart = getCart();
        if (posService.products() != null && cart.getItems().isEmpty()) {
            posService.add(cart, posService.randomProduct(), 1);
        }
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", cart);
        return "index";
    }

    @GetMapping("/add")
    public String addByGet(@RequestParam(name = "pid") String pid, Model model) {
        Cart cart = getCart();
        posService.add(cart, pid, 1);
        session.setAttribute("cart", cart);
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", cart);
        return "index";
    }
}
