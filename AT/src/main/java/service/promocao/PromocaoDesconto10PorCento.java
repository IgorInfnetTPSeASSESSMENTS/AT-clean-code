package service.promocao;

import model.Entrega;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PromocaoDesconto10PorCento implements PromocaoFrete {
    @Override
    public BigDecimal aplicar(Entrega entrega, BigDecimal valorBase) {
        return valorBase.multiply(new BigDecimal("0.9")).setScale(2, RoundingMode.HALF_UP);
    }
}
