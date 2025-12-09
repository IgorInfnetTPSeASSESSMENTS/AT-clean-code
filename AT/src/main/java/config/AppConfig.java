package config;

import controller.EntregaController;
import service.factory.EntregaFactory;
import service.EntregaService;
import service.EtiquetaService;
import service.etiqueta.FormatoEtiqueta;
import service.factory.FreteFactory;

public class AppConfig {

    private final FreteFactory freteFactory;
    private final EtiquetaService etiquetaService;
    private final EntregaService entregaService;
    private final EntregaController entregaController;
    private final EntregaFactory entregaFactory;

    public AppConfig(FormatoEtiqueta formatoEtiqueta) {
        this.freteFactory = new FreteFactory();
        this.etiquetaService = new EtiquetaService(formatoEtiqueta);
        this.entregaService = new EntregaService(freteFactory, etiquetaService);
        this.entregaController = new EntregaController(entregaService);
        this.entregaFactory = new EntregaFactory(); // fábrica com validações
    }

    public EntregaController entregaController() {
        return entregaController;
    }

    public EntregaFactory entregaFactory() {
        return entregaFactory;
    }
}
