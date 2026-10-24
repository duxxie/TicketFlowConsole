package model;

import java.util.Objects;
import java.util.UUID;

import interfaces.IIngresso;
import interfaces.IngressoState;
import pattern.state.EstadoPendente;

public class IngressoBase implements IIngresso {
    private String id;
    private Evento evento;
    private Lote lote;
    private Usuario comprador;
    private IngressoState estado = new EstadoPendente();

    public IngressoBase() {
        this.id = UUID.randomUUID().toString();
    }

    public IngressoBase(Evento evento, Lote lote, Usuario comprador) {
        this(UUID.randomUUID().toString(), evento, lote, comprador);
    }

    public IngressoBase(String id, Evento evento, Lote lote, Usuario comprador) {
        this.id = id;
        this.evento = evento;
        this.lote = lote;
        this.comprador = comprador;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
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
        return Objects.requireNonNull(lote, "O lote do ingresso deve ser definido.")
                .calcularPreco();
    }

    @Override
    public String getDescricao() {
        String nomeEvento = Objects.requireNonNull(evento, "O evento do ingresso deve ser definido.")
                .getNome();
        String nomeLote = Objects.requireNonNull(lote, "O lote do ingresso deve ser definido.")
                .getNome();
        return "Ingresso para " + nomeEvento + " - " + nomeLote;
    }
}