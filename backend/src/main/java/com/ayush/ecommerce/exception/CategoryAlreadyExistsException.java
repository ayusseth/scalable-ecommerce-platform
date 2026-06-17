package com.ayush.ecommerce.exception;

public class CategoryAlreadyExistsException extends RuntimeException{
    public CategoryAlreadyExistsException(String message){
        super(message);
    }
}
