package pattern.strategy;

import model.Lote;

/** @note Papel no Padrão: Concrete Strategy. O preço sobe 10% a cada 25% do lote vendido. */
public class PrecoDinamicoStrategy implements CalculoPrecoStrategy {
    private static final double AUMENTO_POR_FAIXA = 0.10;
    private static final double TAMANHO_FAIXA = 0.25;

    @Override
    public double calcularPreco(Lote lote) {
        double percentualVendido = (double) lote.getQuantidadeVendida() / lote.getQuantidadeTotal();
        int faixasVendidas = (int) (percentualVendido / TAMANHO_FAIXA);
        return lote.getPrecoBase() * (1 + faixasVendidas * AUMENTO_POR_FAIXA);
    }

    @Override
    public String getDescricao() {
        return "Dinâmico (+10% a cada 25% vendido)";
    }
}
