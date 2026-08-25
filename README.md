# WaitWise


### Smart Queue & Appointment Management Platform

WaitWise is a smart queue and appointment management platform designed to reduce physical waiting time and improve the customer experience for businesses.

Instead of customers physically waiting in long queues, WaitWise allows them to discover businesses, book appointments, join queues, monitor their position, receive estimated waiting times, and get notified when their turn is approaching.

For businesses, WaitWise provides tools to manage appointments, organize customer queues, monitor daily activity, and gain insights through dashboards.



## Tech Stack

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (JSON Web Token)
- Maven
- Lombok

## Project Structure

```text
waitwise/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com.waitwise.backend/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── enums/
│       │       ├── exception/
│       │       ├── repository/
│       │       ├── security/
│       │       └── service/
│       │
│       └── resources/
│           └── application.properties
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```
---

## 🚀 Features

- Customer Queue Management
- Staff Dashboard
- Admin Dashboard
- JWT Authentication
- Real-time Queue Updates
- Queue Analytics
- Department Management


## 👥 User Roles

WaitWise is designed around three primary roles.

### 👤 Customer

- Register, log in, and manage their profile
- Browse businesses and view business information
- Book appointments and join queues
- Track queue position, waiting time, and appointment status
- Receive notifications and view their activity

---

### 🏢 Business Owner

- Apply for and manage an approved business account
- Create and manage business information
- Manage appointments and customer queues
- Call, serve, complete, or cancel queue entries
- Monitor business activity and performance through dashboards

---

### 🛡️ Administrator

- Manage users and registered businesses
- Review and verify business owner applications
- Approve or reject business owner requests
- Monitor platform activity and system operations
- Maintain platform security and system integrity


---

## 🛠 Tech Stack

### Frontend
- React (Vite)
- Tailwind CSS
- React Router
- Axios

### Backend
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT Authentication
- WebSocket


---


