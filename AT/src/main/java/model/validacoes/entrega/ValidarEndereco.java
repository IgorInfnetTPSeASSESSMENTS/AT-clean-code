package model.validacoes.entrega;

import model.Entrega;
import service.exceptions.domain.validation.EntregaValidationException;

public class ValidarEndereco implements ValidacaoEntrega {
    @Override
    public void validar(Entrega entrega) {
        String endereco = entrega.getEndereco();
        if (endereco == null || endereco.trim().isBlank() || endereco.trim().length() < 3) {
            throw new EntregaValidationException("Endereço inválido.");
        }
    }
}
