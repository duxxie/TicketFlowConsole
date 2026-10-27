package factory;

import java.time.LocalDateTime;
import java.util.UUID;

import model.Pagamento;
import model.PagamentoBoleto;
import model.PagamentoCartao;
import model.PagamentoPix;

public class PagamentoFactory {
    public enum TipoPagamento {
        PIX,
        CARTAO,
        BOLETO
    }

    public Pagamento criarPagamento(TipoPagamento tipo, double valor) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de pagamento deve ser informado.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("O valor do pagamento não pode ser negativo.");
        }

        String idTransacao = UUID.randomUUID().toString();
        LocalDateTime dataHora = LocalDateTime.now();

        switch (tipo) {
            case PIX:
                return new PagamentoPix(idTransacao, valor, dataHora);
            case CARTAO:
                return new PagamentoCartao(idTransacao, valor, dataHora);
            case BOLETO:
                return new PagamentoBoleto(idTransacao, valor, dataHora);
            default:
                throw new IllegalArgumentException("Tipo de pagamento não suportado: " + tipo);
        }
    }
}
