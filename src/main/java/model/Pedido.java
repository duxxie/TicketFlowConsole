package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import pattern.decorator.IIngresso;

/** Pedido de compra com os ingressos escolhidos e o pagamento. */
public class Pedido {
    private final Long id;
    private final Usuario comprador;
    private final List<IIngresso> itens = new ArrayList<>();
    private final LocalDateTime dataCriacao;
    private Pagamento pagamento;
    private boolean cancelado;

    public Pedido(Long id, Usuario comprador) {
        this.id = id;
        this.comprador = Objects.requireNonNull(comprador, "O pedido deve ter um comprador.");
        this.dataCriacao = LocalDateTime.now();
    }

    public void adicionarItem(IIngresso ingresso) {
        if (!isAberto()) {
            throw new IllegalStateException("Não é possível alterar um pedido pago ou cancelado.");
        }
        itens.add(Objects.requireNonNull(ingresso, "O ingresso não pode ser nulo."));
    }

    public double getValorTotal() {
        double total = 0;
        for (IIngresso item : itens) {
            total += item.getPreco();
        }
        return total;
    }

    public void cancelar() {
        this.cancelado = true;
    }

    public boolean isPago() {
        return pagamento != null && pagamento.isAprovado();
    }

    public boolean isCancelado() {
        return cancelado;
    }

    // Aberto = ainda pode receber itens e ser pago.
    public boolean isAberto() {
        return !isPago() && !cancelado;
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
