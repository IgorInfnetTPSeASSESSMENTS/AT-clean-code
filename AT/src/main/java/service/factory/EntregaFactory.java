package service.factory;

import model.Entrega;
import model.TipoFrete;
import model.validacoes.entrega.*;

import java.math.BigDecimal;
import java.util.List;

public class EntregaFactory {

    private final List<ValidacaoEntrega> validacoes;

    public EntregaFactory() {
        this.validacoes = List.of(
                new ValidarEndereco(),
                new ValidarDestinatario(),
                new ValidarPeso(),
                new ValidarTipoFrete()
        );
    }

    public Entrega criar(String endereco, String destinatario, BigDecimal peso, TipoFrete tipoFrete) {
        return new Entrega(endereco, destinatario, peso, tipoFrete, validacoes);
    }
}
