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
