package pattern.state;

import model.IngressoBase;

/** @note Papel no Padrão: Concrete State. Ingresso cancelado; não aceita mais ações. */
public class EstadoCancelado implements IngressoState {

    @Override
    public void pagar(IngressoBase ingresso) {
        throw new IllegalStateException("Não é possível pagar um ingresso cancelado.");
    }

    @Override
    public void fazerCheckIn(IngressoBase ingresso) {
        throw new IllegalStateException("Check-in negado: o ingresso foi cancelado.");
    }

    @Override
    public void cancelar(IngressoBase ingresso) {
        throw new IllegalStateException("Este ingresso já está cancelado.");
    }

    @Override
    public String getNomeEstado() {
        return "Cancelado";
    }
}
