ALTER TABLE payments
ADD COLUMN razorpay_order_id VARCHAR(255);

ALTER TABLE payments
ADD COLUMN razorpay_payment_id VARCHAR(255);

ALTER TABLE payments
ADD COLUMN razorpay_signature VARCHAR(500);