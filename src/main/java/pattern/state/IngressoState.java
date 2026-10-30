package pattern.state;

import model.IngressoBase;

/** @note Papel no Padrão: State. Define as ações do ingresso, que mudam conforme o estado atual. */
public interface IngressoState {
    void pagar(IngressoBase ingresso);

    void fazerCheckIn(IngressoBase ingresso);

    void cancelar(IngressoBase ingresso);

    String getNomeEstado();
}
