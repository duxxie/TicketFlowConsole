package pattern.factory;

import java.time.LocalDateTime;

import model.Pagamento;
import model.PagamentoCartao;

/** @note Papel no Padrão: Concrete Creator (Factory Method). Cria pagamentos com cartão já parcelados. */
public class PagamentoCartaoFactory extends PagamentoFactory {
    private final int parcelas;

    public PagamentoCartaoFactory(int parcelas) {
        this.parcelas = parcelas;
    }

    @Override
    protected Pagamento criarPagamento(String idTransacao, double valor, LocalDateTime dataHora) {
        PagamentoCartao pagamento = new PagamentoCartao(idTransacao, valor, dataHora);
        pagamento.setParcelas(parcelas);
        return pagamento;
    }
}
