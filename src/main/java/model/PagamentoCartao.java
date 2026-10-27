package model;

import java.time.LocalDateTime;
import java.util.Objects;

import pattern.adapter.GatewayPagamento;

/** @note Papel no Padrão: Concrete Product (Factory Method). Cartão de crédito, autorizado por um gateway externo. */
public class PagamentoCartao extends Pagamento {
    private static final int MAXIMO_PARCELAS = 12;

    private final GatewayPagamento gateway;
    private int parcelas = 1;

    public PagamentoCartao(String idTransacao, double valor, LocalDateTime dataHora, GatewayPagamento gateway) {
        super(idTransacao, valor, dataHora);
        this.gateway = Objects.requireNonNull(gateway, "O gateway de pagamento deve ser informado.");
    }

    @Override
    public void processar() {
        if (gateway.autorizar(getValor(), parcelas)) {
            aprovar();
        }
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
