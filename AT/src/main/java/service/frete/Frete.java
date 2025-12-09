package service.frete;


import model.Entrega;

import java.math.BigDecimal;

public interface Frete {
    BigDecimal calcular(Entrega entrega);
}