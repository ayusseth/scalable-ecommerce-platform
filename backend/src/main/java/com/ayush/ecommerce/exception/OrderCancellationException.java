package com.ayush.ecommerce.exception;

public class OrderCancellationException extends RuntimeException{
    public OrderCancellationException(String message){
        super(message);
    }
}
