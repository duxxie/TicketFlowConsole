package model;

import java.time.LocalDateTime;

public abstract class Pagamento {
    private String idTransacao;
    private double valor;
    private LocalDateTime dataHora;
    private boolean aprovado;

    protected Pagamento(String idTransacao, double valor, LocalDateTime dataHora, boolean aprovado) {
        this.idTransacao = idTransacao;
        this.valor = valor;
        this.dataHora = dataHora;
        this.aprovado = aprovado;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }
}
