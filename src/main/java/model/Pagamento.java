package model;

import java.time.LocalDateTime;

/**
 * Base para os meios de pagamento aceitos. Cada subclasse define como o pagamento
 * é processado e quando ele é considerado aprovado.
 */
public abstract class Pagamento {
    private final String idTransacao;
    private final double valor;
    private final LocalDateTime dataHora;
    private boolean aprovado;

    protected Pagamento(String idTransacao, double valor, LocalDateTime dataHora) {
        this.idTransacao = idTransacao;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    /**
     * Executa o pagamento seguindo as regras do meio escolhido.
     */
    public abstract void processar();

    /**
     * Nome do meio de pagamento, usado nas mensagens do sistema.
     */
    public abstract String getMetodo();

    // Só as subclasses aprovam o pagamento, quando suas regras forem cumpridas.
    protected void aprovar() {
        this.aprovado = true;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public boolean isAprovado() {
        return aprovado;
    }
}
