package service.frete;

import model.Entrega;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FreteExpresso implements Frete {
    private static final BigDecimal TAXA_POR_KG = new BigDecimal("1.5");
    private static final BigDecimal TAXA_FIXA = new BigDecimal("10.0");

    @Override
    public BigDecimal calcular(Entrega entrega) {
        return entrega.getPeso()
                .multiply(TAXA_POR_KG)
                .add(TAXA_FIXA)
                .setScale(2, RoundingMode.HALF_UP);
    }
}

