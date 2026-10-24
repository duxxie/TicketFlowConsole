package interfaces;

import interfaces.IngressoState;

public interface IIngresso {
    double getPreco();

    String getDescricao();

    IngressoState getEstado();
}
