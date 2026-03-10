package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepository {
    private List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment) {
        int i = 0;
        for (Payment saved : paymentData) {
            if (saved.getId().equals(payment.getId())) {
                paymentData.set(i, payment);
                return payment;
            }
            i++;
        }
        paymentData.add(payment);
        return payment;
    }

    public Payment getPayment(String id) {
        for (Payment saved : paymentData) {
            if (saved.getId().equals(id)) {
                return saved;
            }
        }
        return null;
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(paymentData);
    }
}