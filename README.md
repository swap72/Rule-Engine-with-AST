
# Contents
1. [Overview](#overview)
2. [Project Structure](#project-structure)
3. [Technologies Used](#technologies-used)
4. [Prerequisites](#prerequisites)
5. [Setup Instructions](#setup-instructions)
    - [Clone the Repository](#1-clone-the-repository)
    - [Update Database Configuration](#2-update-database-configuration)
    - [Build and Run the Application](#3-build-and-run-the-application)
    - [Access the Application](#4-access-the-application)
6. [Usage](#usage)
    - [Rule Creation](#1-rule-creation)
    - [Rule Evaluation](#2-rule-evaluation)
    - [Example Test Case](#3-example-test-case)
7. [API Endpoints](#api-endpoints)
    - [Create Rule](#1-create-rule)
    - [Evaluate Rule](#2-evaluate-rule)
8. [Frontend UI](#frontend-ui)
9. [Visual References](#Visual-References)

# Rule Engine Application

## Overview

This is a **3-tier Rule Engine Application** built using **Java (Spring Boot)** for the backend, a simple **REST API** for rule creation and evaluation, and a **UI** built with **HTML, CSS, and JavaScript**. The application allows users to define conditional rules based on attributes like **age**, **department**, **income**, **experience**, etc., and evaluate those rules against user data.

### Features:
- **Create dynamic rules**: Create rules with logical operators like `AND`, `OR` and conditions like `>`, `<`, `=`.
- **Evaluate rules**: Test the created rules with various user data and receive a `true` or `false` response.
- **Simple UI**: A web interface for users to create rules, provide input data, and view evaluation results.

## Project Structure

```bash
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/ruleengine/
│   │   │       ├── controller/        # API endpoints (REST Controllers)
│   │   │       ├── model/             # Entity and domain model classes (e.g., Node, Rule)
│   │   │       ├── repository/        # Repository layer for database operations
│   │   │       ├── service/           # Business logic (rule creation, evaluation)
│   │   │       └── RuleEngineApplication.java  # Main class to start the Spring Boot app
│   │   └── resources/
│   │       ├── static/                # Static files like HTML, CSS, JS (UI)
│   │       │   └── index.html         # UI file for rule engine interactions
│   │       ├── application.properties # Configuration file for DB, server, etc.
├── pom.xml                            # Maven build file with dependencies
└── README.md                          # Project overview and setup instructions
```

## Technologies Used

- **Backend**: Java (Spring Boot), Spring Data JPA, H2DB.
- **Frontend**: HTML, CSS, JavaScript (Fetch API).
- **Database**: H2 (in-memory) for development and testing.
- **Build Tool**: Maven.
- **IDE**: Spring Tool Suite.


## Prerequisites

- **Java 8** or higher.
- **Maven** (for dependency management and building the project).
- **Spring Boot Plugins or STS**.
- A **web browser** for interacting with the UI.

## Setup Instructions

### 1. To Clone the Repository

```bash
git clone https://github.com/your-username/rule-engine.git
cd rule-engine
```

### 2. To Update Database Configuration

By default, the application uses an in-memory H2 volatile database for testing. If you'd like to switch to **MySQL** or another database, update the `application.properties` file located in `src/main/resources`:

```properties
# MySQL Configuration (If using MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/rule_engine
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3.To Build and Run the Application

To build and run the application locally, use Maven:

```bash
mvn clean install
mvn spring-boot:run
```

This will start the application on `http://localhost:8080`.

### 4. Access the Application

- **Frontend (UI)**: Visit `http://localhost:8080/index.html` to interact with the rule engine, (While the Application is running).
- **Backend APIs**: You can also interact with the APIs using tools like **Postman** or **cURL**.

## Usage

### 1. Rule Creation
You can create rules using the UI or via the API.

- **API Endpoint**: `POST /api/rules/create`
- **Example Rule**:

```plaintext
(age > 30 AND department = 'Sales') OR salary > 50000
```

- **UI**: Enter the rule in the "Create Rule" section and click "Create Rule".
- **Browser Message:**: Rule created successfully!

### 2. Rule Evaluation
You can evaluate rules by providing user data.

- **API Endpoint**: `POST /api/rules/evaluate`
- **UI**: Enter user data like age, department, salary, and experience, then click "Evaluate Rule".

- **Example User Data** (JSON for API):
  ```json
  {
    "age": 35,
    "department": "Sales",
    "salary": 60000,
    "experience": 3
  }
  ```

### 3. Example Test Case

1. **Create a Rule**: `(age > 30 AND experience >= 2) OR salary > 50000`
2. **Input Data for Evaluation**:
   ```json
   {
     "age": 35,
     "department": "Engineering",
     "salary": 40000,
     "experience": 3
   }
   ```
   - **Expected Output**: `true`

## API Endpoints

### 1. **Create Rule**
- **URL**: `/api/rules/create`
- **Method**: `POST`
- **Request Body**: Plain text (rule string)
- **Response**: Abstract Syntax Tree (AST) in JSON format.

### 2. **Evaluate Rule**
- **URL**: `/api/rules/evaluate`
- **Method**: `POST`
- **Query Parameter**: `rule` (rule string)
- **Request Body**: JSON containing user data (e.g., age, department, salary).
- **Response**: Boolean (`true` or `false`).

## Frontend UI

The frontend UI is located at `src/main/resources/static/index.html`. It includes:
- A form to create rules.
- Input fields to provide user data for rule evaluation.
- Displays the evaluation result (`true` or `false`).



# Creating and Testing Rules in Postman

This guide will help you create and evaluate rules using **Postman** with the provided backend APIs.

## 1. Create a Rule

### Method:
`POST`

### URL:
`http://localhost:8080/api/rules/create`

### Headers:
- `Content-Type`: `text/plain`

### Body (Example Rule):
```plaintext
(age > 30 AND department = 'Sales') OR salary > 50000
```

### Example in Postman:
1. Open **Postman**.
2. Select `POST` as the request method.
3. Enter the URL: `http://localhost:8080/api/rules/create`.
4. Under **Headers**, add:
   - `Content-Type: text/plain`
5. In the **Body** tab, select `raw`, and enter the rule as plain text, for example:
   ```plaintext
   (age > 30 AND department = 'Sales') OR salary > 50000
   ```
6. Click **Send**.

### Expected Response:
A JSON response representing the AST (Abstract Syntax Tree), for example:
```json
{
  "type": "operator",
  "value": "OR",
  "left": {
    "type": "operator",
    "value": "AND",
    "left": {
      "type": "operand",
      "value": "age > 30"
    },
    "right": {
      "type": "operand",
      "value": "department = 'Sales'"
    }
  },
  "right": {
    "type": "operand",
    "value": "salary > 50000"
  }
}
```

---

## 2. Evaluate a Rule

### Method:
`POST`

### URL:
`http://localhost:8080/api/rules/evaluate?rule=(age > 30 AND department = 'Sales') OR salary > 50000`

### Headers:
- `Content-Type`: `application/json`

### Body (Example User Data):
```json
{
  "age": 35,
  "department": "Sales",
  "salary": 60000,
  "experience": 3
}
```

### Example in Postman:
1. Open **Postman**.
2. Select `POST` as the request method.
3. Enter the URL: `http://localhost:8080/api/rules/evaluate?rule=(age > 30 AND department = 'Sales') OR salary > 50000`.
4. Under **Headers**, add:
   - `Content-Type: application/json`
5. In the **Body** tab, select `raw`, and enter the user data in JSON format, for example:
   ```json
   {
     "age": 35,
     "department": "Sales",
     "salary": 60000,
     "experience": 3
   }
   ```
6. Click **Send**.

### Expected Response:
The response will be a boolean value (`true` or `false`), for example:
```json
true
```

---

## Testing with Different Data

### Test Case 1: Should Return `true`
**Data**:
```json
{
  "age": 35,
  "department": "Sales",
  "salary": 60000,
  "experience": 3
}
```
- **Expected Output**: `true`

### Test Case 2: Should Return `false`
**Data**:
```json
{
  "age": 25,
  "department": "Marketing",
  "salary": 45000,
  "experience": 2
}
```
- **Expected Output**: `false`



## Visual References

### Rule Creation and Evaluation
![createAPI](https://github.com/user-attachments/assets/5b4cb77a-4f04-442e-99ad-f9bb71d6d76e)
![TestAPI](https://github.com/user-attachments/assets/97a341f8-fad8-4d7c-ab86-f63277670f19)
![1](https://github.com/user-attachments/assets/37a7a173-a680-4892-87cc-f9785163d29d)
![2](https://github.com/user-attachments/assets/590afcfe-386e-44f4-8259-a14acce7c07d)





