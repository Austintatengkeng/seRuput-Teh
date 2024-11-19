# seRuput Teh Management System

A JavaFX-based product management and registration application for seRuput Teh, a tea shop or inventory system. This project includes functionality for managing products (add, update, delete) and a registration module that enforces validation rules for user inputs. The project is built with JavaFX for the GUI and SQL for data storage.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Dependencies](#dependencies)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Product Management**: Add, update, delete products in the system.
  - Check if product names are unique when adding a new product.
  - Validation for price values during product addition and updates.
  - Display of product details with price and description.
- **User Registration**:
  - Form validation with multiple rules (e.g., email format, phone number prefix, password confirmation).
  - Terms and conditions checkbox requirement.
  - Username uniqueness check during registration.
- **Navigation**: Simple UI navigation using `AdminNav`.

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Austinttattengkeng/seRuput-Teh.git
   cd seRuput-Teh
   ```

2. **Set Up Database**:
   - This application requires an SQL database. You will need to configure your database connection in the `Database` class. Create a database and set up the `products` and `users` tables to match the structure used in the project.

3. **Open the Project in Your IDE**:
   - Open the project in an IDE that supports JavaFX and Java, like IntelliJ IDEA or Eclipse.
   - Ensure that your IDE is configured to support JavaFX.

4. **Run the Application**:
   - Run the `main.Main` class to start the application.

## Usage

### Product Management

- **Add Product**: Fill in product details in the form (name, price, description) and click "Add Product".
- **Update Product**: Select a product, input a new price, and click "Update".
- **Remove Product**: Select a product and click "Remove Product" to delete it from the list.

### User Registration

- **Create a New Account**: Access the registration form, fill in the required fields (username, email, password, etc.), and click "Register".
- **Login Redirect**: If the user already has an account, they can click the "login here" link to be redirected.

## Dependencies

- **JavaFX**: Used for building the graphical user interface.
- **SQL**: Used for managing the backend database for storing product and user data.
- **Java SDK 11+**: Compatible with JavaFX and SQL drivers.

## Project Structure

```
src/
├── main/
│   ├── Main.java             # Entry point for the application
│   └── HelpersFunc.java      # Contains helper functions for validation and alerts
├── view/
│   ├── ManageProduct.java    # Product management UI and functionality
│   ├── Register.java         # User registration form and validation
│   └── Login.java            # User login functionality
├── db/
│   └── Database.java         # Database connection and query handling
└── NavBar/
    └── AdminNav.java         # Navigation bar for admin interface
```

## Contributing

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature-name`).
3. Commit your changes (`git commit -m "Add new feature"`).
4. Push to the branch (`git push origin feature-name`).
5. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

This README template will help users and contributors understand your project’s functionality and requirements, as well as how to get started. Let me know if you need help with specific sections!
