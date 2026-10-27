package pattern.strategy;

import model.Lote;

/** @note Papel no Padrão: Concrete Strategy. Aplica um desconto percentual sobre o preço base. */
public class PrecoPromocionalStrategy implements CalculoPrecoStrategy {
    private final int percentualDesconto;

    public PrecoPromocionalStrategy(int percentualDesconto) {
        if (percentualDesconto <= 0 || percentualDesconto >= 100) {
            throw new IllegalArgumentException("O desconto deve ser entre 1% e 99%.");
        }
        this.percentualDesconto = percentualDesconto;
    }

    @Override
    public double calcularPreco(Lote lote) {
        return lote.getPrecoBase() * (100 - percentualDesconto) / 100;
    }

    @Override
    public String getDescricao() {
        return "Promocional (" + percentualDesconto + "% de desconto)";
    }
}
