package pattern.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Evento;
import model.Lote;

public class EventoBuilder {
    private Long id;
    private String nome;
    private String local;
    private LocalDateTime dataHora;
    private int idadeMinima;
    private boolean openBar;
    private List<Lote> lotes = new ArrayList<>();

    public EventoBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public EventoBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public EventoBuilder comLocal(String local) {
        this.local = local;
        return this;
    }

    public EventoBuilder comDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
        return this;
    }

    public EventoBuilder comIdadeMinima(int idadeMinima) {
        this.idadeMinima = idadeMinima;
        return this;
    }

    public EventoBuilder comOpenBar() {
        this.openBar = true;
        return this;
    }

    public EventoBuilder comLotes(List<Lote> lotes) {
        this.lotes = new ArrayList<>(lotes);
        return this;
    }

    public Evento build() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalStateException("O nome do evento deve ser informado.");
        }
        if (idadeMinima < 0) {
            throw new IllegalStateException("A idade mínima não pode ser negativa.");
        }

        return new Evento(id, nome, local, dataHora, idadeMinima, openBar,
                new ArrayList<>(lotes));
    }
}
