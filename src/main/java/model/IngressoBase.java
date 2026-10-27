package model;

import java.util.Objects;
import java.util.UUID;

import pattern.decorator.IIngresso;
import pattern.state.EstadoPendente;
import pattern.state.IngressoState;

/** @note Papel no Padrão: Concrete Component (Decorator) e Context (State). Ingresso simples, sem adicionais. */
public class IngressoBase implements IIngresso {
    private final String id;
    private final Evento evento;
    private final Lote lote;
    private final Usuario comprador;
    private final double preco;
    private IngressoState estado = new EstadoPendente();

    public IngressoBase(Evento evento, Lote lote, Usuario comprador) {
        // Código curto, fácil de digitar na portaria (ex.: 3F9A1C2B).
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.evento = Objects.requireNonNull(evento, "O evento do ingresso deve ser definido.");
        this.lote = Objects.requireNonNull(lote, "O lote do ingresso deve ser definido.");
        this.comprador = Objects.requireNonNull(comprador, "O comprador do ingresso deve ser definido.");
        if (lote.isEsgotado()) {
            throw new IllegalStateException("O lote " + lote.getNome() + " está esgotado.");
        }
        // Preço fixado na compra: mudanças no lote não afetam ingressos já vendidos.
        this.preco = lote.calcularPreco();
        lote.reservarIngresso();
    }

    @Override
    public void pagar() {
        estado.pagar(this);
    }

    @Override
    public void fazerCheckIn() {
        estado.fazerCheckIn(this);
    }

    @Override
    public void cancelar() {
        estado.cancelar(this);
    }

    public void setEstado(IngressoState novoEstado) {
        Objects.requireNonNull(novoEstado, "O estado do ingresso não pode ser nulo.");
        System.out.println("[STATE] Ingresso " + id + ": "
                + estado.getNomeEstado() + " -> " + novoEstado.getNomeEstado());
        this.estado = novoEstado;
    }

    @Override
    public IngressoState getEstado() {
        return estado;
    }

    @Override
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
    public double getPreco() {
        return preco;
    }

    @Override
    public String getDescricao() {
        return "Ingresso para " + evento.getNome() + " - " + lote.getNome();
    }
}
