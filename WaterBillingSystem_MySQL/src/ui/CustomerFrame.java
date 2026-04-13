package ui;
import dao.InMemoryPaymentDAO;
import model.Payment;
import service.NotificationService;
import service.PaymentService;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;
public class CustomerFrame extends JFrame {
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    private final int customerId=1001;
    private final String customerName="John Mwangi",customerEmail="john.mwangi@email.com",customerPhone="+254712345678",meterNumber="MTR-2024-001",accountNumber="ACC-1001-NRB";
    private static final Color PRIMARY=new Color(0,102,179),SECONDARY=new Color(0,153,76),BG_WHITE=Color.WHITE,LIGHT_BG=new Color(245,247,250),TEXT_DARK=new Color(30,30,30);
    private JTextArea receiptTextArea,notificationLogArea;
    private DefaultTableModel paymentHistoryModel;
    public CustomerFrame() {
        notificationService=new NotificationService();
        paymentService=new PaymentService(new InMemoryPaymentDAO(),notificationService);
        setTitle("Nairobi Water Billing System - Customer Portal");
        setSize(900,650); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLocationRelativeTo(null);
        JPanel main=new JPanel(new BorderLayout());
        main.add(buildHeader(),BorderLayout.NORTH);
        main.add(buildTabs(),BorderLayout.CENTER);
        main.add(buildFooter(),BorderLayout.SOUTH);
        setContentPane(main);
    }
    private JPanel buildHeader(){
        JPanel h=new JPanel(new BorderLayout()); h.setBackground(PRIMARY); h.setBorder(new EmptyBorder(12,20,12,20));
        JLabel t=new JLabel("Nairobi Water Billing System"); t.setFont(new Font("Segoe UI",Font.BOLD,20)); t.setForeground(Color.WHITE);
        JLabel s=new JLabel("Customer Portal",SwingConstants.RIGHT); s.setFont(new Font("Segoe UI",Font.PLAIN,13)); s.setForeground(new Color(200,220,255));
        h.add(t,BorderLayout.WEST); h.add(s,BorderLayout.EAST); return h;
    }
    private JPanel buildFooter(){
        JPanel f=new JPanel(); f.setBackground(new Color(230,235,245));
        JLabel l=new JLabel("© 2024 Nairobi Water & Sewerage Company | Tel: 0800 720 906");
        l.setFont(new Font("Segoe UI",Font.PLAIN,11)); f.add(l); return f;
    }
    private JTabbedPane buildTabs(){
        JTabbedPane t=new JTabbedPane(); t.setFont(new Font("Segoe UI",Font.PLAIN,13));
        t.addTab("My Account",buildAccountTab()); t.addTab("My Bills",buildBillsTab());
        t.addTab("Make Payment",buildPaymentTab()); t.addTab("Payment History",buildHistoryTab());
        t.addTab("View Receipt",buildReceiptTab()); t.addTab("Notifications",buildNotificationsTab());
        return t;
    }
    private JPanel buildAccountTab(){
        JPanel p=new JPanel(new GridBagLayout()); p.setBackground(BG_WHITE); p.setBorder(new EmptyBorder(20,40,20,40));
        GridBagConstraints g=new GridBagConstraints(); g.insets=new Insets(8,8,8,8); g.anchor=GridBagConstraints.WEST;
        String[][] info={{"Full Name:",customerName},{"Customer ID:",String.valueOf(customerId)},{"Account No:",accountNumber},{"Meter No:",meterNumber},{"Email:",customerEmail},{"Phone:",customerPhone},{"Status:","Active"}};
        for(int i=0;i<info.length;i++){
            g.gridx=0;g.gridy=i; JLabel l=new JLabel(info[i][0]); l.setFont(new Font("Segoe UI",Font.BOLD,13)); p.add(l,g);
            g.gridx=1; JLabel v=new JLabel(info[i][1]); v.setFont(new Font("Segoe UI",Font.PLAIN,13)); p.add(v,g);
        }
        JPanel w=new JPanel(new FlowLayout(FlowLayout.CENTER)); w.setBackground(BG_WHITE); w.add(p); return w;
    }
    private JPanel buildBillsTab(){
        JPanel p=new JPanel(new BorderLayout(10,10)); p.setBackground(BG_WHITE); p.setBorder(new EmptyBorder(15,15,15,15));
        String[] cols={"Bill ID","Period","Usage (m3)","Amount (KES)","Due Date","Status"};
        Object[][] rows={{3001,"Jan 2025","18.5","2,775.00","28/02/2025","PAID"},{3002,"Feb 2025","21.0","3,150.00","31/03/2025","PAID"},{3003,"Mar 2025","19.2","2,880.00","30/04/2025","PENDING"}};
        DefaultTableModel m=new DefaultTableModel(rows,cols){public boolean isCellEditable(int r,int c){return false;}};
        JTable t=new JTable(m); styleTable(t);
        p.add(new JScrollPane(t),BorderLayout.CENTER);
        JLabel l=new JLabel("Outstanding: KES 2,880.00",SwingConstants.RIGHT); l.setFont(new Font("Segoe UI",Font.BOLD,13)); l.setForeground(new Color(180,0,0));
        p.add(l,BorderLayout.SOUTH); return p;
    }
    private JPanel buildPaymentTab(){
        JPanel p=new JPanel(new GridBagLayout()); p.setBackground(BG_WHITE); p.setBorder(new EmptyBorder(20,50,20,50));
        GridBagConstraints g=new GridBagConstraints(); g.insets=new Insets(10,10,10,10); g.fill=GridBagConstraints.HORIZONTAL;
        g.gridx=0;g.gridy=0; p.add(bold("Bill ID:"),g); g.gridx=1; JTextField bill=new JTextField("3003",18); p.add(bill,g);
        g.gridx=0;g.gridy=1; p.add(bold("Amount (KES):"),g); g.gridx=1; JTextField amt=new JTextField("2880.00",18); p.add(amt,g);
        g.gridx=0;g.gridy=2; p.add(bold("Payment Method:"),g); g.gridx=1;
        JComboBox<String> method=new JComboBox<>(new String[]{"MPESA","CASH","BANK_TRANSFER","CARD"}); p.add(method,g);
        g.gridx=0;g.gridy=3;g.gridwidth=2; JButton btn=mkBtn("Pay Now",PRIMARY); p.add(btn,g);
        g.gridy=4; JLabel status=new JLabel(" "); status.setFont(new Font("Segoe UI",Font.BOLD,13)); p.add(status,g);
        btn.addActionListener((ActionEvent e)->{
            try{
                Payment pay=paymentService.processPayment(Integer.parseInt(bill.getText().trim()),customerId,customerName,customerEmail,customerPhone,Double.parseDouble(amt.getText().trim()),Payment.PaymentMethod.valueOf((String)method.getSelectedItem()));
                status.setForeground(SECONDARY); status.setText("Payment successful! Receipt: "+pay.getReceiptNumber());
                receiptTextArea.setText(paymentService.generateReceiptText(pay)); refreshHistory(); refreshNotifLog();
                JOptionPane.showMessageDialog(this,"Payment received!\nReceipt: "+pay.getReceiptNumber(),"Success",JOptionPane.INFORMATION_MESSAGE);
            }catch(Exception ex){status.setForeground(Color.RED);status.setText("Error: "+ex.getMessage());}
        });
        return p;
    }
    private JPanel buildHistoryTab(){
        JPanel p=new JPanel(new BorderLayout(10,10)); p.setBackground(BG_WHITE); p.setBorder(new EmptyBorder(15,15,15,15));
        String[] cols={"Receipt No","Bill ID","Amount (KES)","Method","Status","Date"};
        paymentHistoryModel=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable t=new JTable(paymentHistoryModel); styleTable(t);
        JButton ref=mkBtn("Refresh",PRIMARY); ref.addActionListener(e->refreshHistory());
        JPanel s=new JPanel(new FlowLayout(FlowLayout.RIGHT)); s.setBackground(BG_WHITE); s.add(ref);
        p.add(new JScrollPane(t),BorderLayout.CENTER); p.add(s,BorderLayout.SOUTH); return p;
    }
    private void refreshHistory(){
        paymentHistoryModel.setRowCount(0);
        for(Payment pay:paymentService.getPaymentsByCustomer(customerId))
            paymentHistoryModel.addRow(new Object[]{pay.getReceiptNumber(),pay.getBillId(),String.format("%.2f",pay.getAmountPaid()),pay.getPaymentMethod(),pay.getStatus(),pay.getFormattedPaymentDate()});
    }
    private JPanel buildReceiptTab(){
        JPanel p=new JPanel(new BorderLayout(10,10)); p.setBackground(BG_WHITE); p.setBorder(new EmptyBorder(15,15,15,15));
        JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT)); top.setBackground(BG_WHITE);
        JTextField f=new JTextField(22); JButton btn=mkBtn("Find",PRIMARY);
        top.add(bold("Receipt No:")); top.add(f); top.add(btn);
        receiptTextArea=new JTextArea(18,50); receiptTextArea.setFont(new Font("Courier New",Font.PLAIN,12)); receiptTextArea.setEditable(false); receiptTextArea.setBackground(LIGHT_BG);
        receiptTextArea.setText("Enter receipt number above and click Find.");
        btn.addActionListener(e->{
            Optional<Payment> found=paymentService.findByReceiptNumber(f.getText().trim());
            receiptTextArea.setText(found.map(paymentService::generateReceiptText).orElse("No receipt found for: "+f.getText().trim()));
        });
        p.add(top,BorderLayout.NORTH); p.add(new JScrollPane(receiptTextArea),BorderLayout.CENTER); return p;
    }
    private JPanel buildNotificationsTab(){
        JPanel p=new JPanel(new BorderLayout(10,10)); p.setBackground(BG_WHITE); p.setBorder(new EmptyBorder(15,15,15,15));
        notificationLogArea=new JTextArea(18,60); notificationLogArea.setFont(new Font("Courier New",Font.PLAIN,12)); notificationLogArea.setEditable(false); notificationLogArea.setBackground(LIGHT_BG);
        notificationLogArea.setText("No notifications yet.");
        JButton leak=mkBtn("Simulate Leak Alert",new Color(200,80,0)); JButton remind=mkBtn("Simulate Reminder",PRIMARY); JButton ref=mkBtn("Refresh",SECONDARY);
        leak.addActionListener(e->{notificationService.sendLeakAlert(customerName,customerEmail,customerPhone,meterNumber,98.5,"staff@nairobiwater.go.ke");refreshNotifLog();JOptionPane.showMessageDialog(this,"Leak alert simulated!","Alert",JOptionPane.WARNING_MESSAGE);});
        remind.addActionListener(e->{notificationService.sendPaymentReminder(customerName,customerEmail,customerPhone,3003,2880.00,"30/04/2025");refreshNotifLog();});
        ref.addActionListener(e->refreshNotifLog());
        JPanel btns=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); btns.setBackground(BG_WHITE); btns.add(leak); btns.add(remind); btns.add(ref);
        p.add(btns,BorderLayout.NORTH); p.add(new JScrollPane(notificationLogArea),BorderLayout.CENTER); return p;
    }
    private void refreshNotifLog(){
        StringBuilder sb=new StringBuilder();
        List<NotificationService.NotificationLog> list=notificationService.getLogs();
        if(list.isEmpty()) sb.append("No notifications yet.");
        else list.forEach(l->sb.append(l).append("\n"));
        notificationLogArea.setText(sb.toString());
    }
    private void styleTable(JTable t){
        t.setFont(new Font("Segoe UI",Font.PLAIN,12)); t.setRowHeight(26);
        t.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,12));
        t.getTableHeader().setBackground(PRIMARY); t.getTableHeader().setForeground(Color.WHITE);
    }
    private JButton mkBtn(String txt,Color bg){
        JButton b=new JButton(txt); b.setFont(new Font("Segoe UI",Font.BOLD,13)); b.setBackground(bg);
        b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setBorder(BorderFactory.createEmptyBorder(8,18,8,18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); b.setOpaque(true); return b;
    }
    private JLabel bold(String t){JLabel l=new JLabel(t);l.setFont(new Font("Segoe UI",Font.BOLD,13));return l;}
    public static void main(String[] args){SwingUtilities.invokeLater(()->{try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception e){}new CustomerFrame().setVisible(true);});}
}
