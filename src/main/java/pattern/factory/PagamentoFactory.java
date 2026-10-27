package pattern.factory;

import java.time.LocalDateTime;
import java.util.UUID;

import model.Pagamento;

/** @note Papel no Padrão: Creator (Factory Method). Base das fábricas de pagamento. */
public abstract class PagamentoFactory {

    // Factory Method: cada subclasse decide qual tipo de pagamento instanciar.
    protected abstract Pagamento criarPagamento(String idTransacao, double valor, LocalDateTime dataHora);

    public Pagamento novoPagamento(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }

        Pagamento pagamento = criarPagamento(UUID.randomUUID().toString(), valor, LocalDateTime.now());
        System.out.println("[FACTORY METHOD] " + getClass().getSimpleName()
                + " criou um pagamento via " + pagamento.getMetodo() + ".");
        return pagamento;
    }
}
