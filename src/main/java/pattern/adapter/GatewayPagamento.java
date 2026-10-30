package pattern.adapter;

/** @note Papel no Padrão: Target (Adapter). Interface que o sistema usa para autorizar compras no cartão. */
public interface GatewayPagamento {
    boolean autorizar(double valor, int parcelas);

    String getNome();
}
