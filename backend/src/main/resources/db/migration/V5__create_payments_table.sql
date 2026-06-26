CREATE TABLE payments
(
    id BIGSERIAL PRIMARY KEY,

    payment_reference VARCHAR(255) NOT NULL UNIQUE,

    order_id BIGINT NOT NULL UNIQUE,

    amount NUMERIC(19,2) NOT NULL,

    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE CASCADE
);