# FoodBridge — Java Spring Boot Mini Project

FoodBridge connects restaurants/cafés that have safe surplus food with verified NGOs that can collect and distribute it.

## Technology
* Java 17, Spring Boot, Spring MVC
* Thymeleaf + HTML/CSS (GUI)
* Spring Data JPA + MySQL (persistent database)
* BCrypt password hashing

## Features
* Restaurant and NGO registration; role-based session login
* NGO accounts require administrator verification before accepting donations
* Restaurants publish food donations with quantity, location, preparation and expiry times
* NGOs search/filter available donations and accept them
* Donation workflow: AVAILABLE → ACCEPTED → PICKED_UP → COMPLETED; expired available donations are marked EXPIRED
* Admin dashboard verifies NGOs and reviews users/donations
* Dashboard statistics are calculated from database records

## Run
1. Install JDK 17+, Maven 3.9+, and MySQL 8+.
2. Create a database:
   ```sql
   CREATE DATABASE foodbridge;