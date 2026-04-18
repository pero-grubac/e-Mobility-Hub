<div align="center">

# 🚗🛵🚴 eMobilityHub

![MySQL](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-Frontend-red?logo=angular&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-Framework-6DB33F?logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-Enabled-green?logo=springsecurity&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-Views-orange?logo=java&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-UI-blueviolet?logo=bootstrap&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?logo=docker&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-Reverse_Proxy-009639?logo=nginx&logoColor=white)

</div>

## 📌 Project Overview

The **eMobilityHub** is a web-based platform designed for a fictional company specializing in renting electric vehicles such as **cars, bikes, and scooters**. The system is composed of multiple applications and functionalities, ensuring seamless rental management, vehicle tracking, and user administration.

---

## ✨ Features

### 🚘 Vehicle Management
- Electric Cars, Bikes, and Scooters with detailed specifications
- Fault Reporting and Rental & Availability Tracking
- Manufacturers data management
- Vehicle image storage and display

### 🔑 User Management
- Clients with registration and profile management
- Employees (Administrators, Operators, Managers) with role-based access

### 📅 Rental Process
- Rental details tracking including timestamps and locations
- Billing system with PDF invoice generation

### 📢 Promotions & Announcements
- Promotional campaigns and public announcements
- RSS Feed for updates

---

## 🏢 Employee Application (Angular)

### 🔑 Administrator
- Manage Vehicles and Manufacturers
- View rental history and breakdown records
- Manage Users and CSV file uploads

### 🔧 Operator
- View Rentals and Vehicle Locations on a Map
- Manage Clients and Report Vehicle Issues

### 📊 Manager
- Access all Administrator & Operator features
- View Statistics (Revenue, Breakdown Reports, etc.)
- Manage Rental Prices

![Employee App Demo](images/emobility-hub-employee.gif)

---

## 🛒 Client Application (JSP M2)
- Login / Registration
- Browse and rent Electric Cars, Bicycles and Scooters
- Report broken vehicles
- Rental history with PDF invoice download
- Profile Management and Avatar Upload

![Client App Demo](images/emobility-hub-user.gif)

---

## 🎯 Promotions Management Application (JSP)
- Managers Only: View, Search, Create, Update and Delete Promotions & Announcements
- Communicates with Spring Boot backend via REST API

![Promotions App Demo](images/emobility-hub-promotions.gif)

---

## 📌 Additional Functionalities
- Search across all listing pages
- Pagination for optimized data loading
- Consistent UI with Bootstrap and Angular Material
- Revenue Statistics, Breakdown Reports, Income by Vehicle Type

---

## 🐳 Docker Setup

### Prerequisites
- 🐳 Docker and Docker Compose installed
- 🐙 Git installed

### Architecture

```
Browser
  │
  ▼ http://localhost (80)
Nginx (reverse proxy)
  ├── /           → Angular Employee App
  └── /uploads/   → Vehicle images (proxied to backend)
  │
Spring Boot Backend (8080)
  │
MySQL (3306)

Tomcat - JSP Client App       (8081) → http://localhost:8081/user
Tomcat - JSP Promotions App   (8082) → http://localhost:8082/promotions
```

### Running with Docker

**1. Clone the repository**
```bash
git clone https://github.com/pero-grubac/e-Mobility-Hub.git
cd e-Mobility-Hub
```

**2. Configure environment variables**

Edit the `.env` file:
```env
# Database
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=emobility
MYSQL_USER=student
MYSQL_PASSWORD=student

# JWT signing key
JWT_SIGNING_KEY=your-secret-key-here
```

**3. Start all services**
```bash
docker compose up --build -d
```

**4. Access the applications**

| Application | URL |
|-------------|-----|
| Employee App (Angular) | http://localhost |
| Client App (JSP) | http://localhost:8081/user |
| Promotions App (JSP) | http://localhost:8082/promotions |
| Backend API | http://localhost:8080 |

---

## 🔑 Default Accounts

### Employee Application (http://localhost)

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Operator | operator | operator |
| Manager | manager | manager |

### Client Application (http://localhost:8081/user)

| Username | Password |
|----------|----------|
| client1 | operator |
| client2 | operator |

> New client accounts can also be registered directly through the Client Application.

### Promotions Application (http://localhost:8082/promotions)

| Username | Password |
|----------|----------|
| manager | manager |

---

## 📁 CSV Vehicle Import

The Employee Application supports bulk vehicle import via CSV files.

**Electric Car CSV format:**
```csv
uniqueIdentifier,manufacturerId,model,purchasePrice,rentPrice,purchaseDate,description
CAR-100,1,Model X,90000,60,2024-01-15 00:00:00,Tesla Model X Long Range
```

**Electric Bicycle CSV format:**
```csv
uniqueIdentifier,manufacturerId,model,purchasePrice,rentPrice,rangePerCharge
BIC-100,3,Mi Bike Pro,1500,7,120
```

**Electric Scooter CSV format:**
```csv
uniqueIdentifier,manufacturerId,model,purchasePrice,rentPrice,maxSpeed
SCO-100,3,Mi Scooter 4,600,4,30
```

> Note: `purchaseDate` must be in `yyyy-MM-dd HH:mm:ss` format. The `image` column should be omitted.

---

## 🛠️ Useful Commands

```bash
# View logs
docker compose logs -f backend
docker compose logs -f frontend
docker compose logs -f jsp-user
docker compose logs -f jsp-promotions

# Stop all containers (keeps database data)
docker compose down

# Stop and delete database (resets all data)
docker compose down -v

# Rebuild a single service
docker compose up --build backend -d
docker compose up --build frontend -d
docker compose up --build jsp-user -d
docker compose up --build jsp-promotions -d
```