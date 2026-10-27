package pattern.decorator;

import java.util.Objects;

import pattern.state.IngressoState;

/** @note Papel no Padrão: Decorator. Envolve um ingresso e repassa a ele tudo que não for alterado. */
public abstract class IngressoDecorator implements IIngresso {
    protected final IIngresso ingresso;

    protected IngressoDecorator(IIngresso ingresso, String nomeAdicional, double valorAdicional) {
        this.ingresso = Objects.requireNonNull(ingresso, "O ingresso não pode ser nulo.");
        System.out.println("[DECORATOR] Adicional " + nomeAdicional + " (+"
                + String.format("R$ %.2f", valorAdicional) + ") aplicado ao ingresso.");
    }

    @Override
    public double getPreco() {
        return ingresso.getPreco();
    }

    @Override
    public String getDescricao() {
        return ingresso.getDescricao();
    }

    @Override
    public IngressoState getEstado() {
        return ingresso.getEstado();
    }

    @Override
    public void pagar() {
        ingresso.pagar();
    }

    @Override
    public void fazerCheckIn() {
        ingresso.fazerCheckIn();
    }

    @Override
    public void cancelar() {
        ingresso.cancelar();
    }
}
