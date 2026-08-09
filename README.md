# WaitWise

A Spring Boot backend for **WaitWise**, a restaurant queue and reservation management system. This project provides REST APIs for user authentication and will support restaurant management, reservations, waiting lists, and notifications.

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

```
src/main/java/com/waitwise/backend
│
├── controller
├── dto
├── entity
├── enums
├── exception
├── mapper
├── repository
├── security
├── service
├── util
└── BackendApplication.java
```

## 🚀 Features

- Customer Queue Management
- Staff Dashboard
- Admin Dashboard
- JWT Authentication
- Real-time Queue Updates
- Queue Analytics
- Department Management



### 👤 Customer

- User registration and login
- JWT-based authentication
- Browse available businesses
- Create appointments
- View appointments
- Join and manage queue entries
- View current queue position
- View estimated waiting time
- Track queue status
- Receive notifications
- Mark notifications as read

### 🏢 Business Owner

- Business owner registration request
- Business owner application approval workflow
- Business profile management
- View business dashboard
- Monitor customer queues
- View currently serving customer
- Call the next customer
- Complete the current customer
- Cancel queue entries
- Monitor appointment statistics

### 🛡️ Administrator

- Review business owner applications
- Approve business owner applications
- Reject business owner applications
- Manage users and business-related data
- Control business owner access


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

## 📌 Status

🚧 Under Development
