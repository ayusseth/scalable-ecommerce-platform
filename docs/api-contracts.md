# Authentication API
## Register
    POST /api/v1/auth/register

---
- request
    {
        "name":"Ayush",
        "email":"abc@gmail.com",
        "password":"password"
    }

---
- response
    {
        "message":"user register successfully"
    }

---

## Login
    POST /api/v1/auth/login

---
- request
    {
        "email":"abc@gmail.com",
        "password":"password"
    }

---

- response
    {
        "accessToken":"JWT-Token",
        "tokenType":"Bearer"
    }

---
# Product API
## Get Product

    GET /api/v1/products

---
## Supports

    ?page=0
    &size=10
    &sort=name

---

## Product Details

    GET /api/v1/products/{id}

---

## Create Products
- Admins only

    POST /api/v1/products

---

# Cart APIs
## Add products

    POST /api/v1/cart/items

---

- request
    {
        "productID":1,
        "quantity":2
    }

---

## View Cart

    GET /api/v1/cart

---
# Order APIs
## Chechkout
    POST /v1/order/checkout

---

- response
    {
        "orderId":123,
        "Status":"PENDING"
    }

---
## Order Details
    GET /api/v1/orders/{id}

---

# Payment APIs

    POST /v1/payments/process

---

# Standard Response Format
- Success
    {
    "success": true,
    "message": "Product created",
    "data": {}
    }

---
- Error
    {
    "success": false,
    "message": "Product not found",
    "timestamp": "2026-05-30T10:00:00"
    }

---

# Global Error Strategy
- Handle
    ResourceNotFoundException
    ValidationException
    AuthenticationException
    PaymentException

---