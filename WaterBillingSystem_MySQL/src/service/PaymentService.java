package service;
import dao.PaymentDAO;
import model.Payment;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
public class PaymentService {
    private final PaymentDAO paymentDAO;
    private final NotificationService notificationService;
    public PaymentService(PaymentDAO paymentDAO, NotificationService notificationService) {
        this.paymentDAO=paymentDAO; this.notificationService=notificationService;
    }
    public Payment processPayment(int billId, int customerId, String customerName,
                                  String customerEmail, String customerPhone,
                                  double amount, Payment.PaymentMethod method) {
        if (amount<=0) throw new IllegalArgumentException("Payment amount must be greater than zero.");
        Payment payment = new Payment(billId,customerId,customerName,amount,method);
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        Payment saved = paymentDAO.save(payment);
        notificationService.sendEmail(customerEmail,"Payment Confirmation - "+saved.getReceiptNumber(),generateReceiptText(saved));
        notificationService.sendSMS(customerPhone,"Dear "+customerName+", payment of KES "+String.format("%.2f",amount)+" received. Receipt: "+saved.getReceiptNumber());
        return saved;
    }
    public String generateReceiptText(Payment p) {
        String line="=".repeat(50);
        DateTimeFormatter fmt=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return line+"\n       NAIROBI WATER BILLING SYSTEM\n            PAYMENT RECEIPT\n"+line+"\n"+
            String.format("Receipt No    : %s%n",p.getReceiptNumber())+
            String.format("Transaction   : %s%n",p.getTransactionReference())+
            "-".repeat(50)+"\n"+
            String.format("Customer Name : %s%n",p.getCustomerName())+
            String.format("Customer ID   : %d%n",p.getCustomerId())+
            String.format("Bill ID       : %d%n",p.getBillId())+
            "-".repeat(50)+"\n"+
            String.format("Amount Paid   : KES %.2f%n",p.getAmountPaid())+
            String.format("Payment Method: %s%n",p.getPaymentMethod())+
            String.format("Status        : %s%n",p.getStatus())+
            String.format("Date & Time   : %s%n",p.getPaymentDate()!=null?p.getPaymentDate().format(fmt):"N/A")+
            line+"\n  Thank you for your payment!\n"+line;
    }
    public List<Payment> getPaymentsByCustomer(int customerId) { return paymentDAO.findByCustomerId(customerId); }
    public List<Payment> getPaymentsByBill(int billId) { return paymentDAO.findByBillId(billId); }
    public Optional<Payment> findByReceiptNumber(String r) { return paymentDAO.findByReceiptNumber(r); }
    public List<Payment> getAllPayments() { return paymentDAO.findAll(); }
    public double getTotalPaid(int billId) { return paymentDAO.getTotalPaidForBill(billId); }
    public double getOutstandingBalance(int billId, double totalAmount) { return Math.max(0,totalAmount-getTotalPaid(billId)); }
    public boolean issueRefund(int paymentId, String email, String phone) {
        Optional<Payment> opt=paymentDAO.findById(paymentId);
        if (opt.isEmpty()) return false;
        Payment p=opt.get();
        if (p.getStatus()!=Payment.PaymentStatus.COMPLETED) return false;
        p.setStatus(Payment.PaymentStatus.REFUNDED);
        boolean ok=paymentDAO.update(p);
        if (ok) {
            notificationService.sendEmail(email,"Refund - "+p.getReceiptNumber(),"Refund of KES "+String.format("%.2f",p.getAmountPaid())+" processed.");
            notificationService.sendSMS(phone,"Refund of KES "+String.format("%.2f",p.getAmountPaid())+" processed for receipt "+p.getReceiptNumber());
        }
        return ok;
    }
}
