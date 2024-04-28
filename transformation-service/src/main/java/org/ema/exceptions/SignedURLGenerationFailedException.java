package org.ema.exceptions;

public class SignedURLGenerationFailedException extends RuntimeException {
    public SignedURLGenerationFailedException(String message) {
        super(message);
    }
}
