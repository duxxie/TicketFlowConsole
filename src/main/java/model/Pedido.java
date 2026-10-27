package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import interfaces.IIngresso;

/**
 * Pedido de compra: agrupa os ingressos escolhidos pelo comprador e o pagamento.
 */
public class Pedido {
    private final Long id;
    private final Usuario comprador;
    private final List<IIngresso> itens = new ArrayList<>();
    private final LocalDateTime dataCriacao;
    private Pagamento pagamento;

    public Pedido(Long id, Usuario comprador) {
        this.id = id;
        this.comprador = Objects.requireNonNull(comprador, "O pedido deve ter um comprador.");
        this.dataCriacao = LocalDateTime.now();
    }

    public void adicionarItem(IIngresso ingresso) {
        if (isPago()) {
            throw new IllegalStateException("Não é possível alterar um pedido que já foi pago.");
        }
        itens.add(Objects.requireNonNull(ingresso, "O ingresso não pode ser nulo."));
    }

    /**
     * O total é sempre calculado a partir dos itens, assim nunca fica desatualizado.
     */
    public double getValorTotal() {
        double total = 0;
        for (IIngresso item : itens) {
            total += item.getPreco();
        }
        return total;
    }

    public boolean isPago() {
        return pagamento != null && pagamento.isAprovado();
    }

    public Long getId() {
        return id;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public List<IIngresso> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
