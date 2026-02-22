package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testCreateAndEdit() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);


        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Bukan Sampo Cap Bambang");
        updatedProduct.setProductQuantity(99);
        productRepository.update(updatedProduct);
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(updatedProduct.getProductName(), savedProduct.getProductName());
        assertEquals(updatedProduct.getProductQuantity(), savedProduct.getProductQuantity());
        assertNotEquals("Sampo Cap Bambang",updatedProduct.getProductName());
        assertNotEquals(100, updatedProduct.getProductQuantity());

    }

    @Test
    void testCreateGeneratesIdWhenMissing() {
        Product product = new Product();
        product.setProductName("AutoId Product");
        product.setProductQuantity(10);

        productRepository.create(product);

        assertNotNull(product.getProductId());
        assertFalse(product.getProductId().isEmpty());
    }

    @Test
    void testCreateGeneratesIdWhenEmptyString() {
        Product product = new Product();
        product.setProductId("");
        product.setProductName("AutoId Product");
        product.setProductQuantity(10);

        productRepository.create(product);

        assertNotNull(product.getProductId());
        assertFalse(product.getProductId().isEmpty());
    }

    @Test
    void testEditNonExistingProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("does-not-exist");
        updatedProduct.setProductName("Not Exist");
        updatedProduct.setProductQuantity(42);

        Product result = productRepository.update(updatedProduct);
        assertNull(result);
    }

    @Test
    void testUpdateNullProductReturnsNull() {
        Product result = productRepository.update(null);
        assertNull(result);
    }

    @Test
    void testUpdateProductWithoutIdReturnsNull() {
        Product product = new Product();
        product.setProductName("No Id");
        product.setProductQuantity(5);

        Product result = productRepository.update(product);
        assertNull(result);
    }

    @Test
    void testCreateAndDelete() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        productRepository.delete(product);
        assertFalse(productIterator.hasNext());


    }

    @Test
    void testDeleteNonExistingProduct() {
        Product ghostProduct = new Product();
        ghostProduct.setProductId("does-not-exist");
        ghostProduct.setProductName("Not Exist");
        ghostProduct.setProductQuantity(0);

        productRepository.delete(ghostProduct);
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteNullProductDoesNothing() {
        Product product = new Product();
        product.setProductId("some-id");
        productRepository.create(product);

        productRepository.delete(null);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
    }

    @Test
    void testDeleteProductWithoutIdDoesNothing() {
        Product product = new Product();
        product.setProductId("some-id");
        productRepository.create(product);

        Product noId = new Product();
        productRepository.delete(noId);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
    }

    @Test
    void testFindByIdNullReturnsNull() {
        assertNull(productRepository.findById(null));
    }
}
