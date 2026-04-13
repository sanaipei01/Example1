package dao;
import model.Payment;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
public class InMemoryPaymentDAO implements PaymentDAO {
    private final Map<Integer, Payment> store = new LinkedHashMap<>();
    private final AtomicInteger idGen = new AtomicInteger(1);
    @Override public Payment save(Payment p) {
        p.setPaymentId(idGen.getAndIncrement());
        p.setStatus(Payment.PaymentStatus.COMPLETED);
        store.put(p.getPaymentId(), p);
        return p;
    }
    @Override public Optional<Payment> findById(int id) { return Optional.ofNullable(store.get(id)); }
    @Override public List<Payment> findByBillId(int billId) {
        return store.values().stream().filter(p -> p.getBillId()==billId).collect(Collectors.toList());
    }
    @Override public List<Payment> findByCustomerId(int customerId) {
        return store.values().stream().filter(p -> p.getCustomerId()==customerId).collect(Collectors.toList());
    }
    @Override public List<Payment> findAll() { return new ArrayList<>(store.values()); }
    @Override public Optional<Payment> findByReceiptNumber(String r) {
        return store.values().stream().filter(p -> r.equals(p.getReceiptNumber())).findFirst();
    }
    @Override public Optional<Payment> findByTransactionReference(String t) {
        return store.values().stream().filter(p -> t.equals(p.getTransactionReference())).findFirst();
    }
    @Override public boolean update(Payment p) {
        if (!store.containsKey(p.getPaymentId())) return false;
        store.put(p.getPaymentId(), p); return true;
    }
    @Override public boolean delete(int id) { return store.remove(id) != null; }
    @Override public double getTotalPaidForBill(int billId) {
        return store.values().stream()
            .filter(p -> p.getBillId()==billId && p.getStatus()==Payment.PaymentStatus.COMPLETED)
            .mapToDouble(Payment::getAmountPaid).sum();
    }
}
