package pattern.decorator;

/** @note Papel no Padrão: Concrete Decorator. Acesso à área VIP do evento. */
public class AreaVipDecorator extends IngressoDecorator {
    private static final double VALOR = 150.0;

    public AreaVipDecorator(IIngresso ingresso) {
        super(ingresso, "Área VIP", VALOR);
    }

    @Override
    public double getPreco() {
        return ingresso.getPreco() + VALOR;
    }

    @Override
    public String getDescricao() {
        return ingresso.getDescricao() + " + Área VIP";
    }
}
