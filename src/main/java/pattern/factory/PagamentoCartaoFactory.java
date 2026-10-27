package pattern.factory;

import java.time.LocalDateTime;

import model.Pagamento;
import model.PagamentoCartao;
import pattern.adapter.GatewayPagamento;

/** @note Papel no Padrão: Concrete Creator (Factory Method). Cria pagamentos com cartão já parcelados. */
public class PagamentoCartaoFactory extends PagamentoFactory {
    private final int parcelas;
    private final GatewayPagamento gateway;

    public PagamentoCartaoFactory(int parcelas, GatewayPagamento gateway) {
        this.parcelas = parcelas;
        this.gateway = gateway;
    }

    @Override
    protected Pagamento criarPagamento(String idTransacao, double valor, LocalDateTime dataHora) {
        PagamentoCartao pagamento = new PagamentoCartao(idTransacao, valor, dataHora, gateway);
        pagamento.setParcelas(parcelas);
        return pagamento;
    }
}
