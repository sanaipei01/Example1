package model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Payment {
    public enum PaymentMethod { CASH, MPESA, BANK_TRANSFER, CARD }
    public enum PaymentStatus { PENDING, COMPLETED, FAILED, REFUNDED }
    private int paymentId, billId, customerId;
    private String customerName, transactionReference, receiptNumber, notes;
    private double amountPaid;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    public Payment() { this.paymentDate = LocalDateTime.now(); this.status = PaymentStatus.PENDING; }
    public Payment(int billId, int customerId, String customerName, double amountPaid, PaymentMethod paymentMethod) {
        this();
        this.billId = billId; this.customerId = customerId; this.customerName = customerName;
        this.amountPaid = amountPaid; this.paymentMethod = paymentMethod;
        this.transactionReference = "TXN-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + (int)(Math.random()*9000+1000);
        this.receiptNumber = "RCP-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + (int)(Math.random()*9000+1000);
    }
    public Payment(int paymentId, int billId, int customerId, String customerName, double amountPaid,
                   PaymentMethod paymentMethod, PaymentStatus status, String transactionReference,
                   LocalDateTime paymentDate, String receiptNumber) {
        this.paymentId=paymentId; this.billId=billId; this.customerId=customerId;
        this.customerName=customerName; this.amountPaid=amountPaid; this.paymentMethod=paymentMethod;
        this.status=status; this.transactionReference=transactionReference;
        this.paymentDate=paymentDate; this.receiptNumber=receiptNumber;
    }
    public String getFormattedPaymentDate() {
        if (paymentDate == null) return "N/A";
        return paymentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    public int getPaymentId() { return paymentId; } public void setPaymentId(int v) { paymentId=v; }
    public int getBillId() { return billId; } public void setBillId(int v) { billId=v; }
    public int getCustomerId() { return customerId; } public void setCustomerId(int v) { customerId=v; }
    public String getCustomerName() { return customerName; } public void setCustomerName(String v) { customerName=v; }
    public double getAmountPaid() { return amountPaid; } public void setAmountPaid(double v) { amountPaid=v; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(PaymentMethod v) { paymentMethod=v; }
    public PaymentStatus getStatus() { return status; } public void setStatus(PaymentStatus v) { status=v; }
    public String getTransactionReference() { return transactionReference; } public void setTransactionReference(String v) { transactionReference=v; }
    public LocalDateTime getPaymentDate() { return paymentDate; } public void setPaymentDate(LocalDateTime v) { paymentDate=v; }
    public String getReceiptNumber() { return receiptNumber; } public void setReceiptNumber(String v) { receiptNumber=v; }
    public String getNotes() { return notes; } public void setNotes(String v) { notes=v; }
    @Override public String toString() {
        return String.format("Payment{id=%d, billId=%d, customer='%s', amount=%.2f, method=%s, status=%s}",
                paymentId, billId, customerName, amountPaid, paymentMethod, status);
    }
}
