package id.ac.ui.cs.advprog.eshop.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void homePage_shouldRenderIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void createProductPage_shouldProvideEmptyModel() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void createProductPost_shouldPersistAndRedirect() throws Exception {
        mockMvc.perform(post("/product/create")
                .param("productName", "Keyboard")
                .param("productQuantity", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService, times(1)).create(any(Product.class));
    }

    @Test
    void productListPage_shouldRenderProducts() throws Exception {
        Product first = new Product();
        first.setProductId("1");
        first.setProductName("Laptop");
        first.setProductQuantity(2);

        Product second = new Product();
        second.setProductId("2");
        second.setProductName("Mouse");
        second.setProductQuantity(5);

        when(productService.findAll()).thenReturn(List.of(first, second));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attribute("products", hasSize(2)))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void editProductPage_withValidPath_shouldRenderEditView() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Laptop");
        product.setProductQuantity(2);

        when(productService.findById("1")).thenReturn(product);

        mockMvc.perform(get("/product/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attribute("product", is(product)));
    }

    @Test
    void editProductPage_missingProduct_shouldRedirect() throws Exception {
        when(productService.findById("missing")).thenReturn(null);

        mockMvc.perform(get("/product/edit/missing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    void editProductPage_withParamWithoutId_shouldRedirect() throws Exception {
        mockMvc.perform(get("/product/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    void editProductPost_shouldUpdateAndRedirect() throws Exception {
        mockMvc.perform(post("/product/edit")
                .param("productId", "1")
                .param("productName", "Laptop")
                .param("productQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService, times(1)).update(any(Product.class));
    }

    @Test
    void deleteProduct_whenProductExists_shouldRemoveAndRedirect() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        when(productService.findById("1")).thenReturn(product);

        mockMvc.perform(post("/product/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService, times(1)).delete(product);
    }

    @Test
    void deleteProduct_whenProductMissing_shouldJustRedirect() throws Exception {
        when(productService.findById("missing")).thenReturn(null);

        mockMvc.perform(post("/product/delete/missing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService, times(0)).delete(any(Product.class));
    }
}
