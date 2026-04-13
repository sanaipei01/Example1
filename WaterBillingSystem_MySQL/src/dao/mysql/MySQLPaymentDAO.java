package dao.mysql;
import dao.PaymentDAO;
import model.Payment;
import util.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
public class MySQLPaymentDAO implements PaymentDAO {
    private static final String INSERT = "INSERT INTO payments (bill_id,customer_id,customer_name,amount_paid,payment_method,status,transaction_ref,receipt_number,payment_date,notes) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private static final String SELECT_ID = "SELECT * FROM payments WHERE payment_id=?";
    private static final String SELECT_BILL = "SELECT * FROM payments WHERE bill_id=? ORDER BY payment_date DESC";
    private static final String SELECT_CUSTOMER = "SELECT * FROM payments WHERE customer_id=? ORDER BY payment_date DESC";
    private static final String SELECT_ALL = "SELECT * FROM payments ORDER BY payment_date DESC";
    private static final String SELECT_RECEIPT = "SELECT * FROM payments WHERE receipt_number=?";
    private static final String SELECT_TXN = "SELECT * FROM payments WHERE transaction_ref=?";
    private static final String UPDATE = "UPDATE payments SET bill_id=?,customer_id=?,customer_name=?,amount_paid=?,payment_method=?,status=?,transaction_ref=?,receipt_number=?,notes=? WHERE payment_id=?";
    private static final String DELETE = "DELETE FROM payments WHERE payment_id=?";
    private static final String TOTAL = "SELECT COALESCE(SUM(amount_paid),0) FROM payments WHERE bill_id=? AND status='COMPLETED'";
    @Override public Payment save(Payment p) {
        try (Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,p.getBillId()); ps.setInt(2,p.getCustomerId()); ps.setString(3,p.getCustomerName());
            ps.setDouble(4,p.getAmountPaid()); ps.setString(5,p.getPaymentMethod().name());
            ps.setString(6,p.getStatus().name()); ps.setString(7,p.getTransactionReference());
            ps.setString(8,p.getReceiptNumber()); ps.setTimestamp(9,Timestamp.valueOf(p.getPaymentDate()));
            ps.setString(10,p.getNotes()); ps.executeUpdate();
            try(ResultSet rs=ps.getGeneratedKeys()){if(rs.next())p.setPaymentId(rs.getInt(1));}
        } catch(SQLException e){e.printStackTrace();}
        return p;
    }
    @Override public Optional<Payment> findById(int id) {
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(SELECT_ID)){
            ps.setInt(1,id); try(ResultSet rs=ps.executeQuery()){if(rs.next())return Optional.of(map(rs));}
        }catch(SQLException e){e.printStackTrace();} return Optional.empty();
    }
    @Override public List<Payment> findByBillId(int id) { return query(SELECT_BILL,id); }
    @Override public List<Payment> findByCustomerId(int id) { return query(SELECT_CUSTOMER,id); }
    @Override public List<Payment> findAll() {
        List<Payment> list=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){
            while(rs.next())list.add(map(rs));
        }catch(SQLException e){e.printStackTrace();} return list;
    }
    @Override public Optional<Payment> findByReceiptNumber(String r) { return queryOne(SELECT_RECEIPT,r); }
    @Override public Optional<Payment> findByTransactionReference(String t) { return queryOne(SELECT_TXN,t); }
    @Override public boolean update(Payment p) {
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(UPDATE)){
            ps.setInt(1,p.getBillId()); ps.setInt(2,p.getCustomerId()); ps.setString(3,p.getCustomerName());
            ps.setDouble(4,p.getAmountPaid()); ps.setString(5,p.getPaymentMethod().name());
            ps.setString(6,p.getStatus().name()); ps.setString(7,p.getTransactionReference());
            ps.setString(8,p.getReceiptNumber()); ps.setString(9,p.getNotes()); ps.setInt(10,p.getPaymentId());
            return ps.executeUpdate()>0;
        }catch(SQLException e){e.printStackTrace();} return false;
    }
    @Override public boolean delete(int id) {
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(DELETE)){
            ps.setInt(1,id); return ps.executeUpdate()>0;
        }catch(SQLException e){e.printStackTrace();} return false;
    }
    @Override public double getTotalPaidForBill(int id) {
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(TOTAL)){
            ps.setInt(1,id); try(ResultSet rs=ps.executeQuery()){if(rs.next())return rs.getDouble(1);}
        }catch(SQLException e){e.printStackTrace();} return 0;
    }
    private List<Payment> query(String sql,int id){
        List<Payment> list=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,id); try(ResultSet rs=ps.executeQuery()){while(rs.next())list.add(map(rs));}
        }catch(SQLException e){e.printStackTrace();} return list;
    }
    private Optional<Payment> queryOne(String sql,String val){
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,val); try(ResultSet rs=ps.executeQuery()){if(rs.next())return Optional.of(map(rs));}
        }catch(SQLException e){e.printStackTrace();} return Optional.empty();
    }
    private Payment map(ResultSet rs) throws SQLException {
        Timestamp ts=rs.getTimestamp("payment_date");
        return new Payment(rs.getInt("payment_id"),rs.getInt("bill_id"),rs.getInt("customer_id"),
            rs.getString("customer_name"),rs.getDouble("amount_paid"),
            Payment.PaymentMethod.valueOf(rs.getString("payment_method")),
            Payment.PaymentStatus.valueOf(rs.getString("status")),
            rs.getString("transaction_ref"),ts!=null?ts.toLocalDateTime():LocalDateTime.now(),
            rs.getString("receipt_number"));
    }
}
