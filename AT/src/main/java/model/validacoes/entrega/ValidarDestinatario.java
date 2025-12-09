package model.validacoes.entrega;


import model.Entrega;
import service.exceptions.domain.validation.EntregaValidationException;

public class ValidarDestinatario implements ValidacaoEntrega {
    @Override
    public void validar(Entrega entrega) {
        String destinatario = entrega.getDestinatario();
        if (destinatario == null || destinatario.trim().isBlank()) {
            throw new EntregaValidationException("Destinatário inválido.");
        }
    }
}
