1. System Architecture
- Modular Monoloith
---
2. Component Diagram
graph LR

User[User Browser]

    User --> Frontend

    Frontend[React]

    Frontend --> Nginx

    Nginx --> Backend

    Backend[Spring Boot]

    Backend --> PostgreSQL
    Backend --> Redis

    Backend --> Kafka

    Kafka --> NotificationService

    NotificationService --> EmailProvider

    Backend --> PaymentGateway

    Backend --> Prometheus

    Prometheus --> Grafana
    Backend --> ELK

---
3. Authentication Flow

graph LR

User[User Browser]

    User --> Frontend

    Frontend[React]

    Frontend --> POST/auth/login

    POST/auth/login --> Spring-Security


    Spring-Security --> AuthenticationManager
    AuthenticationManager --> Database

    Database --> JWT-Token

    JWT-Token --> Frontend

---
## Subsequent Request

graph LR

    JWT-Token --> Authorization-Header

    Authorization-Header --> JWT-Filter

    JWT-Filter --> Token-Validation
    Token-Validation --> Security-Context
    Security-Context --> Controller-Access

---
4. Product Fetch Flow

flowchart LR

    Client[Client] --> API[Spring Boot API]

    API --> Redis[(Redis Cache)]

    Redis --> Decision{Data Found?}

    Decision -->|Yes| Response[Return Response]

    Decision -->|No| DB[(PostgreSQL)]
    DB --> CacheUpdate[Update Redis Cache]
    CacheUpdate --> Response
---
5. Checkout Flow

flowchart TD

    A[Validate Cart] --> B[Create Order]
    B --> C[Reserve Inventory]
    C --> D[Process Payment]
    D --> E[Update Order Status]
    E --> F[Publish Order Event]
    F --> G[Send Notification]

---
6. Cahce Flow
## Caheable Data

    Products
    Categories
    Product details

---
## Not Cacheable

    Payments
    Orders
    User Balence

--- 
# Flow
## Cacheing Strategy
- Cache Aside Pattern
---
    Read Redis
      ↓
    Miss
      ↓
    Read DB
      ↓
    Write Redis
      ↓
    Return Response

---
7. Deployment Architecture
# Development

    React
    Spring Boot
    PostgreSQL
    Redis
    Docker Compose

---
# Production

    Internet
      ↓
    Load Balancer
      ↓
    Nginx
      ↓
    Spring Boot Containers
      ↓
    PostgreSQL
    Redis
    Kafka

---
8. Monitoring Architecture
# Metrics

    Spring Boot
       ↓
    Micrometer
       ↓
    Prometheus
       ↓
    Grafana

---
## Track
- API Latency
- request count
- error rate
- memory usage
- CPU usage
## Logs

    Application Logs
       ↓
    Logstash
       ↓
    Elasticsearch
       ↓
    Kibana

---
10. Failure Strategy
## Scenario 1
- Payment Success
- Order failed
- Solution: Compensation logic (Refund payment)
## Scenario 2
- Redis Down
- Solution: Fallback to DB (Application still works)
## Scenario 3
- Email Service Down
- Solution: Retry thorugh kafka (Order still succeeds)
