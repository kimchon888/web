    package com.example.coffeeshop.controllers;

    import com.example.coffeeshop.models.Product;
    import com.example.coffeeshop.services.ProductsRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import java.util.List;

    @Controller
    @RequestMapping("/products") // Prefix chung cho tất cả sản phẩm
    public class ProductsController {

        @Autowired
        private ProductsRepository repo;

        @GetMapping("")
        public String homeProductList(Model model) {
            List<Product> products = repo.findAll();
            model.addAttribute("products", products);
            return "products/index";
        }

        @GetMapping("/sanphamnoibat")
        public String sanphamnoibat(Model model) {
            List<Product> products = repo.findAll();
            model.addAttribute("products", products);
            return "products/sanphamnoibat";
        }

        @GetMapping("/doankem")
        public String doankem(Model model) {
            List<Product> products = repo.findAll();
            model.addAttribute("products", products);
            return "products/doankem"; // file lowercase
        }

        @GetMapping("/showcart")
        public String showcart(Model model) {
            List<Product> products = repo.findAll();
            model.addAttribute("products", products);
            return "products/showcart";
        }

        @GetMapping("/xemthem/{id}")
        public String showProductDetails(@PathVariable("id") Long productId, Model model) {
            Product product = repo.findById(productId).orElse(null);
            model.addAttribute("product", product);
            return "products/xemthem";
        }

        @GetMapping("/admin")
        public String adminPage(Model model) {
            List<Product> products = repo.findByIsHiddenFalse();
            model.addAttribute("products", products);
            return "products/admin";
        }
    }
