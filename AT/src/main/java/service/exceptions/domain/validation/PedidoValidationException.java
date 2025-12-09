package service.exceptions.domain.validation;

import service.exceptions.domain.DomainException;

public class PedidoValidationException extends DomainException {

    private final String field;
    private final Object value;

    public PedidoValidationException(String message) {
        super(message);
        this.field = null;
        this.value = null;
    }

    public PedidoValidationException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
        this.value = null;
    }

    public PedidoValidationException(String field, Object value, String detail) {
        super(format(field, value, detail));
        this.field = field;
        this.value = value;
    }

    private static String format(String field, Object value, String detail) {
        return "[Pedido] Campo '%s' inválido (valor='%s'): %s"
                .formatted(field, String.valueOf(value), detail);
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}
