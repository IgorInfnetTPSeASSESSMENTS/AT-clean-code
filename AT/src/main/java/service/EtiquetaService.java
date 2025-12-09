package service;

import model.Entrega;
import service.etiqueta.FormatoEtiqueta;
import service.exceptions.application.ApplicationException;
import service.exceptions.domain.validation.EntregaValidationException;

import java.math.BigDecimal;

public class EtiquetaService {

    private final FormatoEtiqueta formato;

    public EtiquetaService(FormatoEtiqueta formato) {
        this.formato = formato;
    }

    public String gerarEtiqueta(Entrega entrega, BigDecimal valorFrete) {
        validarParametros(entrega, valorFrete);
        try {
            return formato.gerarEtiqueta(entrega, valorFrete);
        } catch (Exception e) {
            throw new ApplicationException("Erro inesperado ao gerar etiqueta", e);
        }
    }

    public String gerarResumo(Entrega entrega, BigDecimal valorFrete) {
        validarParametros(entrega, valorFrete);
        try {
            return formato.gerarResumo(entrega, valorFrete);
        } catch (Exception e) {
            throw new ApplicationException("Erro inesperado ao gerar resumo", e);
        }
    }

    private void validarParametros(Entrega entrega, BigDecimal valorFrete) {
        if (entrega == null) {
            throw new EntregaValidationException("Entrega não pode ser nula ao gerar etiqueta/resumo");
        }
        if (valorFrete == null) {
            throw new EntregaValidationException("Valor do frete não pode ser nulo");
        }
    }
}
