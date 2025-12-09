package controller;


import model.Entrega;
import service.EntregaService;

import java.math.BigDecimal;

public class EntregaController {

    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    // Métodos originais continuam intactos
    public String gerarResumo(Entrega entrega) {
        return entregaService.gerarResumo(entrega);
    }

    public String gerarEtiqueta(Entrega entrega) {
        return entregaService.gerarEtiqueta(entrega);
    }

    public BigDecimal calcularFrete(Entrega entrega) {
        return entregaService.calcularFrete(entrega);
    }

    // Novos métodos opcionais para usar valor promocional
    public String gerarResumoComPromocao(Entrega entrega, BigDecimal valorFrete) {
        return entregaService.gerarResumoComFretePromocional(entrega, valorFrete);
    }

    public String gerarEtiquetaComPromocao(Entrega entrega, BigDecimal valorFrete) {
        return entregaService.gerarEtiquetaComFretePromocional(entrega, valorFrete);
    }
}