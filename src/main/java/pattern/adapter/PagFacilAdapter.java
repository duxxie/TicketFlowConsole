package pattern.adapter;

import externo.PagFacilSDK;

/** @note Papel no Padrão: Adapter. Traduz o GatewayPagamento para o formato do PagFacilSDK. */
public class PagFacilAdapter implements GatewayPagamento {
    private final PagFacilSDK sdk = new PagFacilSDK();

    @Override
    public boolean autorizar(double valor, int parcelas) {
        long valorEmCentavos = Math.round(valor * 100);
        System.out.println("[ADAPTER] PagFacilAdapter: R$ " + String.format("%.2f", valor)
                + " convertido para " + valorEmCentavos + " centavos.");

        int status = sdk.efetuarCobranca(valorEmCentavos, parcelas);
        return status == PagFacilSDK.STATUS_APROVADO;
    }

    @Override
    public String getNome() {
        return "PagFácil";
    }
}
