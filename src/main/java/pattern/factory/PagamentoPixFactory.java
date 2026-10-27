package pattern.factory;

import java.time.LocalDateTime;

import model.Pagamento;
import model.PagamentoPix;

/** @note Papel no Padrão: Concrete Creator (Factory Method). Cria pagamentos via Pix. */
public class PagamentoPixFactory extends PagamentoFactory {

    @Override
    protected Pagamento criarPagamento(String idTransacao, double valor, LocalDateTime dataHora) {
        return new PagamentoPix(idTransacao, valor, dataHora);
    }
}
