package com.sharp.sharpshap.exceptions;

public class RefreshTokenNotFound extends RuntimeException{
    public RefreshTokenNotFound(String message){
        super(message);
    }
}
