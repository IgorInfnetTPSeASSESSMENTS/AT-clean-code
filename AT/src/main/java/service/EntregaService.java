package service;

import model.Entrega;
import service.exceptions.application.ApplicationException;
import service.exceptions.domain.TipoFreteNaoSuportadoException;
import service.exceptions.domain.validation.EntregaValidationException;
import service.factory.FreteFactory;
import java.math.BigDecimal;
import java.util.Objects;

public class EntregaService {

    private final FreteFactory freteFactory;
    private final EtiquetaService etiquetaService;

    public EntregaService(FreteFactory freteFactory, EtiquetaService etiquetaService) {
        this.freteFactory = freteFactory;
        this.etiquetaService = etiquetaService;
    }

    /**
     * Calcula o frete da entrega.
     *
     * @param entrega objeto de entrega
     * @return valor do frete
     */
    public BigDecimal calcularFrete(Entrega entrega) {
        Objects.requireNonNull(entrega, "Entrega não pode ser nula ao calcular frete");

        try {
            if (entrega.isFreteGratis()) {
                return BigDecimal.ZERO;
            }
            return freteFactory.criar(entrega.getTipoFrete()).calcular(entrega);
        } catch (TipoFreteNaoSuportadoException | EntregaValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException("Erro inesperado ao calcular o frete", e);
        }
    }

    /**
     * Gera a etiqueta da entrega.
     *
     * @param entrega objeto de entrega
     * @return string com a etiqueta
     */
    public String gerarEtiqueta(Entrega entrega) {
        try {
            BigDecimal valorFrete = calcularFrete(entrega);
            return etiquetaService.gerarEtiqueta(entrega, valorFrete);
        } catch (Exception e) {
            throw new ApplicationException("Erro ao gerar etiqueta", e);
        }
    }

    /**
     * Gera um resumo simplificado da entrega.
     *
     * @param entrega objeto de entrega
     * @return string com resumo
     */
    public String gerarResumo(Entrega entrega) {
        try {
            BigDecimal valorFrete = calcularFrete(entrega);
            return etiquetaService.gerarResumo(entrega, valorFrete);
        } catch (Exception e) {
            throw new ApplicationException("Erro ao gerar resumo da entrega", e);
        }
    }

    public String gerarEtiquetaComFretePromocional(Entrega entrega, BigDecimal valorFrete) {
        try {
            return etiquetaService.gerarEtiqueta(entrega, valorFrete);
        } catch (Exception e) {
            throw new ApplicationException("Erro ao gerar etiqueta com valor personalizado", e);
        }
    }

    public String gerarResumoComFretePromocional(Entrega entrega, BigDecimal valorFrete) {
        try {
            return etiquetaService.gerarResumo(entrega, valorFrete);
        } catch (Exception e) {
            throw new ApplicationException("Erro ao gerar resumo com valor personalizado", e);
        }
    }
}
