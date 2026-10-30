package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** @note Papel no Padrão: Concrete Product (Factory Method). Boleto, aprovado só se compensado antes do vencimento. */
public class PagamentoBoleto extends Pagamento {
    private static final int DIAS_PARA_VENCIMENTO = 3;

    private final String codigoBarras;
    private final LocalDate vencimento;

    public PagamentoBoleto(String idTransacao, double valor, LocalDateTime dataHora) {
        super(idTransacao, valor, dataHora);
        this.codigoBarras = "BOL-" + idTransacao.substring(0, 8).toUpperCase();
        this.vencimento = dataHora.toLocalDate().plusDays(DIAS_PARA_VENCIMENTO);
    }

    @Override
    public void processar() {
        // Só emite o boleto; a aprovação vem em confirmarCompensacao().
    }

    /** Simula o banco confirmando o pagamento do boleto. */
    public void confirmarCompensacao() {
        if (isVencido()) {
            throw new IllegalStateException("O boleto venceu em " + vencimento + " e não pode mais ser pago.");
        }
        aprovar();
    }

    @Override
    public String getMetodo() {
        return "Boleto";
    }

    public boolean isVencido() {
        return LocalDate.now().isAfter(vencimento);
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }
}
