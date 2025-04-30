package org.example.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ResourceNotFoundException  extends Throwable  {
    private String message;
    public ResourceNotFoundException(String message) {
        this.message = message;
    }
    public ResourceNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
