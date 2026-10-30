package pattern.state;

import model.IngressoBase;

/** @note Papel no Padrão: Concrete State. Ingresso reservado, aguardando pagamento. */
public class EstadoPendente implements IngressoState {

    @Override
    public void pagar(IngressoBase ingresso) {
        ingresso.setEstado(new EstadoPago());
    }

    @Override
    public void fazerCheckIn(IngressoBase ingresso) {
        throw new IllegalStateException("Check-in negado: o ingresso ainda não foi pago.");
    }

    @Override
    public void cancelar(IngressoBase ingresso) {
        ingresso.getLote().devolverIngresso();
        ingresso.setEstado(new EstadoCancelado());
    }

    @Override
    public String getNomeEstado() {
        return "Pendente";
    }
}
