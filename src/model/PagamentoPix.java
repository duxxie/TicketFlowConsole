package model;

import java.time.LocalDateTime;

public class PagamentoPix extends Pagamento {
    public PagamentoPix(String idTransacao, double valor, LocalDateTime dataHora, boolean aprovado) {
        super(idTransacao, valor, dataHora, aprovado);
    }
}
