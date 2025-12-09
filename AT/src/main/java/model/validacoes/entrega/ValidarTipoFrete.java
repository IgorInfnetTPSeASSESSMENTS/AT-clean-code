package model.validacoes.entrega;

import model.Entrega;
import service.exceptions.domain.validation.EntregaValidationException;

public class ValidarTipoFrete implements ValidacaoEntrega {
    @Override
    public void validar(Entrega entrega) {
        if (entrega.getTipoFrete() == null) {
            throw new EntregaValidationException("Tipo de frete inválido.");
        }
    }
}
