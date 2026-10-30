package pattern.factory;

import java.time.LocalDateTime;

import model.Pagamento;
import model.PagamentoBoleto;

/** @note Papel no Padrão: Concrete Creator (Factory Method). Cria pagamentos via boleto. */
public class PagamentoBoletoFactory extends PagamentoFactory {

    @Override
    protected Pagamento criarPagamento(String idTransacao, double valor, LocalDateTime dataHora) {
        return new PagamentoBoleto(idTransacao, valor, dataHora);
    }
}
