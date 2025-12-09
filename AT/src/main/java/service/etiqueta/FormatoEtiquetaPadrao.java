package service.etiqueta;

import model.Entrega;

import java.math.BigDecimal;

public class FormatoEtiquetaPadrao implements FormatoEtiqueta {

    @Override
    public String gerarEtiqueta(Entrega entrega, BigDecimal valorFrete) {
        return """
                Destinatário: %s
                Endereço: %s
                Valor do Frete: R$ %s
                """.formatted(
                entrega.getDestinatario(),
                entrega.getEndereco(),
                valorFrete
        );
    }

    @Override
    public String gerarResumo(Entrega entrega, BigDecimal valorFrete) {
        return "Pedido para %s com frete tipo %s: no valor de R$ %s"
                .formatted(
                        entrega.getDestinatario(),
                        entrega.getTipoFrete(),
                        valorFrete
                );
    }
}

