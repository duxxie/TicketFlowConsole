package pattern.decorator;

/** @note Papel no Padrão: Concrete Decorator. Vaga de estacionamento no local do evento. */
public class EstacionamentoDecorator extends IngressoDecorator {
    private static final double VALOR = 40.0;

    public EstacionamentoDecorator(IIngresso ingresso) {
        super(ingresso, "Estacionamento", VALOR);
    }

    @Override
    public double getPreco() {
        return ingresso.getPreco() + VALOR;
    }

    @Override
    public String getDescricao() {
        return ingresso.getDescricao() + " + Estacionamento";
    }
}
