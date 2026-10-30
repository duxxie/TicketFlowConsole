package pattern.decorator;

/** @note Papel no Padrão: Concrete Decorator. Pulseira de open bar, vendida só em eventos que oferecem o serviço. */
public class KitOpenBarDecorator extends IngressoDecorator {
    private static final double VALOR = 120.0;

    public KitOpenBarDecorator(IIngresso ingresso) {
        super(ingresso, "Kit Open Bar", VALOR);
    }

    @Override
    public double getPreco() {
        return ingresso.getPreco() + VALOR;
    }

    @Override
    public String getDescricao() {
        return ingresso.getDescricao() + " + Kit Open Bar";
    }
}
