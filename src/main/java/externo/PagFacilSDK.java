package externo;

/** @note Papel no Padrão: Adaptee (Adapter). Simula o SDK de um gateway externo que não podemos alterar. */
public class PagFacilSDK {
    public static final int STATUS_APROVADO = 200;
    public static final int STATUS_RECUSADO = 402;

    private static final long LIMITE_EM_CENTAVOS = 500_000;

    // Trabalha com valores em centavos e responde com um código de status.
    public int efetuarCobranca(long valorEmCentavos, int numeroParcelas) {
        if (valorEmCentavos > LIMITE_EM_CENTAVOS) {
            return STATUS_RECUSADO;
        }
        return STATUS_APROVADO;
    }
}
