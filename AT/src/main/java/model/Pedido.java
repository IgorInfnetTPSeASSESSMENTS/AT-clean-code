package model;

import java.util.UUID;
import java.util.Objects;

public final class Pedido {

    private final String id;
    private final Entrega entrega;

    public Pedido(Entrega entrega) {
        this.entrega = Objects.requireNonNull(entrega, "Entrega não pode ser nula");
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public Entrega getEntrega() {
        return entrega;
    }
}
