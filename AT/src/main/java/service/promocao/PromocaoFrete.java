package service.promocao;

import model.Entrega;

import java.math.BigDecimal;

public interface PromocaoFrete {
    BigDecimal aplicar(Entrega entrega, BigDecimal valorBase);
}
