package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PaymentRepository {

    private Map<String, Payment> paymentData = new HashMap<>();

    public Payment save(Payment payment) {
        paymentData.put(payment.getId(), payment);
        return payment;
    }

    public Payment getPayment(String id) {
        return paymentData.getOrDefault(id, null);
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(paymentData.values());
}
}