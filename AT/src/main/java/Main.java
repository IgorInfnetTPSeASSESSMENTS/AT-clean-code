import config.AppConfig;
import controller.EntregaController;
import model.Entrega;
import model.Pedido;
import model.TipoFrete;
import service.etiqueta.FormatoEtiquetaPadrao;
import service.exceptions.domain.validation.EntregaValidationException;
import service.factory.EntregaFactory;
import service.promocao.PromocaoDesconto10PorCento;
import service.promocao.PromocaoFrete;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        // Inicializa a aplicação
        AppConfig appConfig = new AppConfig(new FormatoEtiquetaPadrao());
        EntregaController controller = appConfig.entregaController();
        EntregaFactory factory = appConfig.entregaFactory(); // fábrica com validações

        // ===============================
        // 1. Testes de entregas válidas
        // ===============================
        System.out.println("=== Teste: entregas válidas ===");
        Entrega entregaPadrao = factory.criar("Rua A, 10", "Alice", new BigDecimal("5"), TipoFrete.PADRAO);
        Entrega entregaExpresso = factory.criar("Rua B, 20", "Bob", new BigDecimal("3"), TipoFrete.EXPRESSO);
        Entrega entregaEconomicoBaixo = factory.criar("Rua C, 30", "Carol", new BigDecimal("1.5"), TipoFrete.ECONOMICO);
        Entrega entregaEconomicoAlto = factory.criar("Rua D, 40", "Daniel", new BigDecimal("50"), TipoFrete.ECONOMICO);

        // Criando pedidos
        Pedido pedido1 = new Pedido(entregaPadrao);
        Pedido pedido2 = new Pedido(entregaExpresso);
        Pedido pedido3 = new Pedido(entregaEconomicoBaixo);
        Pedido pedido4 = new Pedido(entregaEconomicoAlto);

        Pedido[] pedidos = {pedido1, pedido2, pedido3, pedido4};

        // Criando promoção
        PromocaoFrete promocao10 = new PromocaoDesconto10PorCento();

        for (Pedido p : pedidos) {
            Entrega e = p.getEntrega();

            // Calcula frete normalmente
            BigDecimal freteNormal = controller.calcularFrete(e);

            // Aplica promoção
            BigDecimal freteComDesconto = promocao10.aplicar(e, freteNormal);

            System.out.println("ID do pedido: " + p.getId());
            System.out.println(controller.gerarResumo(e));
            System.out.println(controller.gerarEtiqueta(e));
            System.out.println("Frete normal: " + freteNormal);
            System.out.println("Frete com promoção 10%: " + freteComDesconto);

            // Exibe resumo e etiqueta com valor promocional
            System.out.println("Resumo com promoção: " + controller.gerarResumoComPromocao(e, freteComDesconto));
            System.out.println("Etiqueta com promoção:\n" + controller.gerarEtiquetaComPromocao(e, freteComDesconto));

            System.out.println("Frete grátis? " + e.isFreteGratis());
            System.out.println("---------------------------");
        }

        // ===============================
        // 2. Testes de validação de domínio
        // ===============================
        System.out.println("=== Teste: erros de validação ===");

        testarErro(() -> factory.criar(null, "Alice", new BigDecimal("1"), TipoFrete.PADRAO), "Endereço nulo");
        testarErro(() -> factory.criar("  ", "Alice", new BigDecimal("1"), TipoFrete.PADRAO), "Endereço vazio");
        testarErro(() -> factory.criar("AB", "Alice", new BigDecimal("1"), TipoFrete.PADRAO), "Endereço curto");

        testarErro(() -> factory.criar("Rua A, 10", null, new BigDecimal("1"), TipoFrete.PADRAO), "Destinatário nulo");
        testarErro(() -> factory.criar("Rua A, 10", "  ", new BigDecimal("1"), TipoFrete.PADRAO), "Destinatário vazio");

        testarErro(() -> factory.criar("Rua A, 10", "Alice", null, TipoFrete.PADRAO), "Peso nulo");
        testarErro(() -> factory.criar("Rua A, 10", "Alice", new BigDecimal("0"), TipoFrete.PADRAO), "Peso zero");
        testarErro(() -> factory.criar("Rua A, 10", "Alice", new BigDecimal("-1"), TipoFrete.PADRAO), "Peso negativo");

        testarErro(() -> factory.criar("Rua A, 10", "Alice", new BigDecimal("1"), null), "Tipo de frete nulo");

        testarErro(() -> new Pedido(null), "Pedido com entrega nula");

        // ===============================
        // 3. Testes de frete econômico negativo
        // ===============================
        System.out.println("=== Teste: frete econômico nunca negativo ===");
        Entrega entregaEconomicoMuitoBaixo = factory.criar("Rua Z, 99", "Zeca", new BigDecimal("0.5"), TipoFrete.ECONOMICO);
        System.out.println("Frete calculado: " + controller.calcularFrete(entregaEconomicoMuitoBaixo)); // Deve ser 0
    }

    private static void testarErro(Runnable runnable, String descricao) {
        try {
            runnable.run();
            System.out.println("[ERRO] " + descricao + " não lançou exceção!");
        } catch (EntregaValidationException | NullPointerException e) {
            System.out.println("[OK] " + descricao + " lançou exceção: " + e.getMessage());
        }
    }
}
