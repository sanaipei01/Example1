package service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class NotificationService {
    public enum NotificationType { EMAIL, SMS, LEAK_ALERT, PAYMENT_REMINDER, OVERDUE_NOTICE }
    private final List<NotificationLog> logs=new ArrayList<>();
    public void sendEmail(String to, String subject, String body) {
        if (to==null||to.isBlank()) return;
        System.out.println("\n[EMAIL] To: "+to+" | Subject: "+subject+"\n"+body);
        logs.add(new NotificationLog(NotificationType.EMAIL,to,subject,true,LocalDateTime.now()));
    }
    public void sendSMS(String to, String message) {
        if (to==null||to.isBlank()) return;
        System.out.println("\n[SMS] To: "+to+" | "+message);
        logs.add(new NotificationLog(NotificationType.SMS,to,message,true,LocalDateTime.now()));
    }
    public void sendLeakAlert(String name,String email,String phone,String meter,double usage,String staffEmail) {
        sendEmail(email,"Water Leak Alert - "+meter,"Dear "+name+", high usage of "+String.format("%.2f",usage)+" m3 detected on meter "+meter+". Please check for leaks.");
        sendSMS(phone,"ALERT: High usage on meter "+meter+" ("+String.format("%.2f",usage)+" m3). Check for leaks. Call 0800720906.");
        if (staffEmail!=null&&!staffEmail.isBlank()) sendEmail(staffEmail,"FIELD INSPECTION: "+meter,"Leak alert for customer "+name+". Meter: "+meter+". Usage: "+String.format("%.2f",usage)+" m3.");
        logs.add(new NotificationLog(NotificationType.LEAK_ALERT,email,"Leak alert meter "+meter,true,LocalDateTime.now()));
    }
    public void sendPaymentReminder(String name,String email,String phone,int billId,double amount,String due) {
        sendEmail(email,"Payment Reminder - Bill #"+billId,"Dear "+name+", bill #"+billId+" of KES "+String.format("%.2f",amount)+" is due on "+due+".");
        sendSMS(phone,"Reminder: Bill #"+billId+" KES "+String.format("%.2f",amount)+" due "+due+". Pay via MPesa or visit our offices.");
        logs.add(new NotificationLog(NotificationType.PAYMENT_REMINDER,email,"Reminder bill #"+billId,true,LocalDateTime.now()));
    }
    public void sendOverdueNotice(String name,String email,String phone,int billId,double amount) {
        sendEmail(email,"OVERDUE - Bill #"+billId,"Dear "+name+", bill #"+billId+" of KES "+String.format("%.2f",amount)+" is OVERDUE. Pay immediately to avoid disconnection.");
        sendSMS(phone,"OVERDUE: Bill #"+billId+" KES "+String.format("%.2f",amount)+" overdue. Pay now. Call 0800720906.");
        logs.add(new NotificationLog(NotificationType.OVERDUE_NOTICE,email,"Overdue bill #"+billId,true,LocalDateTime.now()));
    }
    public List<NotificationLog> getLogs() { return Collections.unmodifiableList(logs); }
    public static class NotificationLog {
        private final NotificationType type; private final String recipient,message; private final boolean success; private final LocalDateTime sentAt;
        public NotificationLog(NotificationType t,String r,String m,boolean s,LocalDateTime d){type=t;recipient=r;message=m;success=s;sentAt=d;}
        public NotificationType getType(){return type;} public String getRecipient(){return recipient;}
        public String getMessage(){return message;} public boolean isSuccess(){return success;} public LocalDateTime getSentAt(){return sentAt;}
        @Override public String toString(){return String.format("[%s] %s -> %s | OK: %b",sentAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),type,recipient,success);}
    }
}
