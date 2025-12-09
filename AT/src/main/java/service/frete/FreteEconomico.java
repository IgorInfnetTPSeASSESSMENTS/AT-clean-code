package service.frete;


import model.Entrega;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FreteEconomico implements Frete {
    private static final BigDecimal TAXA_POR_KG = new BigDecimal("1.1");
    private static final BigDecimal DESCONTO_FIXO = new BigDecimal("5.0");

    @Override
    public BigDecimal calcular(Entrega entrega) {
        BigDecimal valor = entrega.getPeso()
                .multiply(TAXA_POR_KG)
                .subtract(DESCONTO_FIXO)
                .setScale(2, RoundingMode.HALF_UP);

        // Garante que não seja negativo
        return valor.max(BigDecimal.ZERO);
    }
}



