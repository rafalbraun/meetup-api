package org.example.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ValidationException extends Throwable {
    String message;
    public ValidationException(String message) {this.message = message;}
    public ValidationException(String message, Object... args) {
        super(String.format(message, args));
    }
}
