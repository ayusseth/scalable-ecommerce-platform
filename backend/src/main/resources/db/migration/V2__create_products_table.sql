CREATE TABLE products
(
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    description TEXT,

    price NUMERIC(19,2) NOT NULL,

    stock_quantity INTEGER NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);