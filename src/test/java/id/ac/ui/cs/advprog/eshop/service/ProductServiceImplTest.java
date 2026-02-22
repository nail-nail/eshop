package id.ac.ui.cs.advprog.eshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product baseProduct;

    @BeforeEach
    void setUp() {
        baseProduct = new Product();
        baseProduct.setProductId("123");
        baseProduct.setProductName("Laptop");
        baseProduct.setProductQuantity(2);
    }

    @Test
    void create_shouldDelegateToRepository() {
        when(productRepository.create(baseProduct)).thenReturn(baseProduct);

        Product created = productService.create(baseProduct);

        assertEquals("123", created.getProductId());
        verify(productRepository, times(1)).create(baseProduct);
    }

    @Test
    void findAll_shouldTransformIteratorIntoList() {
        Product second = new Product();
        second.setProductId("456");
        second.setProductName("Mouse");
        second.setProductQuantity(5);

        Iterator<Product> iterator = List.of(baseProduct, second).iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> products = productService.findAll();

        assertEquals(2, products.size());
        assertEquals("Mouse", products.get(1).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnRepositoryResult() {
        when(productRepository.findById("123")).thenReturn(baseProduct);

        Product found = productService.findById("123");

        assertEquals(baseProduct, found);
        verify(productRepository, times(1)).findById("123");
    }

    @Test
    void findById_whenMissingProduct_shouldReturnNull() {
        when(productRepository.findById("missing")).thenReturn(null);

        Product found = productService.findById("missing");

        assertNull(found);
    }

    @Test
    void update_shouldReturnUpdatedProduct() {
        Product updated = new Product();
        updated.setProductId("123");
        updated.setProductName("Laptop V2");
        updated.setProductQuantity(7);

        when(productRepository.update(updated)).thenReturn(updated);

        Product result = productService.update(updated);

        assertEquals("Laptop V2", result.getProductName());
        verify(productRepository, times(1)).update(updated);
    }

    @Test
    void delete_shouldDelegateToRepository() {
        productService.delete(baseProduct);

        verify(productRepository, times(1)).delete(baseProduct);
    }

    @Test
    void update_whenRepositoryReturnsNull_shouldPropagate() {
        when(productRepository.update(any(Product.class))).thenReturn(null);

        Product result = productService.update(baseProduct);

        assertNull(result);
    }
}
