# Java Banking System Simulation

A comprehensive Java-based banking system simulation that provides a secure, multi-threaded environment for managing bank accounts and transactions.

## Features

- **User Authentication & Role-Based Access**
  - Secure login and registration system
  - Role-based permissions (admin and customer)
  - Password hashing with salting for security

- **Account Management**
  - Create checking and savings accounts
  - View account balances and details
  - Manage multiple accounts per user

- **Transaction Processing**
  - Deposit money
  - Withdraw funds
  - Transfer between accounts
  - View transaction history

- **Admin Functions**
  - View all users in the system
  - Monitor all accounts
  - Track all transactions
  - Create admin users

- **Technical Features**
  - Multi-threaded transaction processing
  - Thread-safe operations with locks
  - File-based data persistence using JSON
  - No SQL database dependencies

## Technology Stack

- Java 8+
- Jackson for JSON processing
- File-based storage
- Thread concurrency management

## Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Maven (optional for dependency management)

### Option 1: Manual Setup
1. Clone the repository
   ```bash
   git clone https://github.com/yourusername/banking-system.git
   cd banking-system
   ```

2. Create a `lib` directory and download the Jackson libraries
   ```bash
   mkdir lib
   cd lib
   # Download Jackson JAR files (see commands in the build script below)
   ```

3. Compile the application
   ```bash
   cd src/main/java
   javac -cp ".;../../../lib/*" com/bankingsystem/BankingSystem.java com/bankingsystem/model/*.java com/bankingsystem/repository/*.java com/bankingsystem/service/*.java com/bankingsystem/util/*.java
   ```

4. Run the application
   ```bash
   java -cp ".;../../../lib/*" com.bankingsystem.BankingSystem
   ```

### Option 2: Using the Build Script
1. Clone the repository
   ```bash
   git clone https://github.com/yourusername/banking-system.git
   cd banking-system
   ```

2. Run the build script
   ```bash
   ./build-and-run.bat  # Windows
   ```

The build script (`build-and-run.bat`) contains:
```batch
@echo off
echo Banking System Builder and Runner
echo ===============================

REM Create lib directory if it doesn't exist
if not exist lib mkdir lib
cd lib

REM Download Jackson libraries if they don't exist
if not exist jackson-databind-2.14.0.jar (
    echo Downloading Jackson libraries...
    powershell -Command "Invoke-WebRequest -Uri https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.14.0/jackson-databind-2.14.0.jar -OutFile jackson-databind-2.14.0.jar"
    powershell -Command "Invoke-WebRequest -Uri https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.14.0/jackson-core-2.14.0.jar -OutFile jackson-core-2.14.0.jar"
    powershell -Command "Invoke-WebRequest -Uri https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.14.0/jackson-annotations-2.14.0.jar -OutFile jackson-annotations-2.14.0.jar"
    powershell -Command "Invoke-WebRequest -Uri https://repo1.maven.org/maven2/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.14.0/jackson-datatype-jsr310-2.14.0.jar -OutFile jackson-datatype-jsr310-2.14.0.jar"
)

cd ..

REM Compile the code
echo Compiling...
cd src\main\java
javac -cp ".;../../../lib/*" com/bankingsystem/BankingSystem.java com/bankingsystem/model/*.java com/bankingsystem/repository/*.java com/bankingsystem/service/*.java com/bankingsystem/util/*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    cd ..\..\..
    pause
    exit /b %errorlevel%
)

REM Run the application
echo Running Banking System...
java -cp ".;../../../lib/*" com.bankingsystem.BankingSystem

cd ..\..\..
pause
```

### Option 3: Using Maven (Recommended)
1. Clone the repository
   ```bash
   git clone https://github.com/yourusername/banking-system.git
   cd banking-system
   ```

2. Build and run with Maven
   ```bash
   mvn compile
   mvn exec:java
   ```

## Usage

### Default Admin Account
On first run, the system creates a default admin user:
- Username: `admin`
- Password: `admin123`

### Customer Operations
1. **Register a new account**
   - Select option 2 from the main menu
   - Enter username and password

2. **Create a bank account**
   - Login as a customer
   - Select option 2 from the customer menu
   - Choose account type and initial deposit

3. **Deposit money**
   - Login as a customer
   - Select option 3 from the customer menu
   - Follow the prompts to select an account and amount

4. **Withdraw money**
   - Login as a customer
   - Select option 4 from the customer menu
   - Follow the prompts to select an account and amount

5. **Transfer money**
   - Login as a customer
   - Select option 5 from the customer menu
   - Follow the prompts for source account, destination account, and amount

### Admin Operations
1. **View all users**
   - Login as admin
   - Select option 1 from the admin menu

2. **View all accounts**
   - Login as admin
   - Select option 2 from the admin menu

3. **View all transactions**
   - Login as admin
   - Select option 3 from the admin menu

4. **Create admin user**
   - Login as admin
   - Select option 4 from the admin menu
   - Select option 1 from the user management menu

## Project Structure
```
bankingsystem/
├── data/                       # JSON data storage
├── lib/                        # External libraries
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── bankingsystem/
│   │               ├── model/              # Data models
│   │               │   ├── User.java
│   │               │   ├── Account.java
│   │               │   └── Transaction.java
│   │               ├── repository/         # Data access
│   │               │   ├── UserRepository.java
│   │               │   ├── AccountRepository.java
│   │               │   └── TransactionRepository.java
│   │               ├── service/            # Business logic
│   │               │   ├── AuthenticationService.java
│   │               │   ├── AccountService.java
│   │               │   ├── TransactionService.java
│   │               │   └── UserService.java
│   │               ├── util/               # Utilities
│   │               │   ├── PasswordUtils.java
│   │               │   └── JsonUtils.java
│   │               └── BankingSystem.java  # Main application
│   └── test/                   # Test classes (if any)
└── pom.xml                     # Maven configuration
```

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## Acknowledgements
- This project was created as a demonstration of Java programming concepts including multi-threading, file I/O, and object-oriented design.
