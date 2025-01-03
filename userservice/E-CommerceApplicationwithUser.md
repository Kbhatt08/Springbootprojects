# E-Commerce Application with User and Order Services

## Overview

You are tasked with developing an **E-Commerce Platform** with a microservices architecture using Spring Boot. The platform will consist of two microservices: **User Service** and **Order Service**.

- **User Service** will handle user management (authentication and user data).
- **Order Service** will handle the creation and management of orders placed by users.

The application will be **secured with OAuth 2.0**, with **PostgreSQL** as the database and **Kafka** for messaging between microservices.

---

## Microservices Overview

### 1. User Service (Authentication & User Data Management)

**Responsibilities:**
- User registration and authentication via OAuth 2.0.
- Store user information (e.g., username, email, password).
- Retrieve user details using OAuth 2.0 token.
- Produce messages to Kafka for user-related events (e.g., new user registration).

**Technology Stack:**
- Spring Boot
- Spring Security with OAuth 2.0 (JWT)
- PostgreSQL for user data storage
- Kafka for messaging
- Swagger/OpenAPI for API documentation
- JUnit for unit and integration tests

---

### 2. Order Service (Order Management)

**Responsibilities:**
- Process orders placed by users.
- Store order details in PostgreSQL.
- Consume Kafka messages sent by the **User Service** to handle order creation once a user is registered.
- Secure API endpoints with OAuth 2.0 and provide access based on roles.
- Expose RESTful APIs to create and manage orders (e.g., create order, update order status).

**Technology Stack:**
- Spring Boot
- Spring Security with OAuth 2.0 (JWT)
- PostgreSQL for order data storage
- Kafka for messaging
- Swagger/OpenAPI for API documentation
- JUnit for unit and integration tests

---

### 3. OAuth 2.0 Authorization Server (Keycloak or Auth0)

**Responsibilities:**
- Provide OAuth 2.0 token issuance for user authentication and authorization.
- Secure the **User Service** and **Order Service** by validating JWT tokens.
- Enable role-based access for services.
- Redirect users to authenticate via OAuth 2.0 and issue tokens.

**Technology Stack:**
- Keycloak (or Auth0, Okta, etc.)
- OAuth 2.0 Authorization Code Flow

---

## Application Count and Details

---

### 1. User Service Application

**API Endpoints:**
- `POST /users/register`: Register a new user. (Secure with OAuth 2.0)
- `GET /users/{id}`: Retrieve user data using the OAuth 2.0 token. (Secure with OAuth 2.0)
- `POST /users/login`: OAuth 2.0 login, redirects to Authorization Server.
  
**Database**: PostgreSQL
- **User Table**: Stores user information like `id`, `username`, `email`, `password`.
  
**Kafka**:
- **Producer**: Sends a Kafka message to the **Order Service** when a new user is registered.

**OAuth 2.0 Security**:
- Protects API endpoints using JWT tokens issued by the Authorization Server.
- The Authorization Server issues a token upon successful user login, and user details can be retrieved securely using this token.

**Swagger Documentation**: Document the API endpoints and authentication flows using Swagger.

---

### 2. Order Service Application

**API Endpoints:**
- `POST /orders`: Create a new order (secure with OAuth 2.0).
- `GET /orders/{id}`: Retrieve order details using OAuth 2.0 token (secure with OAuth 2.0).
- `PUT /orders/{id}/status`: Update order status (secure with OAuth 2.0).
  
**Database**: PostgreSQL
- **Order Table**: Stores order information like `id`, `user_id` (foreign key), `order_date`, `status`, `total_amount`.
  
**Kafka**:
- **Consumer**: Listens to Kafka messages from **User Service** to process orders when a new user is created.
  
**OAuth 2.0 Security**:
- Protects API endpoints using JWT tokens issued by the Authorization Server.
- Users can only access their own orders based on the OAuth 2.0 token’s claims (i.e., user_id).

**Swagger Documentation**: Document the API endpoints and authentication flows using Swagger.

---

### 3. OAuth 2.0 Authorization Server (Keycloak/Auth0/Okta)

**Responsibilities**:
- Issue OAuth 2.0 tokens (JWT).
- Authenticate users when they attempt to log in.
- Authorize access to the **User Service** and **Order Service** based on the roles assigned to the user (e.g., `USER`, `ADMIN`).
- Provide an authorization endpoint, token endpoint, and user info endpoint for OAuth 2.0 flows.
  
**Configuration**:
- Keycloak is configured with two clients: **User Service** and **Order Service**.
- Both clients are configured with **Authorization Code Flow** (OAuth 2.0).
- The Authorization Server issues JWT tokens for authenticated users and validates tokens for the microservices.

---

## Flow of Interaction Between Services

1. **User Authentication:**
   - A user requests access to the application.
   - The user is redirected to the **OAuth 2.0 Authorization Server** for authentication.
   - Upon successful authentication, the server issues a **JWT token**.

2. **User Service Interaction:**
   - The **User Service** receives the **JWT token** in the request header.
   - It validates the token and processes the request (e.g., registering the user, fetching user data).

3. **Kafka Messaging:**
   - After user registration, the **User Service** sends a Kafka message to notify the **Order Service** of the new user.
   
4. **Order Service Interaction:**
   - The **Order Service** consumes the Kafka message sent by the **User Service**.
   - It processes the order based on the new user details.
   - The **Order Service** verifies the **JWT token** to ensure the request is from an authenticated user and allows them to place an order.

5. **Order Creation & Management:**
   - The user can create an order via the **Order Service** API, secured by OAuth 2.0.
   - The **Order Service** checks if the user is authorized (based on the `user_id` in the JWT token).

---

## Application Details

| **Application Name**  | **Responsibilities**                                          | **Database**         | **Message Broker**     | **Security**                 |
|-----------------------|---------------------------------------------------------------|----------------------|------------------------|------------------------------|
| **User Service**       | User registration, authentication, and user data management. | PostgreSQL (User)    | Kafka (Producer)       | OAuth 2.0 (JWT Tokens)       |
| **Order Service**      | Order creation, order management, and order processing.      | PostgreSQL (Order)   | Kafka (Consumer)       | OAuth 2.0 (JWT Tokens)       |
| **OAuth 2.0 Server**   | Authentication and authorization via OAuth 2.0.              | N/A                  | N/A                    | OAuth 2.0 Authorization Flow |

---

## Optional Enhancements

- **Rate Limiting**: Add rate limiting to APIs to prevent abuse.
- **Service Discovery**: Use Spring Cloud Eureka for microservices discovery if deploying in a distributed system.
- **Caching**: Integrate Redis or Ehcache to cache frequently accessed data, such as user information.

---

This detailed scenario provides a complete picture of the architecture, components, and flow of data between the services. You can now proceed with the implementation based on these details.


# User Service Application Overview

The **User Service Application** is responsible for managing user-related operations such as registration, authentication, and user data retrieval. It ensures that only authorized users can access protected resources in the system, and it also communicates with the **OAuth 2.0 Authorization Server** to manage user tokens (like JWT tokens).

---

## 1. User Registration Flow

When a new user registers, the following steps occur:

- **User Submits Registration Data**:
  - The user provides details such as:
    - `username`
    - `email`
    - `password` (hashed before storage)
  - You will **hash the password** before storing it to ensure it's secure. You can use a library like `BCrypt` for password hashing in Spring Security.

- **User Data Stored in PostgreSQL**:
  - The **User Service** stores this data in the **PostgreSQL database**. 
  - A typical `User` table could look like this:
    ```sql
    CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        username VARCHAR(255) UNIQUE NOT NULL,
        email VARCHAR(255) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    ```
  - The **password** stored in the database is **hashed** (not in plaintext), and other information like `username`, `email`, and `created_at` is stored as is.

- **Sending User to OAuth Server**:
  - After storing the user data, the **User Service** communicates with the **OAuth 2.0 Authorization Server** (e.g., **Keycloak** or **Auth0**).
  - The Authorization Server will be responsible for issuing tokens, handling authentication, and managing user sessions.

- **Successful Registration Response**:
  - Once the registration is successful, the **User Service** might return a response like:
    ```json
    {
      "message": "User registered successfully",
      "status": "success"
    }
    ```

  The **OAuth 2.0 token** (JWT) is not issued immediately during registration but will be issued during the login process (explained below).

---

## 2. User Login Flow

When a user logs in:

- **User Submits Login Data**:
  - The user provides:
    - `username`
    - `password`

- **Password Validation**:
  - The **User Service** first checks the database to find the user associated with the provided `username` (or `email`, depending on your preference).
  - Then, the **User Service** compares the **hashed password** stored in the database with the password provided by the user. The comparison uses the same hashing algorithm (e.g., `BCrypt`) to ensure the passwords match.

- **Token Issuance** (via OAuth 2.0 Authorization Server):
  - If the credentials are valid (i.e., username and password match), the **User Service** communicates with the **OAuth 2.0 Authorization Server** to request an OAuth 2.0 **Access Token** (usually a JWT token).
  - This token contains the **user’s details** (like `user_id`, `username`, `roles`, etc.) and is used for authentication in subsequent requests.
  - The **Authorization Server** issues the token and returns it to the **User Service**.

- **Response with JWT Token**:
  - The **User Service** then responds with the token to the client (e.g., frontend or mobile app).
  - Example response:
    ```json
    {
      "access_token": "<JWT Token>",
      "expires_in": 3600,
      "token_type": "Bearer"
    }
    ```

- **Token Usage**:
  - The client now stores this **JWT token** (usually in memory or local storage on the frontend) and includes it in the **Authorization header** for subsequent requests to the backend.
  - For example, to access a protected route, the client sends:
    ```
    Authorization: Bearer <JWT Token>
    ```

  - The **User Service** (and other microservices) will validate the JWT token in every request to ensure that it’s issued by a trusted OAuth 2.0 server and is valid (not expired or tampered).

---

## 3. What Information Do You Store?

In the **User Service**, you store:

- **User Information**:
  - `username`: Unique username to identify the user.
  - `email`: User's email address, often used for notifications and authentication.
  - `password`: **Hashed** password (never store plaintext passwords).
  - `created_at`: The date and time when the user was registered.

  This data is typically stored in a **PostgreSQL database** in a `users` table.

- **Password Handling**:
  - **Never store passwords in plaintext**. Use a hashing function such as **BCrypt** or **PBKDF2** to securely store the password.
  - When the user logs in, you **hash the input password** and compare it to the stored hash. If they match, the user is authenticated.

---

## 4. OAuth Token and Its Role

- **JWT Token**:
  - The **OAuth 2.0 Authorization Server** issues a **JWT token** when the user successfully logs in.
  - The token contains user-related data (e.g., `user_id`, `roles`).
  - This JWT is used for authentication in future API requests. The token validates the user’s identity and permissions.
  
- **Token Expiration**:
  - The token has an **expiration time** (`expires_in`) after which the user will need to refresh the token or log in again.
  - In a production system, you may implement **refresh tokens** to allow the user to renew the access token without re-entering their credentials.

---

## 5. Structure of the User Service and Its Endpoints

Here’s a basic structure for the **User Service** and its functionalities:

### a) **User Registration Endpoint** (`POST /users/register`)
- Request:
    ```json
    {
      "username": "john_doe",
      "email": "john.doe@example.com",
      "password": "secure_password"
    }
    ```
- Operation: Stores user in the PostgreSQL database, sends a request to the OAuth Authorization Server.
- Response:
    ```json
    {
      "message": "User registered successfully",
      "status": "success"
    }
    ```

### b) **User Login Endpoint** (`POST /users/login`)
- Request:
    ```json
    {
      "username": "john_doe",
      "password": "secure_password"
    }
    ```
- Operation: Validates credentials, generates an OAuth token by communicating with the Authorization Server.
- Response:
    ```json
    {
      "access_token": "jwt_token",
      "expires_in": 3600,
      "token_type": "Bearer"
    }
    ```

### c) **Get User Info Endpoint** (`GET /users/{id}`)
- Request: `Authorization: Bearer <JWT Token>`
- Operation: Retrieves user information from the database using the `user_id` in the token.
- Response:
    ```json
    {
      "id": 1,
      "username": "john_doe",
      "email": "john.doe@example.com",
      "created_at": "2024-01-01T00:00:00"
    }
    ```

---

## Summary of User Service Flow:

1. **Registration**: The user submits their username, email, and password, which is stored in PostgreSQL after hashing the password.
2. **Login**: The user submits their credentials (username and password). The password is verified against the stored hash. If valid, a JWT token is issued.
3. **OAuth Token**: The JWT token is used for authentication in future API requests. The token validates the user’s identity and permissions.

This architecture ensures secure user management, handles login and registration processes, and integrates OAuth 2.0 for token-based security.
