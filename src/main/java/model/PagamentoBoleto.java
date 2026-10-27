package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Pagamento via boleto: fica pendente até o banco confirmar a compensação,
 * que precisa acontecer antes do vencimento.
 */
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
        // Emitir o boleto não aprova o pagamento; a aprovação vem em confirmarCompensacao().
    }

    /**
     * Simula o retorno do banco informando que o boleto foi pago.
     */
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
