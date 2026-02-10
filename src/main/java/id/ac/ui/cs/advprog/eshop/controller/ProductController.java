package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public String homePage() {
        return "index";
    }

    @GetMapping("/product/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProduct";
    }

    @PostMapping("/product/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {
        service.create(product);
        return "redirect:/product/list";
    }

    @GetMapping("/product/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "productList";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductPage(@PathVariable("id") String productId, Model model) {
        return renderEditPage(productId, model);
    }

    @GetMapping("/product/edit")
    public String editProductPageWithParam(@RequestParam(name = "id", required = false) String productId,
                                           Model model) {
        return renderEditPage(productId, model);
    }
    

    @PostMapping("/product/edit")
    public String editProductPost(@ModelAttribute Product product) {
        service.update(product);
        return "redirect:/product/list";
    }


    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") String productId, Model model) {
        Product product = service.findById(productId);
        if (product == null) {
            return "redirect:/product/list";
        }
        service.delete(product);
        return "redirect:/product/list";
    }

    private String renderEditPage(String productId, Model model) {
        if (productId == null || productId.isBlank()) {
            return "redirect:/product/list";
        }
        Product product = service.findById(productId);
        if (product == null) {
            return "redirect:/product/list";
        }
        model.addAttribute("product", product);
        return "editProduct";
    }
}
