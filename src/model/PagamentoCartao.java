package model;

import java.time.LocalDateTime;

public class PagamentoCartao extends Pagamento {
    public PagamentoCartao(String idTransacao, double valor, LocalDateTime dataHora, boolean aprovado) {
        super(idTransacao, valor, dataHora, aprovado);
    }
}
