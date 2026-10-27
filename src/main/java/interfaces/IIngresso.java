package interfaces;

import pattern.state.IngressoState;

/** Contrato comum dos ingressos, com ou sem adicionais. */
public interface IIngresso {
    double getPreco();

    String getDescricao();

    IngressoState getEstado();

    void pagar();

    void fazerCheckIn();

    void cancelar();
}
