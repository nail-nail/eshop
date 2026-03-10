package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testSavePayment() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);
        Payment result = paymentRepository.save(payment);
        assertEquals("pay-001", result.getId());
    }

    @Test
    void testGetPaymentFound() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);
        paymentRepository.save(payment);
        Payment result = paymentRepository.getPayment("pay-001");
        assertEquals("pay-001", result.getId());
    }

    @Test
    void testGetPaymentNotFound() {
        Payment result = paymentRepository.getPayment("invalid-id");
        assertNull(result);
    }

    @Test
    void testGetAllPayments() {
        Payment payment1 = new Payment("pay-001", "VOUCHER_CODE", paymentData);
        Map<String, String> data2 = new HashMap<>();
        data2.put("bankName", "BCA");
        data2.put("referenceCode", "REF123");
        Payment payment2 = new Payment("pay-002", "BANK_TRANSFER", data2);
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        List<Payment> results = paymentRepository.getAllPayments();
        assertEquals(2, results.size());
    }
}