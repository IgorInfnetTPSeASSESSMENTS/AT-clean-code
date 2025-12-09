package service.exceptions.domain.validation;

import service.exceptions.domain.DomainException;

public class EntregaValidationException extends DomainException {

    private final String field;
    private final Object value;

    public EntregaValidationException(String message) {
        super(message);
        this.field = null;
        this.value = null;
    }

    public EntregaValidationException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
        this.value = null;
    }

    public EntregaValidationException(String field, Object value, String detail) {
        super(format(field, value, detail));
        this.field = field;
        this.value = value;
    }

    private static String format(String field, Object value, String detail) {
        return "[Entrega] Campo '%s' inválido (valor='%s'): %s"
                .formatted(field, value, detail);
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}

