-- Create Database
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- 1. Table: Library Profiles
CREATE TABLE IF NOT EXISTS libraries (
    library_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    address VARCHAR(255),
    location_link VARCHAR(255),
    photo_path VARCHAR(255),
    owner_name VARCHAR(100),
    owner_phone VARCHAR(20),
    owner_email VARCHAR(100),
    owner_aadhaar VARCHAR(20),
    is_approved BOOLEAN DEFAULT FALSE,
    payment_done BOOLEAN DEFAULT FALSE,
    admin_username VARCHAR(50),
    admin_password VARCHAR(50)
);

-- 2. Table: Users (Students)
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50),
    full_name VARCHAR(100),
    gender VARCHAR(10),
    aadhaar_number VARCHAR(20),
    phone_number VARCHAR(20),
    address VARCHAR(255),
    has_paid_fees BOOLEAN DEFAULT FALSE,
    elibrary_expenditure DOUBLE DEFAULT 0.0,
    library_id VARCHAR(50),
    FOREIGN KEY (library_id) REFERENCES libraries(library_id) ON DELETE CASCADE
);

-- 3. Table: Books
CREATE TABLE IF NOT EXISTS books (
    title VARCHAR(100),
    author VARCHAR(100),
    category VARCHAR(50),
    subject VARCHAR(50),
    is_available BOOLEAN DEFAULT TRUE,
    borrowed_by_user VARCHAR(50),
    borrowed_date DATE,
    current_borrowing_id VARCHAR(50),
    location_row VARCHAR(10),
    location_section VARCHAR(10),
    location_block VARCHAR(10),
    image_path VARCHAR(255),
    library_id VARCHAR(50),
    PRIMARY KEY (title, library_id), -- Composite key to allow same book title in different libraries
    FOREIGN KEY (library_id) REFERENCES libraries(library_id) ON DELETE CASCADE,
    FOREIGN KEY (borrowed_by_user) REFERENCES users(username) ON DELETE SET NULL
);

-- 4. Table: Transactions
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50),
    book_title VARCHAR(100),
    activity_type VARCHAR(50),
    date DATE,
    details VARCHAR(255),
    amount DOUBLE,
    library_id VARCHAR(50),
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE SET NULL,
    FOREIGN KEY (library_id) REFERENCES libraries(library_id) ON DELETE CASCADE
);

-- 5. Table: eLibrary Store Items (Global)
CREATE TABLE IF NOT EXISTS store_items (
    item_id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(100),
    price DOUBLE,
    item_type VARCHAR(20), -- PDF or PHYSICAL
    content_path VARCHAR(255),
    description VARCHAR(255)
);

-- 6. Table: Orders
CREATE TABLE IF NOT EXISTS orders (
    order_id VARCHAR(50) PRIMARY KEY,
    student_username VARCHAR(50),
    library_id VARCHAR(50),
    item_name VARCHAR(100),
    amount DOUBLE,
    shipping_address VARCHAR(255),
    is_shipped BOOLEAN DEFAULT FALSE,
    order_date DATE,
    FOREIGN KEY (student_username) REFERENCES users(username) ON DELETE CASCADE
);

-- Insert Default eLibrary Items
INSERT IGNORE INTO store_items VALUES 
('ITM001', 'Java Programming', 'James Gosling', 50.0, 'PDF', 'java.pdf', 'Complete Guide'),
('ITM002', 'World Atlas', 'Map Co.', 500.0, 'PHYSICAL', 'atlas.jpg', 'Hardcover Atlas');