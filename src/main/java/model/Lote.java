package model;

import pattern.strategy.CalculoPrecoStrategy;

/**
 * Lote de ingressos de um evento (ex.: "1º Lote", "Pista Promocional").
 * Controla o estoque e delega o cálculo do preço para uma estratégia.
 */
public class Lote {
    private int numero;
    private String nome;
    private double precoBase;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private CalculoPrecoStrategy estrategiaPreco;

    public Lote() {
    }

    public Lote(int numero, String nome, double precoBase, int quantidadeTotal,
                CalculoPrecoStrategy estrategiaPreco) {
        if (precoBase < 0) {
            throw new IllegalArgumentException("O preço base do lote não pode ser negativo.");
        }
        if (quantidadeTotal <= 0) {
            throw new IllegalArgumentException("O lote deve ter pelo menos um ingresso.");
        }
        this.numero = numero;
        this.nome = nome;
        this.precoBase = precoBase;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal;
        this.estrategiaPreco = estrategiaPreco;
    }

    public double calcularPreco() {
        if (estrategiaPreco == null) {
            throw new IllegalStateException("Uma estratégia de preço deve ser definida para o lote.");
        }
        return estrategiaPreco.calcularPreco(precoBase);
    }

    /**
     * Retira um ingresso do estoque no momento da compra.
     */
    public void reservarIngresso() {
        if (isEsgotado()) {
            throw new IllegalStateException("O lote " + nome + " está esgotado.");
        }
        quantidadeDisponivel--;
    }

    /**
     * Devolve um ingresso ao estoque quando uma compra é cancelada.
     */
    public void devolverIngresso() {
        if (quantidadeDisponivel == quantidadeTotal) {
            throw new IllegalStateException("O lote " + nome + " já está com o estoque completo.");
        }
        quantidadeDisponivel++;
    }

    public boolean isEsgotado() {
        return quantidadeDisponivel == 0;
    }

    public int getQuantidadeVendida() {
        return quantidadeTotal - quantidadeDisponivel;
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

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public CalculoPrecoStrategy getEstrategiaPreco() {
        return estrategiaPreco;
    }

    public void setEstrategiaPreco(CalculoPrecoStrategy estrategiaPreco) {
        this.estrategiaPreco = estrategiaPreco;
    }
}
