# 🚀 S4.02 — REST API with Spring Boot (Multi-Module Project)

## 📝 Project Overview

This project consists of three independent Spring Boot applications, each organized as a separate Maven module.

Each module implements a full CRUD API using different databases and progressively more advanced architecture:

- Level 1 → H2 (in-memory database)
- Level 2 → MySQL (relational database with relationships)
- Level 3 → MongoDB (DDD + Hexagonal Architecture)

---

## 📦 Project Structure (Multi-Module)

root  
 ├── nivel-1-h2  
 ├── nivel-2-mysql  
 └── nivel-3-mongodb  

Each module contains its own:

- pom.xml  
- Controllers  
- Services  
- Repositories  
- Configuration  
- Tests  

---

# 🔹 Level 1 — CRUD with H2 (Fruit Management API)

## 🎯 Objective

Develop a REST API to manage fruit stock using an in-memory H2 database.

---

## 🧩 Features

- Create fruit  
- Get all fruits  
- Get fruit by ID  
- Update fruit  
- Delete fruit  

---

## 📁 Main Entity

Fruit  
- Long id  
- String name  
- int weightInKilos  

---

## 🛠 Technologies Used

- Java 17 / 21  
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- H2 Database  
- Bean Validation  
- DTO pattern  
- Global Exception Handling  
- JUnit 5  
- MockMvc / Mockito  
- Docker (multi-stage build)  

---

# 🔹 Level 2 — CRUD with MySQL (Fruits + Providers)

## 🎯 Objective

Extend Level 1 by introducing providers and a relational model using MySQL.

---

## 🧩 Features

- Create provider  
- List providers  
- Update provider  
- Delete provider (only if no associated fruits)  
- Create fruit linked to provider  
- Filter fruits by provider  

---

## 📁 Entities

Provider  
- Long id  
- String name  
- String country  

Fruit  
- Long id  
- String name  
- int weightInKilos  
- Provider provider  

---

## 🛠 Technologies Used

- Java 17 / 21  
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- MySQL Driver  
- Bean Validation  
- DTO pattern  
- Docker  
- Docker Compose (MySQL)  
- JUnit 5  
- MockMvc / Mockito  

---

# 🔹 Level 3 — CRUD with MongoDB (Fruit Orders)

## 🎯 Objective

Manage fruit orders placed by clients using MongoDB with a clean architecture approach.

---

## 🧱 Architecture

This module follows:

- Domain-Driven Design (DDD)  
- Hexagonal Architecture  

Layers:

- Domain → Entities and business logic  
- Application → Use cases  
- Infrastructure → REST controllers, Mongo repositories  
- Configuration → Beans and adapters  

---

## 🧩 Features

- Create order  
- Get all orders  
- Get order by ID  
- Update order  
- Delete order  

---

## 📁 Main Document

Order  
- String id  
- String clientName  
- LocalDate deliveryDate  
- List of OrderItem  

OrderItem  
- String fruitName  
- int quantityInKilos  

---

## ✅ Validations

- Minimum delivery date: next day  
- At least one item per order  
- Positive quantities  

---

## 🛠 Technologies Used

- Java 17 / 21  
- Spring Boot  
- Spring Web  
- Spring Data MongoDB  
- Bean Validation  
- DDD  
- Hexagonal Architecture  
- JUnit 5  

---

# 🚀 Skills Developed

- REST API design  
- Spring Boot and Spring Data (JPA and MongoDB)  
- DTOs and validation  
- Global exception handling  
- Test-Driven Development (TDD)  
- Entity relationships  
- Dockerization (multi-stage builds)  
- Clean Architecture (DDD + Hexagonal)  

---

## 🛠️ Installation

Clone this repository:
```git
git clone https://github.com/cristhianchulca49/S4.02.Api-Rest-With-Spring-Boot.git
```

--- 

## 🤝 Contributions are welcome! 
Please follow these steps to contribute:
  
- Fork the repository Create a new branch: git checkout -b feature/NewFeature 
- Make your changes and commit them: git commit -m 'Add New Feature' 
- Push the changes to your branch: git push origin feature/NewFeature 
- Open a Pull Request

