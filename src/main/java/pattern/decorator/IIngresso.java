package pattern.decorator;

import pattern.state.IngressoState;

/** @note Papel no Padrão: Component (Decorator). Contrato comum dos ingressos, com ou sem adicionais. */
public interface IIngresso {
    String getId();

    double getPreco();

    String getDescricao();

    IngressoState getEstado();

    void pagar();

    void fazerCheckIn();

    void cancelar();
}
