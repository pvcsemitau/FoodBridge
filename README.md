# 🍃 FoodBridge — Smart Food Donation & Management System

<p align="center">
  <strong>Connecting Surplus Food with Those Who Need It.</strong>
  <br />
  A Java Spring Boot web application connecting restaurants and cafés with verified NGOs to reduce food waste and support food donation.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-Framework-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
  <img src="https://img.shields.io/badge/Thymeleaf-Template-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring_MVC-Web_Layer-6DB33F?style=flat-square&logo=spring&logoColor=white" alt="Spring MVC" />
  <img src="https://img.shields.io/badge/Spring_Data_JPA-ORM-6DB33F?style=flat-square&logo=spring&logoColor=white" alt="Spring Data JPA" />
  <img src="https://img.shields.io/badge/BCrypt-Password_Security-8E44AD?style=flat-square" alt="BCrypt Password Security" />
  <img src="https://img.shields.io/badge/HTML5-CSS3-E34F26?style=flat-square&logo=html5&logoColor=white" alt="HTML5 and CSS3" />
</p>

---

## 📑 Table of Contents

* [About the Project](#-about-the-project)
* [Key Features](#-key-features)
  * [Restaurant Portal](#-restaurant-portal)
  * [NGO Portal](#-ngo-portal)
  * [Admin Dashboard](#-admin-dashboard)
  * [Security & Architecture](#-security--architecture)
* [Donation Lifecycle Workflow](#-donation-lifecycle-workflow)
* [Technology Stack](#-technology-stack)
* [Application Architecture](#-application-architecture)
* [Getting Started](#-getting-started)
  * [Prerequisites](#-prerequisites)
  * [Installation & Setup](#-installation--setup)
* [Future Enhancements](#-future-enhancements)
* [Social Impact](#-social-impact)
* [Contributors](#-contributors)

  ---

## 🌍 About the Project

Food waste and food insecurity are significant challenges that affect communities worldwide. Restaurants and cafés often have safe surplus food that remains unused, while NGOs work to provide food to people in need.

**FoodBridge** is a web-based food donation and management platform developed using Java 17 and Spring Boot. It connects restaurants and cafés with verified NGOs, making it easier to publish, discover, accept, and manage surplus food donations.

The application includes role-based access, administrator verification, donation status tracking, and database-driven dashboard statistics to support an organized food donation process.

### 🎯 Our Mission

> Turn surplus food into meaningful support for communities in need.

## ✨ Key Features

### 🍽️ Restaurant Portal
- Restaurant registration and login.
- Publish surplus food donation listings.
- Provide food quantity, location, preparation time, and expiry time.
- Monitor donation status.

### 🤝 NGO Portal
- NGO registration and login.
- Administrator verification before accepting donations.
- Search and filter available food donations.
- Accept suitable donations.
- Track donation progress.

### 🛡️ Admin Dashboard
- Verify NGO accounts before they can accept donations.
- Review registered users and food donations.
- Monitor donation activity.
- View dashboard statistics calculated from database records.

### 🔐 Security and Data Management
- BCrypt password hashing for password protection.
- Role-based session login.
- Persistent data storage using MySQL.
- Database operations managed through Spring Data JPA.

## 🔄 Donation Workflow

<p align="center">
  <img src="https://img.shields.io/badge/1-AVAILABLE-2196F3?style=for-the-badge" alt="Available" />
  ➜
  <img src="https://img.shields.io/badge/2-ACCEPTED-8E44AD?style=for-the-badge" alt="Accepted" />
  ➜
  <img src="https://img.shields.io/badge/3-PICKED__UP-FF9800?style=for-the-badge" alt="Picked Up" />
  ➜
  <img src="https://img.shields.io/badge/4-COMPLETED-2E8B57?style=for-the-badge" alt="Completed" />
</p>

- **AVAILABLE:** The restaurant has published a food donation.
- **ACCEPTED:** An eligible, verified NGO has accepted the donation.
- **PICKED_UP:** The donation has been collected.
- **COMPLETED:** The donation workflow has been completed.
- **EXPIRED:** An available donation that has passed its expiry time is marked as expired.

This workflow helps restaurants, NGOs, and administrators monitor donation progress.

## 📊 System Architecture & Tech Stack

```text
+-------------------------------------------------------+
|                Presentation / Web Layer               |
|         (Thymeleaf / REST APIs, Controllers)          |
+-------------------------------------------------------+
                           |
                           ▼
+-------------------------------------------------------+
|                 Business Logic Layer                  |
|       (Spring Services, DTOs, Validation, Spring)     |
+-------------------------------------------------------+
                           |
                           ▼
+-------------------------------------------------------+
|               Data Access / JPA Layer                 |
|         (Spring Data JPA Repositories, Entities)      |
+-------------------------------------------------------+
                           |
                           ▼
+-------------------------------------------------------+
|                   Relational Database                 |
|             (MySQL 8.0: FoodBridge Schema)            |
+-------------------------------------------------------+

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Core programming language |
| Spring Boot | Application configuration and backend development |
| Spring MVC | Web request handling and controller logic |
| Thymeleaf | Dynamic HTML page rendering |
| HTML5 & CSS3 | User interface and styling |
| Spring Data JPA | Database access and entity management |
| MySQL 8+ | Persistent relational database |
| BCrypt | Password hashing |
| Maven | Dependency management and build automation |

## 🏗️ Application Architecture

FoodBridge uses a layered web application architecture.

- **Presentation Layer:** Thymeleaf templates, HTML, and CSS provide the user interface.
- **Controller Layer:** Spring MVC handles incoming web requests and routes them to the appropriate application logic.
- **Data Access Layer:** Spring Data JPA manages database operations.
- **Database Layer:** MySQL stores user accounts, restaurant and NGO details, food donations, and related records.

This separation helps keep the application's user interface, request handling, and data management organized.

## 🚀 Getting Started

### 📋 Prerequisites

Install the following software:

- Java Development Kit (JDK) 21 or later
- Apache Maven 3.9 or later
- MySQL 8 or later
- An IDE such as IntelliJ IDEA, Eclipse, or VS Code
- A modern web browser

### 1️⃣ Create the Database

Open MySQL and execute the following SQL command:

```sql
CREATE DATABASE foodbridge;
```

### 2️⃣ Clone the Repository

Replace the placeholder with your actual GitHub repository URL.

```bash
git clone <your-repository-url>
cd FoodBridge
```

## 🗄️ Database Management

FoodBridge uses MySQL for persistent data storage and Spring Data JPA for database access.

The database supports the application's main operations, including:

- User registration and role management.
- Restaurant and NGO account information.
- Food donation details and availability.
- Donation acceptance and status tracking.
- Administrator verification and review.
- Dashboard statistics derived from stored records.

The exact tables and relationships depend on the entity classes defined in the application.

## 🌱 Future Enhancements

Potential improvements to FoodBridge include:

- 📍 Location-based matching between restaurants and NGOs.
- ⏰ Automated food expiry alerts.
- 🚚 Food pickup scheduling and coordination.
- 📩 Email or SMS notifications for donation updates.
- 📊 Advanced donation analytics and impact reports.
- 📱 Enhanced mobile responsiveness and accessibility.

## 💚 Social Impact

FoodBridge demonstrates how technology can help organizations coordinate food donations and make better use of safe surplus food.

By connecting restaurants and cafés with verified NGOs, the platform aims to support more efficient food redistribution, reduce avoidable food waste, and encourage community collaboration.

## 👩‍💻 Contributors

Add the names of the students who contributed to the project.

---

<p align="center">
  <strong>🍃 FoodBridge — Connecting Food. Connecting People. Creating Impact.</strong>
  <br />
  <em>Less Waste. More Care. A Better Tomorrow.</em>
</p>
