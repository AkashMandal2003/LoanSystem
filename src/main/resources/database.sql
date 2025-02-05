CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    address VARCHAR(255),
    identity_number VARCHAR(50) UNIQUE NOT NULL
);


CREATE TABLE loan_product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    term_months INT NOT NULL,
    min_amount DECIMAL(15,2) NOT NULL,
    max_amount DECIMAL(15,2) NOT NULL,
    description TEXT
);

CREATE TABLE loan_application (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    product_id INT,
    loan_amount DECIMAL(15,2) NOT NULL,
    application_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    requested_term INT NOT NULL,
    loan_purpose TEXT,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (product_id) REFERENCES loan_product(product_id)
);


CREATE TABLE loan (
    loan_id INT AUTO_INCREMENT PRIMARY KEY,
    application_id INT,
    disbursement_date DATE NOT NULL,
    loan_amount DECIMAL(15,2) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    loan_term_months INT NOT NULL,
    loan_balance DECIMAL(15,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (application_id) REFERENCES loan_application(application_id)
);


CREATE TABLE loan_payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    loan_id INT,
    payment_date DATE NOT NULL,
    payment_amount DECIMAL(15,2) NOT NULL,
    remaining_balance DECIMAL(15,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    FOREIGN KEY (loan_id) REFERENCES loan(loan_id)
);


CREATE TABLE risk_assessment (
    assessment_id INT AUTO_INCREMENT PRIMARY KEY,
    application_id INT,
    credit_score INT NOT NULL,
    income DECIMAL(15,2) NOT NULL,
    approved_amount DECIMAL(15,2) NOT NULL,
    approved_term INT NOT NULL,
    approval_status VARCHAR(50) NOT NULL,
    assessment_date DATE NOT NULL,
    FOREIGN KEY (application_id) REFERENCES loan_application(application_id)
);


CREATE TABLE loan_disbursement (
    disbursement_id INT AUTO_INCREMENT PRIMARY KEY,
    loan_id INT,
    disbursement_date DATE NOT NULL,
    disbursed_amount DECIMAL(15,2) NOT NULL,
    disbursement_method VARCHAR(50) NOT NULL,
    FOREIGN KEY (loan_id) REFERENCES loan(loan_id)
);


CREATE TABLE credit_score (
    credit_score_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    score INT NOT NULL,
    score_date DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);


CREATE INDEX idx_customer_id ON loan_application(customer_id);
CREATE INDEX idx_product_id ON loan_application(product_id);
CREATE INDEX idx_application_id ON loan(application_id);
CREATE INDEX idx_loan_id ON loan_payment(loan_id);
