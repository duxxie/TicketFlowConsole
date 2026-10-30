package model;

import java.time.LocalDateTime;

/** @note Papel no Padrão: Product (Factory Method). Cada subclasse define quando o pagamento é aprovado. */
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

    public abstract void processar();

    public abstract String getMetodo();

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
