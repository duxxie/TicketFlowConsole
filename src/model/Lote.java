package model;

import pattern.strategy.CalculoPrecoStrategy;

public class Lote {
    private int numero;
    private String nome;
    private double precoBase;
    private int quantidadeDisponivel;
    private CalculoPrecoStrategy estrategiaPreco;

    public Lote() {
    }

    public Lote(int numero, String nome, double precoBase, int quantidadeDisponivel,
                CalculoPrecoStrategy estrategiaPreco) {
        this.numero = numero;
        this.nome = nome;
        this.precoBase = precoBase;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.estrategiaPreco = estrategiaPreco;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public CalculoPrecoStrategy getEstrategiaPreco() {
        return estrategiaPreco;
    }

    public void setEstrategiaPreco(CalculoPrecoStrategy estrategiaPreco) {
        this.estrategiaPreco = estrategiaPreco;
    }

    public double calcularPreco() {
        if (estrategiaPreco == null) {
            throw new IllegalStateException("Uma estratégia de preço deve ser definida para o lote.");
        }
        return estrategiaPreco.calcularPreco(precoBase);
    }
}
