package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import model.Evento;
import model.IngressoBase;
import model.Lote;
import model.Pagamento;
import model.PagamentoBoleto;
import model.Pedido;
import model.Usuario;
import pattern.decorator.AreaVipDecorator;
import pattern.decorator.EstacionamentoDecorator;
import pattern.decorator.IIngresso;
import pattern.decorator.KitOpenBarDecorator;
import pattern.decorator.SeguroIngressoDecorator;
import pattern.factory.PagamentoFactory;

/** Fluxo de compra: monta o pedido, aplica os adicionais, processa o pagamento e trata cancelamentos. */
public class CompraService {

    // O seguro fica por último porque é calculado sobre o valor com os outros adicionais.
    public enum Adicional {
        AREA_VIP, ESTACIONAMENTO, KIT_OPEN_BAR, SEGURO
    }

    private final List<Pedido> pedidos = new ArrayList<>();
    private long proximoId = 1;

    public Pedido criarPedido(Usuario comprador) {
        Pedido pedido = new Pedido(proximoId++, comprador);
        pedidos.add(pedido);
        return pedido;
    }

    public IIngresso adicionarIngresso(Pedido pedido, Evento evento, int numeroLote, Set<Adicional> adicionais) {
        // As validações vêm antes de criar o ingresso, pois criar já retira uma unidade do estoque.
        if (!pedido.isAberto()) {
            throw new IllegalStateException("Este pedido já foi pago ou cancelado.");
        }
        if (evento.jaAconteceu()) {
            throw new IllegalStateException("As vendas para este evento já foram encerradas.");
        }
        Usuario comprador = pedido.getComprador();
        if (!evento.permiteIdade(comprador.getIdade())) {
            throw new IllegalStateException("Evento proibido para menores de " + evento.getIdadeMinima() + " anos.");
        }
        if (adicionais.contains(Adicional.KIT_OPEN_BAR) && !evento.isOpenBar()) {
            throw new IllegalArgumentException("Este evento não oferece open bar.");
        }

        Lote lote = evento.buscarLote(numeroLote);
        IIngresso ingresso = aplicarAdicionais(new IngressoBase(evento, lote, comprador), adicionais);
        pedido.adicionarItem(ingresso);
        return ingresso;
    }

    public Pagamento pagar(Pedido pedido, PagamentoFactory fabrica) {
        if (!pedido.isAberto()) {
            throw new IllegalStateException("Este pedido já foi pago ou cancelado.");
        }
        if (pedido.getItens().isEmpty()) {
            throw new IllegalStateException("O pedido não tem ingressos.");
        }

        Pagamento pagamento = fabrica.novoPagamento(pedido.getValorTotal());
        pagamento.processar();
        pedido.setPagamento(pagamento);
        if (pagamento.isAprovado()) {
            confirmarIngressos(pedido);
        }
        return pagamento;
    }

    public void confirmarBoleto(Pedido pedido) {
        if (!pedido.isAberto() || !(pedido.getPagamento() instanceof PagamentoBoleto)) {
            throw new IllegalStateException("Este pedido não tem boleto aguardando pagamento.");
        }

        PagamentoBoleto boleto = (PagamentoBoleto) pedido.getPagamento();
        boleto.confirmarCompensacao();
        confirmarIngressos(pedido);
    }

    // Pedido ainda não pago: cancela todos os ingressos e devolve ao estoque.
    public void cancelarPedido(Pedido pedido) {
        if (!pedido.isAberto()) {
            throw new IllegalStateException("Só é possível cancelar pedidos que ainda não foram pagos.");
        }
        for (IIngresso item : pedido.getItens()) {
            item.cancelar();
        }
        pedido.cancelar();
    }

    // Ingresso já pago: cancela só ele e devolve o valor ao comprador.
    public double cancelarIngresso(String codigo) {
        Pedido pedido = buscarPedidoDoIngresso(codigo);
        if (!pedido.isPago()) {
            throw new IllegalStateException("O pedido deste ingresso ainda não foi pago; cancele o pedido inteiro.");
        }

        IIngresso ingresso = buscarIngresso(codigo);
        ingresso.cancelar();
        return ingresso.getPreco();
    }

    public IIngresso buscarIngresso(String codigo) {
        for (Pedido pedido : pedidos) {
            for (IIngresso item : pedido.getItens()) {
                if (item.getId().equalsIgnoreCase(codigo)) {
                    return item;
                }
            }
        }
        throw new IllegalArgumentException("Ingresso " + codigo + " não encontrado.");
    }

    public List<Pedido> listarPedidos(Usuario comprador) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getComprador() == comprador) {
                resultado.add(pedido);
            }
        }
        return resultado;
    }

    private IIngresso aplicarAdicionais(IIngresso ingresso, Set<Adicional> adicionais) {
        // Percorre na ordem do enum para garantir que o seguro seja aplicado por último.
        for (Adicional adicional : Adicional.values()) {
            if (!adicionais.contains(adicional)) {
                continue;
            }
            switch (adicional) {
                case AREA_VIP:
                    ingresso = new AreaVipDecorator(ingresso);
                    break;
                case ESTACIONAMENTO:
                    ingresso = new EstacionamentoDecorator(ingresso);
                    break;
                case KIT_OPEN_BAR:
                    ingresso = new KitOpenBarDecorator(ingresso);
                    break;
                case SEGURO:
                    ingresso = new SeguroIngressoDecorator(ingresso);
                    break;
            }
        }
        return ingresso;
    }

    private void confirmarIngressos(Pedido pedido) {
        for (IIngresso item : pedido.getItens()) {
            item.pagar();
        }
    }

    private Pedido buscarPedidoDoIngresso(String codigo) {
        for (Pedido pedido : pedidos) {
            for (IIngresso item : pedido.getItens()) {
                if (item.getId().equalsIgnoreCase(codigo)) {
                    return pedido;
                }
            }
        }
        throw new IllegalArgumentException("Ingresso " + codigo + " não encontrado.");
    }
}
