package model;

import java.time.LocalDateTime;

/**
 * Pagamento com cartão de crédito, permitindo parcelamento em até 12 vezes.
 */
public class PagamentoCartao extends Pagamento {
    private static final int MAXIMO_PARCELAS = 12;

    private int parcelas = 1;

    public PagamentoCartao(String idTransacao, double valor, LocalDateTime dataHora) {
        super(idTransacao, valor, dataHora);
    }

    @Override
    public void processar() {
        // Simulação: a operadora do cartão sempre autoriza a compra.
        aprovar();
    }

    @Override
    public String getMetodo() {
        return "Cartão de crédito";
    }

    public double getValorParcela() {
        return getValor() / parcelas;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        if (parcelas < 1 || parcelas > MAXIMO_PARCELAS) {
            throw new IllegalArgumentException("O número de parcelas deve ser entre 1 e " + MAXIMO_PARCELAS + ".");
        }
        this.parcelas = parcelas;
    }
}
