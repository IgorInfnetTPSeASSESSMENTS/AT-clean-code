package model;

import model.validacoes.entrega.ValidacaoEntrega;
import model.validacoes.entrega.ValidarEndereco;
import model.validacoes.entrega.ValidarDestinatario;
import model.validacoes.entrega.ValidarPeso;
import model.validacoes.entrega.ValidarTipoFrete;

import java.math.BigDecimal;
import java.util.List;

public final class Entrega {

    private final String endereco;
    private final String destinatario;
    private final BigDecimal peso;
    private final TipoFrete tipoFrete;

    private static final BigDecimal LIMITE_FRETE_GRATIS = new BigDecimal("2.0");

    public Entrega(
            String endereco,
            String destinatario,
            BigDecimal peso,
            TipoFrete tipoFrete,
            List<ValidacaoEntrega> validacoes
    ) {
        this.endereco = endereco;
        this.destinatario = destinatario;
        this.peso = peso;
        this.tipoFrete = tipoFrete;

        // Executa todas as validações recebidas
        if (validacoes != null) {
            validacoes.forEach(v -> v.validar(this));
        }
    }

    public boolean isFreteGratis() {
        return tipoFrete == TipoFrete.ECONOMICO && peso.compareTo(LIMITE_FRETE_GRATIS) < 0;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public TipoFrete getTipoFrete() {
        return tipoFrete;
    }
}
