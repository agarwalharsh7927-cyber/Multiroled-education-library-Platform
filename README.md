# Multiroled-education-library-Platform
Library Management System with Student and Librarian Login BY CODE AGNI
Next-Gen Library SaaS Platform

📚 Project Overview

This project is a comprehensive Multi-Branch Library Management System designed as a SaaS platform. It integrates traditional library operations (book borrowing, returning) with modern digital features like an eLibrary for PDF access and an eCommerce store for purchasing physical books. The system supports multiple library branches, role-based access (Student, Librarian, Super Admin), and features a study space booking system.

The application is built using Java Swing for the GUI and JDBC for robust database connectivity with MySQL, adhering to core Object-Oriented Programming (OOP) principles.

Note: While the primary production database is MySQL via JDBC, for local testing or offline scenarios, XML can be used as a lightweight, locally hosted database platform.

🔐 Default Credentials

Use these credentials to log in and test the different roles in the system:

Super Admin

Username: superadmin

Password: super123

Note: Full system access. Can approve new libraries.

Librarian

Username: (Created by Super Admin)

Password: (Created by Super Admin)

Note: Credentials are set by the Super Admin after approving a library registration request.

Student

Username: (Registered by User)

Password: (Registered by User)

Note: Create a new account via the "Student Portal" -> "Register" screen.

Database

Username: root

Password: password

Note: Default MySQL credentials. Update DatabaseConnection.java if yours differ.

🚀 Key Features

1. Multi-Branch Architecture

Library Registration: New libraries can register on the platform by providing details (owner info, address, photo) and paying a registration fee.

Approval Workflow: A Super Admin reviews and approves library registrations, generating admin credentials for each branch.

2. Core Library Operations

Book Management: Librarians can add, edit, and remove books from their branch inventory.

Circulation: Students can borrow and return books. The system tracks due dates and calculates late fines automatically.

Reservation System: If a book is borrowed, students can join a reservation queue.

3. Smart Study Space

Seat Booking: Students can view real-time availability of study seats and book specific time slots (e.g., 9-12, 12-3).

Capacity Management: The system prevents overbooking and tracks revenue from seat rentals.

4. Digital eLibrary & eCommerce

eBooks (PDFs): Students can browse a global catalog of PDF books and "download" them (simulated payment & download).

Physical Store: Students can order physical books for delivery. The system tracks shipping addresses and order status.

Global Inventory: The Super Admin manages the central repository of eLibrary items available to all branches.

5. User Roles

Student: Browse books, borrow/return, book seats, access eLibrary, view personal history.

Librarian: Manage branch inventory, view transaction logs, export reports to CSV.

Super Admin: Approve libraries, manage global eStore, oversee system-wide health.

🛠️ Technical Architecture & Rubric Compliance

This project strictly follows the Java GUI-Based Projects Marking Rubric.

1. OOP Implementation 

Inheritance: The User class extends the abstract Person class, inheriting common fields like name and address.

Polymorphism: The Book class implements the Searchable interface, allowing for polymorphic search behavior.

Encapsulation: All data fields are private with public Getters/Setters.

Exception Handling: Custom LibraryActionException handles business logic errors (e.g., double booking) gracefully.

Interfaces: The Searchable interface defines a contract for searchable entities.

2. Collections & Generics 

Collections: Extensive use of ArrayList for storing logs and HashMap (in memory caching) before DB writes.

Generics: A generic method filterList<T> in Library.java allows filtering lists of Books or Users using predicates.

3. Multithreading & Synchronization

Synchronization: The bookSeat() method is synchronized to prevent race conditions where two users might book the last seat simultaneously.

Multithreading:

Auto-Save: A background daemon thread saves system state (if using file mode) or performs health checks.

Simulation: Downloading an eBook triggers a separate thread to update a progress bar without freezing the GUI.

4. Database Connectivity (JDBC) 

Connection: DatabaseConnection.java implements the Singleton pattern to manage the MySQL connection.

DAO Pattern: Data Access Objects (UserDAO, BookDAO, LibraryDAO, etc.) separate SQL logic from business logic.

Operations: Full CRUD (Create, Read, Update, Delete) support for Users, Books, and Transactions.

XML Support: XML is used as an alternative locally hosted database platform for configuration or offline data storage.

📂 File Structure & Description

User Interface

LibraryManagementGUI.java: The main application window. Features a modern "Glassmorphism" design with a split-screen layout, dark mode, and semi-transparent panels.

System Controllers

LibrarySystem.java: Root controller. Manages the Super Admin session, global eStore inventory, and the list of registered libraries.

Library.java: Branch controller. Manages logic specific to one library (local books, users, seat capacity).

Database Layer

DatabaseConnection.java: Manages JDBC connection to MySQL.

library_schema.sql: SQL script to create the database and tables.

UserDAO.java: SQL operations for Student data.

BookDAO.java: SQL operations for Book inventory.

LibraryDAO.java: SQL operations for Library profiles and approvals.

TransactionDAO.java: SQL operations for logging activities.

StoreDAO.java: SQL operations for eLibrary items and orders.

Data Models (Entities)

Person.java (Abstract): Base class for people.

User.java: Student entity with borrowing history.

Book.java: Book entity with reservation queue.

LibraryProfile.java: Stores registration info for a library.

StoreItem.java: Represents a PDF or Physical product.

Order.java: Represents an eCommerce order.

TransactionRecord.java: A log entry for history/reporting.

Helpers & Interfaces

ActivityType.java: Enum defining all system actions.

Searchable.java: Interface for search functionality.

LibraryActionException.java: Custom exception class.

⚙️ Setup & Usage Guide

Prerequisites

Java Development Kit (JDK): Version 8 or higher.

MySQL Server: Installed and running.

MySQL JDBC Driver: Add mysql-connector-j-8.x.x.jar to your project's classpath.

Step 1: Database Setup

Open your MySQL Workbench or Command Line.

Run the script provided in library_schema.sql. This creates the library_db database and all required tables.

Step 2: Configure Connection

Open DatabaseConnection.java.

Update the USER and PASSWORD constants to match your local MySQL credentials.

Step 3: Run the Application

Compile the project: javac *.java

Run the main class: java LibraryManagementGUI

How to Use

Register a Library: Click "Register New Library", fill in details, and click "Pay & Register".

Approve Library: Log in as Super Admin (User: superadmin, Pass: super123). Go to Dashboard -> Apremoprove the pending request.

Enter Library: Go back to Home -> Click "Student Portal" -> Select your library -> Register a new student account.

Manage: Log in as Librarian (using credentials set by Super Admin) to add books.

Use Features: As a student, book a seat, search for books, or visit the eLibrary portal to buy PDFs.

Developed for Java GUI Project Review 1
