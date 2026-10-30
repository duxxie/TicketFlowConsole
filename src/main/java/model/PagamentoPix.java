package model;

import java.time.LocalDateTime;

/** @note Papel no Padrão: Concrete Product (Factory Method). Pagamento via Pix, aprovado na hora. */
public class PagamentoPix extends Pagamento {
    private final String codigoCopiaECola;

    public PagamentoPix(String idTransacao, double valor, LocalDateTime dataHora) {
        super(idTransacao, valor, dataHora);
        this.codigoCopiaECola = "PIX-" + idTransacao.substring(0, 8).toUpperCase();
    }

    @Override
    public void processar() {
        aprovar();
    }

    @Override
    public String getMetodo() {
        return "Pix";
    }

    public String getCodigoCopiaECola() {
        return codigoCopiaECola;
    }
}
