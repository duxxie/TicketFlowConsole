package pattern.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Evento;
import model.Lote;

/** @note Papel no Padrão: Builder. Monta um Evento passo a passo e valida antes de criar. */
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

    public EventoBuilder adicionarLote(Lote lote) {
        this.lotes.add(Objects.requireNonNull(lote, "O lote não pode ser nulo."));
        return this;
    }

    public Evento build() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalStateException("O nome do evento deve ser informado.");
        }
        if (local == null || local.trim().isEmpty()) {
            throw new IllegalStateException("O local do evento deve ser informado.");
        }
        if (dataHora == null || dataHora.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("O evento deve ter uma data futura.");
        }
        if (idadeMinima < 0) {
            throw new IllegalStateException("A idade mínima não pode ser negativa.");
        }
        if (lotes.isEmpty()) {
            throw new IllegalStateException("O evento deve ter pelo menos um lote.");
        }

        System.out.println("[BUILDER] Evento \"" + nome + "\" montado com " + lotes.size() + " lote(s).");
        return new Evento(id, nome, local, dataHora, idadeMinima, openBar, new ArrayList<>(lotes));
    }
}
