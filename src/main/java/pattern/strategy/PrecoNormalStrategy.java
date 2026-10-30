package pattern.strategy;

import model.Lote;

/** @note Papel no Padrão: Concrete Strategy. Cobra o preço base do lote. */
public class PrecoNormalStrategy implements CalculoPrecoStrategy {

    @Override
    public double calcularPreco(Lote lote) {
        return lote.getPrecoBase();
    }

    @Override
    public String getDescricao() {
        return "Preço normal";
    }
}
