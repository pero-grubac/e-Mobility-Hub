# 🚗🛵🚴 eMobilityHub

## 📌 Project Overview
The **eMobilityHub** is a web-based platform designed for a fictional company specializing in renting electric vehicles such as **cars, bikes, and scooters**. The system is composed of multiple applications and functionalities, ensuring seamless rental management, vehicle tracking, and user administration.

### 🏗️ Technologies Used
- **Backend:** Spring Boot 🚀, Spring Security 🔐, RESTful API 🌐, JWT Authentication 🔑
- **Frontend:** Angular ⚡, Angular Material 🎨, Bootstrap 🎭, HTML 🖥️, CSS 🎨
- **Database:** MySQL 🗄️
- **Additional Services:** RSS Generator 📢, RSS Feeds 🔄, JSP M2 📄, Mockaroo 📊
- **Architecture:** MVC 🏛️

---
## ✨ Features
### 🚘 Vehicle Management
- **Electric Cars**: ID, purchase date, cost, manufacturer, model, description.
- **Electric Bikes**: ID, manufacturer, model, cost, battery autonomy.
- **Electric Scooters**: ID, manufacturer, model, cost, max speed.
- **Fault Reporting**: Vehicles can break down, and reports include the reason and timestamp.
- **Rental & Availability Tracking**: Identify whether a vehicle is **rented** or **broken** at any time.
- **Manufacturers**: Store details including name, country, address, and contact information.
- **Vehicle Images**: Each vehicle has an associated image.

### 🔑 User Management
- **Clients** (users renting vehicles): Account creation with **name, ID card number, email, phone number, and avatar**.
- **Employees**:
  - **Administrators**: Full system access.
  - **Operators**: Limited access (view rentals, manage vehicles, track clients).
  - **Managers**: Access to statistics, pricing, and all administrator/operator features.

### 📅 Rental Process
- **Rental Details**: Includes date, time, user, start location, end location (GPS coordinates), and duration.
- **Car Rentals**: Require an **ID document (passport/ID card)** and a **driver’s license**.
- **Billing**: A **PDF invoice** is generated for users.

### 📢 Promotions & Announcements
- **Promotional campaigns** and **public announcements** can be created.
- **RSS Feed**: Public updates on promotions and announcements.

---
## 🏢 Employee Application
### 🔑 Administrator Features
- **Manage Vehicles**: Add, remove, and update vehicles from categorized lists.
- **View Vehicle Details**: View **rental history**, maintenance records, and breakdown reports.
- **Manage Manufacturers**: CRUD operations.
- **Manage Users**: View and edit both clients and employees. Client accounts can be **blocked/unblocked**.
- **Upload CSV Files**: Bulk import vehicle data.

### 🔧 Operator Features
- **View Rentals**: Read-only access.
- **View Vehicles on a Map**: Display vehicle locations.
- **Manage Clients**: Block/unblock client accounts.
- **Report Vehicle Issues**: Log maintenance requests.

### 📊 Manager Features
- **Access Administrator & Operator Features**.
- **View Statistics**:
  - **Total revenue per day (monthly overview).**
  - **Breakdown frequency by vehicle type.**
  - **Income statistics by vehicle category.**
- **Manage Rental Prices**.

---
## 🛒 Client Application
### 🔑 User Access
- **Login / Registration**.
- **Main Page Options**:
  - **Rent an Electric Car** 🚗
  - **Rent an Electric Bike** 🚴
  - **Rent an Electric Scooter** 🛵
  - **View Profile** 🧑‍💼
- **Rental Process**:
  - **Choose Vehicle**.
  - **Enter Location (Manual or Auto-detect)**.
  - **Confirm Payment (Card-based)**.
  - **Start Ride**.
  - **Live Ride Tracking**: Display **current fare** and **ride duration**.
  - **End Ride**: Finalize rental and **generate invoice**.
- **Profile Management**:
  - **Change Password**.
  - **Deactivate Account**.
  - **View Rental History**.

🚀 **Client Application Implementation:** JSP M2 (Without RESTful Services) with a **mobile-first** design.

---
## 🎯 Promotions Management Application
- **Accessible by Managers Only**.
- **View and Search Promotions**.
- **Create New Promotions and Announcements**.
- **JSP-based implementation**.

---
## 📌 Additional Functionalities
### 🛠️ General Features
- **Search**: Implemented on all listing pages.
- **Pagination / Virtual Scroll**: Ensures efficient data loading.
- **Consistent UI**: Uniform design across all applications using **Bootstrap / Angular Material**.

### 📊 Graphical Reports
- **Revenue per day (monthly graph)** 📈
- **Breakdown count per vehicle** 🔧
- **Revenue by vehicle type** 💰
