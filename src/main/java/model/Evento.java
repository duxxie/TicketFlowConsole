package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Evento com ingressos à venda (show, festa, palestra etc.), dividido em lotes.
 */
public class Evento {
    private Long id;
    private String nome;
    private String local;
    private LocalDateTime dataHora;
    private int idadeMinima;
    private boolean openBar;
    private List<Lote> lotes = new ArrayList<>();

    public Evento() {
    }

    public Evento(Long id, String nome, String local, LocalDateTime dataHora, int idadeMinima,
                  List<Lote> lotes) {
        this.id = id;
        this.nome = nome;
        this.local = local;
        this.dataHora = dataHora;
        this.idadeMinima = idadeMinima;
        this.lotes = lotes;
    }

    public Evento(Long id, String nome, String local, LocalDateTime dataHora, int idadeMinima,
                  boolean openBar, List<Lote> lotes) {
        this(id, nome, local, dataHora, idadeMinima, lotes);
        this.openBar = openBar;
    }

    public void adicionarLote(Lote lote) {
        lotes.add(Objects.requireNonNull(lote, "O lote não pode ser nulo."));
    }

    public Lote buscarLote(int numero) {
        for (Lote lote : lotes) {
            if (lote.getNumero() == numero) {
                return lote;
            }
        }
        throw new IllegalArgumentException("Lote " + numero + " não encontrado no evento " + nome + ".");
    }

    /**
     * Verifica se a idade informada atende à classificação etária do evento.
     */
    public boolean permiteIdade(int idade) {
        return idade >= idadeMinima;
    }

    public boolean jaAconteceu() {
        return dataHora != null && dataHora.isBefore(LocalDateTime.now());
    }

    public int getIngressosDisponiveis() {
        int total = 0;
        for (Lote lote : lotes) {
            total += lote.getQuantidadeDisponivel();
        }
        return total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public int getIdadeMinima() {
        return idadeMinima;
    }

    public void setIdadeMinima(int idadeMinima) {
        this.idadeMinima = idadeMinima;
    }

    public boolean isOpenBar() {
        return openBar;
    }

    public void setOpenBar(boolean openBar) {
        this.openBar = openBar;
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    public void setLotes(List<Lote> lotes) {
        this.lotes = lotes;
    }
}
