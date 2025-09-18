# Banking Microservices 🚀

A demo **banking system** built using a **microservices architecture** with Spring Boot and Spring Cloud.  
The project showcases industry best practices like **database-per-service, service discovery, centralized configuration, fault tolerance, and rate limiting**.

---

## 🔑 Features
- **Customer Service & Account Service** – Independent microservices, each with its own **MySQL database**, following the **database-per-service pattern**.  
- **Inter-Service Communication** – Implemented using **Feign Client** with **Eureka Server** for service discovery and load balancing.  
- **Centralized Configuration** – Managed with **Spring Cloud Config Server**, backed by GitHub for version-controlled configs.  
- **Fault Tolerance & Resilience** – Added **Resilience4j Circuit Breaker** and **fallback mechanisms** for graceful degradation during failures.  
- **Rate Limiting** – Implemented request throttling to protect against DDoS-style traffic and ensure scalability.  

---

## 🛠️ Tech Stack
- **Backend:** Spring Boot, Spring Cloud, Hibernate  
- **Resilience & Scalability:** Resilience4j (Circuit Breaker, Rate Limiter), Eureka Server, Feign Client  
- **Configuration Management:** Spring Cloud Config Server (GitHub-backed)  
- **Database:** MySQL (separate DB per microservice)  
- **Build Tool:** Maven  

---
