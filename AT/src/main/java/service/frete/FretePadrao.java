package service.frete;

import model.Entrega;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FretePadrao implements Frete {
    private static final BigDecimal TAXA_POR_KG = new BigDecimal("1.2");

    @Override
    public BigDecimal calcular(Entrega entrega) {
        return entrega.getPeso()
                .multiply(TAXA_POR_KG)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
