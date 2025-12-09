package model.validacoes.entrega;

import model.Entrega;
import service.exceptions.domain.validation.EntregaValidationException;

import java.math.BigDecimal;

public class ValidarPeso implements ValidacaoEntrega {
    @Override
    public void validar(Entrega entrega) {
        if (entrega.getPeso() == null || entrega.getPeso().compareTo(BigDecimal.ZERO) <= 0) {
            throw new EntregaValidationException("Peso inválido.");
        }
    }
}
