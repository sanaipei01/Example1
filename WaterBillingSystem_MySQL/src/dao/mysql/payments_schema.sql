CREATE TABLE IF NOT EXISTS payments (
    payment_id        INT AUTO_INCREMENT PRIMARY KEY,
    bill_id           INT NOT NULL,
    customer_id       INT NOT NULL,
    customer_name     VARCHAR(150) NOT NULL,
    amount_paid       DECIMAL(10,2) NOT NULL,
    payment_method    ENUM('CASH','MPESA','BANK_TRANSFER','CARD') NOT NULL,
    status            ENUM('PENDING','COMPLETED','FAILED','REFUNDED') DEFAULT 'PENDING',
    transaction_ref   VARCHAR(100) UNIQUE,
    receipt_number    VARCHAR(100) UNIQUE,
    payment_date      DATETIME DEFAULT CURRENT_TIMESTAMP,
    notes             TEXT,
    FOREIGN KEY (bill_id) REFERENCES bills(bill_id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);
CREATE INDEX idx_payments_customer ON payments(customer_id);
CREATE INDEX idx_payments_bill ON payments(bill_id);
