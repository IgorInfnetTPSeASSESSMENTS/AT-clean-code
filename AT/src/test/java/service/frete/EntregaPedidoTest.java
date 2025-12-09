package service.frete;

import model.Entrega;
import model.Pedido;
import model.TipoFrete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.EntregaService;
import service.EtiquetaService;

import service.etiqueta.FormatoEtiquetaPadrao;
import service.exceptions.domain.validation.EntregaValidationException;
import service.factory.EntregaFactory;
import service.factory.FreteFactory;
import service.promocao.PromocaoDesconto10PorCento;
import service.promocao.PromocaoFrete;


import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

public class EntregaPedidoTest {

    private EntregaFactory entregaFactory;
    private EntregaService entregaService;

    @BeforeEach
    public void setup() {
        FreteFactory freteFactory = new FreteFactory();
        EtiquetaService etiquetaService = new EtiquetaService(new FormatoEtiquetaPadrao());
        entregaService = new EntregaService(freteFactory, etiquetaService);
        entregaFactory = new EntregaFactory();
    }

    // =================================================
    // 1. Validação do domínio: Entrega
    // =================================================
    @Test
    public void entregaValidaDeveSerCriadaComSucesso() {
        Entrega entrega = entregaFactory.criar("Rua X, 123", "João", new BigDecimal("5"), TipoFrete.PADRAO);
        assertEquals("Rua X, 123", entrega.getEndereco());
        assertEquals("João", entrega.getDestinatario());
        assertEquals(new BigDecimal("5"), entrega.getPeso());
        assertEquals(TipoFrete.PADRAO, entrega.getTipoFrete());
    }

    // ---------- Endereço ----------
    @Test
    public void entregaComEnderecoNuloDeveLancarExcecao() {
        assertThrows(EntregaValidationException.class, () ->
                entregaFactory.criar(null, "João", new BigDecimal("1"), TipoFrete.ECONOMICO)
        );
    }

    @Test
    public void entregaComEnderecoVazioDeveLancarExcecao() {
        assertThrows(EntregaValidationException.class, () ->
                entregaFactory.criar("  ", "João", new BigDecimal("1"), TipoFrete.ECONOMICO)
        );
    }

    @Test
    public void entregaComEnderecoCurtoDeveLancarExcecao() {
        assertThrows(EntregaValidationException.class, () ->
                entregaFactory.criar("AB", "João", new BigDecimal("1"), TipoFrete.ECONOMICO)
        );
    }

    // ---------- Destinatário ----------
    @Test
    public void entregaComDestinatarioNuloDeveLancarExcecao() {
        assertThrows(EntregaValidationException.class, () ->
                entregaFactory.criar("Rua X, 123", null, new BigDecimal("1"), TipoFrete.ECONOMICO)
        );
    }

    @Test
    public void entregaComDestinatarioVazioDeveLancarExcecao() {
        assertThrows(EntregaValidationException.class, () ->
                entregaFactory.criar("Rua X, 123", "  ", new BigDecimal("1"), TipoFrete.ECONOMICO)
        );
    }

    // ---------- Peso ----------
    @Test
    public void entregaComPesoNuloDeveLancarExcecao() {
        assertThrows(EntregaValidationException.class, () ->
                entregaFactory.criar("Rua X, 123", "João", null, TipoFrete.ECONOMICO)
        );
    }

    @Test
    public void entregaComPesoZeroOuNegativoDeveLancarExcecao() {
        assertThrows(EntregaValidationException.class, () ->
                entregaFactory.criar("Rua X, 123", "João", new BigDecimal("0"), TipoFrete.ECONOMICO)
        );
        assertThrows(EntregaValidationException.class, () ->
                entregaFactory.criar("Rua X, 123", "João", new BigDecimal("-1"), TipoFrete.ECONOMICO)
        );
    }

    // ---------- Tipo de frete ----------
    @Test
    public void entregaComTipoFreteNuloDeveLancarExcecao() {
        assertThrows(EntregaValidationException.class, () ->
                entregaFactory.criar("Rua X, 123", "João", new BigDecimal("1"), null)
        );
    }

    // =================================================
    // 2. Validação do domínio: Pedido
    // =================================================
    @Test
    public void pedidoComEntregaNulaDeveLancarExcecao() {
        assertThrows(NullPointerException.class, () -> new Pedido(null));
    }

    @Test
    public void pedidoComEntregaValidaDeveTerIdGerado() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("5"), TipoFrete.PADRAO);
        Pedido pedido = new Pedido(entrega);
        assertNotNull(pedido.getId());
        assertEquals(entrega, pedido.getEntrega());
    }

    // =================================================
    // 3. Cálculo de frete
    // =================================================
    @Test
    public void calcularFretePadrao() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("2"), TipoFrete.PADRAO);
        BigDecimal frete = entregaService.calcularFrete(entrega);
        assertEquals(new BigDecimal("2.40").setScale(2), frete);
    }

    @Test
    public void calcularFreteExpresso() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("2"), TipoFrete.EXPRESSO);
        BigDecimal frete = entregaService.calcularFrete(entrega);
        assertEquals(new BigDecimal("13.00").setScale(2), frete);
    }

    @Test
    public void calcularFreteEconomicoComPesoBaixo() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("1.5"), TipoFrete.ECONOMICO);
        BigDecimal frete = entregaService.calcularFrete(entrega);
        assertEquals(BigDecimal.ZERO.setScale(2), frete.setScale(2));
    }

    @Test
    public void calcularFreteEconomicoComPesoAlto() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("50"), TipoFrete.ECONOMICO);
        BigDecimal frete = entregaService.calcularFrete(entrega);
        BigDecimal esperado = new BigDecimal("50.00").setScale(2);
        assertEquals(esperado, frete.setScale(2));
    }

    // ---------- Regra de frete grátis ----------
    @Test
    public void freteEconomicoComPesoBaixoDeveSerGratis() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("1.9"), TipoFrete.ECONOMICO);
        assertTrue(entrega.isFreteGratis());
    }

    @Test
    public void freteEconomicoComPesoAltoNaoDeveSerGratis() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("2.1"), TipoFrete.ECONOMICO);
        assertFalse(entrega.isFreteGratis());
    }

    @Test
    public void fretePadraoNuncaEhGratis() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("1"), TipoFrete.PADRAO);
        assertFalse(entrega.isFreteGratis());
    }

    @Test
    public void freteExpressoNuncaEhGratis() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("1"), TipoFrete.EXPRESSO);
        assertFalse(entrega.isFreteGratis());
    }

    // ---------- Segurança: frete econômico nunca negativo ----------
    @Test
    public void freteEconomicoNaoPodeSerNegativoMesmoComPesoMuitoBaixo() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("0.5"), TipoFrete.ECONOMICO);
        BigDecimal frete = entregaService.calcularFrete(entrega);
        assertEquals(BigDecimal.ZERO.setScale(2), frete.setScale(2));
    }

    // =================================================
    // 4. Etiqueta
    // =================================================
    @Test
    public void gerarEtiquetaNaoDeveRetornarNulo() {
        Entrega entrega = entregaFactory.criar("Rua X", "João", new BigDecimal("5"), TipoFrete.PADRAO);
        String etiqueta = entregaService.gerarEtiqueta(entrega);
        assertNotNull(etiqueta);
        assertTrue(etiqueta.contains("Rua X"));
    }

    // =================================================
    // 5. Teste de promoção de frete
    // =================================================
        @Test
        public void aplicarPromocaoDesconto10PorCento() {
            // Cria entrega normal
            Entrega entrega = entregaFactory.criar("Rua Promo, 99", "Lucas", new BigDecimal("10"), TipoFrete.PADRAO);

            // Calcula frete normal
            BigDecimal freteNormal = entregaService.calcularFrete(entrega);
            assertEquals(new BigDecimal("12.00").setScale(2), freteNormal);

            // Cria promoção
            PromocaoFrete promocao = new PromocaoDesconto10PorCento();

            // Aplica promoção sobre o valor do frete
            BigDecimal freteComDesconto = promocao.aplicar(entrega, freteNormal).setScale(2, RoundingMode.HALF_UP);
            assertEquals(new BigDecimal("10.80").setScale(2), freteComDesconto);

            // Gera resumo e etiqueta usando o valor com desconto
            String resumoComDesconto = entregaService.gerarResumoComFretePromocional(entrega, freteComDesconto);
            String etiquetaComDesconto = entregaService.gerarEtiquetaComFretePromocional(entrega, freteComDesconto);

            assertTrue(resumoComDesconto.contains("10.80"));
            assertTrue(etiquetaComDesconto.contains("10.80"));
        }
}
