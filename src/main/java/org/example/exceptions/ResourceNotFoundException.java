package org.example.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ResourceNotFoundException extends RuntimeException  {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }

}
