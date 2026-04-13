package dao;
import model.Payment;
import java.util.List;
import java.util.Optional;
public interface PaymentDAO {
    Payment save(Payment payment);
    Optional<Payment> findById(int paymentId);
    List<Payment> findByBillId(int billId);
    List<Payment> findByCustomerId(int customerId);
    List<Payment> findAll();
    Optional<Payment> findByReceiptNumber(String receiptNumber);
    Optional<Payment> findByTransactionReference(String transactionReference);
    boolean update(Payment payment);
    boolean delete(int paymentId);
    double getTotalPaidForBill(int billId);
}
