package pattern.decorator;

/** @note Papel no Padrão: Concrete Decorator. Seguro contra imprevistos, custa 10% do valor do ingresso. */
public class SeguroIngressoDecorator extends IngressoDecorator {
    private static final double PERCENTUAL = 0.10;

    public SeguroIngressoDecorator(IIngresso ingresso) {
        super(ingresso, "Seguro", ingresso.getPreco() * PERCENTUAL);
    }

    // O seguro é calculado sobre o valor do ingresso já com os adicionais anteriores.
    @Override
    public double getPreco() {
        return ingresso.getPreco() * (1 + PERCENTUAL);
    }

    @Override
    public String getDescricao() {
        return ingresso.getDescricao() + " + Seguro";
    }
}
