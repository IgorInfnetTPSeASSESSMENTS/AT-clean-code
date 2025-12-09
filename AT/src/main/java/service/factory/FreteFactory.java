package service.factory;

import model.TipoFrete;
import service.exceptions.domain.TipoFreteNaoSuportadoException;
import service.frete.Frete;
import service.frete.FreteEconomico;
import service.frete.FreteExpresso;
import service.frete.FretePadrao;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;


public class FreteFactory {

    private static final Map<TipoFrete, Supplier<Frete>> REGISTRY = Map.of(
            TipoFrete.PADRAO, FretePadrao::new,
            TipoFrete.EXPRESSO, FreteExpresso::new,
            TipoFrete.ECONOMICO, FreteEconomico::new
    );

    public Frete criar(TipoFrete tipo) {

        Supplier<Frete> fornecedor = Optional.ofNullable(REGISTRY.get(tipo))
                .orElseThrow(() -> new TipoFreteNaoSuportadoException(tipo.name()));

        return fornecedor.get();
    }
}

