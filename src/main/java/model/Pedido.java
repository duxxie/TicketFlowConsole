package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import interfaces.IIngresso;

public class Pedido {
    private Long id;
    private Usuario comprador;
    private List<IIngresso> itens = new ArrayList<>();
    private Pagamento pagamento;
    private double valorTotal;
    private LocalDateTime dataCriacao;

    public Pedido() {
    }

    public Pedido(Long id, Usuario comprador, List<IIngresso> itens, Pagamento pagamento,
                  double valorTotal, LocalDateTime dataCriacao) {
        this.id = id;
        this.comprador = comprador;
        this.itens = itens;
        this.pagamento = pagamento;
        this.valorTotal = valorTotal;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public List<IIngresso> getItens() {
        return itens;
    }

    public void setItens(List<IIngresso> itens) {
        this.itens = itens;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
