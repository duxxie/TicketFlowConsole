package model;

import java.util.Objects;
import java.util.UUID;

import interfaces.IIngresso;
import interfaces.IngressoState;
import pattern.state.EstadoPendente;

/** Ingresso simples, sem adicionais. */
public class IngressoBase implements IIngresso {
    private final String id;
    private final Evento evento;
    private final Lote lote;
    private final Usuario comprador;
    private final double preco;
    private IngressoState estado = new EstadoPendente();

    public IngressoBase(Evento evento, Lote lote, Usuario comprador) {
        this.id = UUID.randomUUID().toString();
        this.evento = Objects.requireNonNull(evento, "O evento do ingresso deve ser definido.");
        this.lote = Objects.requireNonNull(lote, "O lote do ingresso deve ser definido.");
        this.comprador = Objects.requireNonNull(comprador, "O comprador do ingresso deve ser definido.");
        // Preço fixado na compra: mudanças no lote não afetam ingressos já vendidos.
        this.preco = lote.calcularPreco();
    }

    public String getId() {
        return id;
    }

    public Evento getEvento() {
        return evento;
    }

    public Lote getLote() {
        return lote;
    }

    public Usuario getComprador() {
        return comprador;
    }

    @Override
    public IngressoState getEstado() {
        return estado;
    }

    public void setEstado(IngressoState estado) {
        this.estado = Objects.requireNonNull(estado, "O estado do ingresso não pode ser nulo.");
    }

    @Override
    public double getPreco() {
        return preco;
    }

    @Override
    public String getDescricao() {
        return "Ingresso para " + evento.getNome() + " - " + lote.getNome();
    }
}
