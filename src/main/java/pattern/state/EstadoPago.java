package pattern.state;

import model.IngressoBase;

/** @note Papel no Padrão: Concrete State. Ingresso pago, liberado para a entrada no evento. */
public class EstadoPago implements IngressoState {

    @Override
    public void pagar(IngressoBase ingresso) {
        throw new IllegalStateException("Este ingresso já foi pago.");
    }

    @Override
    public void fazerCheckIn(IngressoBase ingresso) {
        ingresso.setEstado(new EstadoUtilizado());
    }

    @Override
    public void cancelar(IngressoBase ingresso) {
        ingresso.getLote().devolverIngresso();
        ingresso.setEstado(new EstadoCancelado());
    }

    @Override
    public String getNomeEstado() {
        return "Pago";
    }
}
