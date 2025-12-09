package service.etiqueta;

import model.Entrega;

import java.math.BigDecimal;

public interface FormatoEtiqueta {
    String gerarEtiqueta(Entrega entrega, BigDecimal valorFrete);
    String gerarResumo(Entrega entrega, BigDecimal valorFrete);
}

