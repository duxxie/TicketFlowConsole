package pattern.state;

import model.IngressoBase;

/** @note Papel no Padrão: Concrete State. Ingresso já usado na entrada; não aceita mais ações. */
public class EstadoUtilizado implements IngressoState {

    @Override
    public void pagar(IngressoBase ingresso) {
        throw new IllegalStateException("Este ingresso já foi pago e utilizado.");
    }

    @Override
    public void fazerCheckIn(IngressoBase ingresso) {
        throw new IllegalStateException("Check-in negado: o ingresso já foi utilizado.");
    }

    @Override
    public void cancelar(IngressoBase ingresso) {
        throw new IllegalStateException("Não é possível cancelar um ingresso já utilizado.");
    }

    @Override
    public String getNomeEstado() {
        return "Utilizado";
    }
}
