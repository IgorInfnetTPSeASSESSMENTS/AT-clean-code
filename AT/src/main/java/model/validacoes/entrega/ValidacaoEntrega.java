package model.validacoes.entrega;

import model.Entrega;
import service.exceptions.domain.validation.EntregaValidationException;

public interface ValidacaoEntrega {
    void validar(Entrega entrega) throws EntregaValidationException;
}
