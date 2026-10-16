# Lab 1: Java JDBC & MySQL Database Operations

A foundational Java application demonstrating database connectivity, schema creation, and full CRUD operations on student records using JDBC and MySQL.

## Prerequisites

Before running the application, ensure the following are installed:

- **Java Development Kit (JDK)**: Version 17 or higher
- **MySQL / MariaDB Server**: Version 8.0+ (running locally on port `3306`)
- **MySQL Connector/J**: JDBC Driver (e.g. `mysql-connector-j-8.x+` or `mysql-connector-j-26.x`)
- **Git**

Verify your local environment:

```bash
java -version
mysql --version
```



## Project Structure

```text
Lab 1/
├── src/
│   └── Main.java                 # Main application with JDBC logic & CRUD operations
├── Lab1_jdbc_connection1.iml     # IntelliJ IDEA module configuration & driver reference
└── README.md                     # Project documentation
```
.

## How to Run

### Method 1: Using IntelliJ IDEA (Recommended)

1. Open the project folder (`Lab 1`) in IntelliJ IDEA.
2. Ensure the MySQL Connector JAR is configured:
   - Go to **File** > **Project Structure** > **Libraries**.
   - Add `mysql-connector-j-*.jar` if it is not already resolved.
3. Start your local MySQL service:
   ```bash
   sudo systemctl start mariadb   # or: sudo systemctl start mysql
   ```
4. Open [`src/Main.java`](src/Main.java) and click the green **Run** icon (or press `Shift + F10`).

### Method 2: Using the Command Line

1. Make sure your MySQL service is active.
2. Compile the Java source file specifying the path to the MySQL connector jar:
   ```bash
   javac -cp ".:path/to/mysql-connector-j.jar" src/Main.java -d out
   ```
3. Run the application:
   ```bash
   java -cp "out:path/to/mysql-connector-j.jar" Main
   ```

