package pattern.strategy;

import model.Lote;

/** @note Papel no Padrão: Strategy. Regra de cálculo do preço de um lote. */
public interface CalculoPrecoStrategy {
    double calcularPreco(Lote lote);

    String getDescricao();
}
