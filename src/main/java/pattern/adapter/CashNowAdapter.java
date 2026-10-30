package pattern.adapter;

import java.util.Locale;

import externo.CashNowApi;

/** @note Papel no Padrão: Adapter. Traduz o GatewayPagamento para o formato da CashNowApi. */
public class CashNowAdapter implements GatewayPagamento {
    private final CashNowApi api = new CashNowApi();

    @Override
    public boolean autorizar(double valor, int parcelas) {
        String valorTexto = String.format(Locale.US, "%.2f", valor);
        System.out.println("[ADAPTER] CashNowAdapter: R$ " + String.format("%.2f", valor)
                + " convertido para o texto \"" + valorTexto + "\".");

        String resposta = api.charge(valorTexto, String.valueOf(parcelas));
        return resposta.equals("APPROVED");
    }

    @Override
    public String getNome() {
        return "CashNow";
    }
}
