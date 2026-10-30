package externo;

/** @note Papel no Padrão: Adaptee (Adapter). Simula a API de um gateway estrangeiro que não podemos alterar. */
public class CashNowApi {
    private static final int MAXIMO_PARCELAS = 6;

    // Recebe tudo como texto e responde "APPROVED" ou "DECLINED".
    public String charge(String amount, String installments) {
        if (Integer.parseInt(installments) > MAXIMO_PARCELAS) {
            return "DECLINED";
        }
        return "APPROVED";
    }
}
