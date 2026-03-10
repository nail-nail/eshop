package service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderService orderService;

    Order order;
    Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order = new Order("order-001", products, 1708560000L, "Safira Sudrajat");
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testAddPayment() {
        Payment mockPayment = new Payment("pay-001", "VOUCHER_CODE", paymentData);
        doReturn(mockPayment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);
        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "SUCCESS");
        assertEquals("SUCCESS", result.getStatus());
        verify(orderService, times(1)).updateStatus(anyString(), eq("SUCCESS"));
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "REJECTED");
        assertEquals("REJECTED", result.getStatus());
        verify(orderService, times(1)).updateStatus(anyString(), eq("FAILED"));
    }

    @Test
    void testGetPayment() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);
        doReturn(payment).when(paymentRepository).getPayment("pay-001");

        Payment result = paymentService.getPayment("pay-001");
        assertEquals("pay-001", result.getId());
    }

    @Test
    void testGetAllPayments() {
        Payment payment1 = new Payment("pay-001", "VOUCHER_CODE", paymentData);
        Map<String, String> data2 = new HashMap<>();
        data2.put("bankName", "BCA");
        data2.put("referenceCode", "REF123");
        Payment payment2 = new Payment("pay-002", "BANK_TRANSFER", data2);
        doReturn(List.of(payment1, payment2)).when(paymentRepository).getAllPayments();

        List<Payment> results = paymentService.getAllPayments();
        assertEquals(2, results.size());
    }
}