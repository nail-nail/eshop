package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.status = determineStatus(method, paymentData);
    }

    private String determineStatus(String method, Map<String, String> paymentData) {
        if ("VOUCHER_CODE".equals(method)) {
            return validateVoucherCode(paymentData.get("voucherCode"))
                    ? "SUCCESS" : "REJECTED";
        } else if ("BANK_TRANSFER".equals(method)) {
            return validateBankTransfer(paymentData) ? "SUCCESS" : "REJECTED";
        }
        return "REJECTED";
    }

    private boolean validateVoucherCode(String voucherCode) {
        if (voucherCode == null) return false;
        if (voucherCode.length() != 16) return false;
        if (!voucherCode.startsWith("ESHOP")) return false;
        long digitCount = voucherCode.chars().filter(Character::isDigit).count();
        return digitCount == 8;
    }

    private boolean validateBankTransfer(Map<String, String> data) {
        String bankName = data.get("bankName");
        String referenceCode = data.get("referenceCode");
        if (bankName == null || bankName.isEmpty()) return false;
        if (referenceCode == null || referenceCode.isEmpty()) return false;
        return true;
    }

    public void setStatus(String status) {
        String[] validStatuses = {"SUCCESS", "REJECTED"};
        if (Arrays.stream(validStatuses).noneMatch(s -> s.equals(status))) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }
}