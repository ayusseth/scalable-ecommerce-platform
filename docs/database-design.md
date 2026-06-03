# Initial Tables
- users
- roles
- prodcuts
- categories
- cart
- cart_item
- orders
- order_items
- payment
# Table 1 - User
- id
- name
- email
- role_id
- created_at
- updated_at
# Contstraint
- email must be UNIQUE

# Table 2 - Roles
- id
- name
## Relationship

    One Role
      ↓
    Many Users

---
# Table 3 - Categories
- id
- name
- description
- created_at

# Table 4 - Products
- id
- name
- description
- price
- stock_quantity
- categories_id
- created_at
- updated_at

## Relationship

    One Category
       ↓
    Many Products

---
# Table 5 - Cart
- id
- user_id
- created_at
## Relationship

    One User
      ↓
    One Cart

---

# Table 6 - Cart_item
- id
- cart_id
- product_id
- quantity

## Relationship

    Cart
     ↓
    Many Cart Items

---
# Table 7 - Orders
- id
- user_id
- total_amount
- status
- created_at
## Status

    PENDING
    PAID
    SHIPPED
    DELIVERED
    CANCELLED

---

# Table 8 - Order item
- id
- order_id
- product_id
- quantity
- price

# Table 9 - Payment

- id
- order_id
- transaction_id
- amount
- status
- payment_provider
- created_at
## Status
    PENDING
    SUCCESS
    FAILED
    REFUNDED

---