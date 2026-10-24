package pattern.state;

import interfaces.IngressoState;

public class EstadoPendente implements IngressoState {
    @Override
    public String getNomeEstado() {
        return "Pendente";
    }
}
