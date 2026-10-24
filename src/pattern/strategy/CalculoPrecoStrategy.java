package pattern.strategy;

@FunctionalInterface
public interface CalculoPrecoStrategy {
    double calcularPreco(double precoBase);
}
