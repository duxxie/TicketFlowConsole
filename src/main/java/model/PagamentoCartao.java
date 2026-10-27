package model;

import java.time.LocalDateTime;

/** @note Papel no Padrão: Concrete Product (Factory Method). Cartão de crédito, parcelável em até 12x. */
public class PagamentoCartao extends Pagamento {
    private static final int MAXIMO_PARCELAS = 12;

    private int parcelas = 1;

    public PagamentoCartao(String idTransacao, double valor, LocalDateTime dataHora) {
        super(idTransacao, valor, dataHora);
    }

    @Override
    public void processar() {
        // Simulação: a operadora sempre autoriza.
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
