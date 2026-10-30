import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import model.Evento;
import model.Lote;
import model.Pagamento;
import model.PagamentoBoleto;
import model.PagamentoCartao;
import model.PagamentoPix;
import model.Pedido;
import model.Usuario;
import pattern.adapter.CashNowAdapter;
import pattern.adapter.GatewayPagamento;
import pattern.adapter.PagFacilAdapter;
import pattern.builder.EventoBuilder;
import pattern.decorator.IIngresso;
import pattern.factory.PagamentoBoletoFactory;
import pattern.factory.PagamentoCartaoFactory;
import pattern.factory.PagamentoFactory;
import pattern.factory.PagamentoPixFactory;
import pattern.strategy.CalculoPrecoStrategy;
import pattern.strategy.PrecoDinamicoStrategy;
import pattern.strategy.PrecoNormalStrategy;
import pattern.strategy.PrecoPromocionalStrategy;
import service.CheckInService;
import service.CompraService;
import service.CompraService.Adicional;
import service.EventoService;
import service.UsuarioService;

/** @note Papel no Padrão: Client (Strategy, Factory Method e Adapter). Menu interativo do TicketFlow Console. */
public class Main {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Lê o teclado na codificação do terminal (no Windows é cp850), senão acentos digitados chegam errados.
    private static final Scanner scanner = new Scanner(System.in,
            System.getProperty("stdin.encoding", Charset.defaultCharset().name()));
    private static final UsuarioService usuarioService = new UsuarioService();
    private static final EventoService eventoService = new EventoService();
    private static final CompraService compraService = new CompraService();
    private static final CheckInService checkInService = new CheckInService(compraService);

    private static Usuario usuarioAtual;

    public static void main(String[] args) {
        System.out.println("Carregando dados de exemplo...");
        carregarDadosIniciais();

        boolean executando = true;
        while (executando) {
            mostrarMenu();
            String opcao = lerLinha();
            try {
                executando = executarOpcao(opcao);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        System.out.println("Até logo!");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== TicketFlow Console =====");
        System.out.println("Usuário atual: " + usuarioAtual.getNome()
                + (usuarioAtual.isOrganizador() ? " (organizador)" : ""));
        System.out.println(" 1 - Ver eventos e lotes");
        System.out.println(" 2 - Comprar ingressos");
        System.out.println(" 3 - Meus pedidos");
        System.out.println(" 4 - Pagar pedido pendente");
        System.out.println(" 5 - Confirmar pagamento de boleto");
        System.out.println(" 6 - Cancelar pedido ou ingresso");
        System.out.println(" 7 - Check-in na portaria");
        System.out.println(" 8 - Cadastrar evento (organizador)");
        System.out.println(" 9 - Alterar regra de preço de um lote (organizador)");
        System.out.println("10 - Trocar de usuário");
        System.out.println("11 - Cadastrar usuário");
        System.out.println(" 0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static boolean executarOpcao(String opcao) {
        switch (opcao) {
            case "1":
                listarEventos();
                break;
            case "2":
                comprarIngressos();
                break;
            case "3":
                listarMeusPedidos();
                break;
            case "4":
                pagarPedidoPendente();
                break;
            case "5":
                confirmarBoleto();
                break;
            case "6":
                cancelar();
                break;
            case "7":
                fazerCheckIn();
                break;
            case "8":
                cadastrarEvento();
                break;
            case "9":
                alterarRegraDePreco();
                break;
            case "10":
                trocarUsuario();
                break;
            case "11":
                cadastrarUsuario();
                break;
            case "0":
                return false;
            default:
                System.out.println("Opção inválida.");
        }
        return true;
    }

    // ===== Eventos =====

    private static void listarEventos() {
        for (Evento evento : eventoService.listar()) {
            mostrarEvento(evento);
            mostrarLotes(evento);
        }
    }

    private static void mostrarEvento(Evento evento) {
        System.out.println();
        System.out.println("#" + evento.getId() + " " + evento.getNome() + " | " + evento.getLocal()
                + " | " + evento.getDataHora().format(FORMATO_DATA_HORA)
                + (evento.getIdadeMinima() > 0 ? " | " + evento.getIdadeMinima() + "+" : " | Livre")
                + (evento.isOpenBar() ? " | Open bar" : ""));
    }

    private static void mostrarLotes(Evento evento) {
        for (Lote lote : evento.getLotes()) {
            String disponibilidade = lote.isEsgotado() ? "ESGOTADO"
                    : lote.getQuantidadeDisponivel() + "/" + lote.getQuantidadeTotal() + " disponíveis";
            double preco = lote.calcularPreco();
            System.out.println("   Lote " + lote.getNumero() + " - " + lote.getNome() + " | " + moeda(preco)
                    + " | " + disponibilidade);
        }
    }

    private static Evento escolherEvento() {
        for (Evento evento : eventoService.listar()) {
            System.out.println("#" + evento.getId() + " " + evento.getNome());
        }
        return eventoService.buscarPorId(lerInteiro("Número do evento: "));
    }

    private static void cadastrarEvento() {
        if (!usuarioAtual.isOrganizador()) {
            System.out.println("Apenas organizadores podem cadastrar eventos. Troque de usuário (opção 10).");
            return;
        }

        EventoBuilder builder = new EventoBuilder()
                .comNome(lerTexto("Nome do evento: "))
                .comLocal(lerTexto("Local: "))
                .comDataHora(lerDataHoraFutura("Data e hora (dd/MM/aaaa HH:mm): "))
                .comIdadeMinima(lerInteiroEntre("Idade mínima (0 para livre): ", 0, 21));
        if (lerSimNao("Oferece open bar? (s/n): ")) {
            builder.comOpenBar();
        }

        int numeroLote = 1;
        do {
            System.out.println("-- Lote " + numeroLote);
            String nome = lerTexto("Nome do lote: ");
            double precoBase = lerDecimal("Preço base: ");
            int quantidade = lerInteiroEntre("Quantidade de ingressos: ", 1, 100000);
            builder.adicionarLote(new Lote(numeroLote, nome, precoBase, quantidade, escolherEstrategia()));
            numeroLote++;
        } while (lerSimNao("Adicionar outro lote? (s/n): "));

        Evento evento = eventoService.criarEvento(usuarioAtual, builder);
        System.out.println("Evento #" + evento.getId() + " cadastrado com sucesso!");
    }

    private static void alterarRegraDePreco() {
        if (!usuarioAtual.isOrganizador()) {
            System.out.println("Apenas organizadores podem alterar preços. Troque de usuário (opção 10).");
            return;
        }

        Evento evento = escolherEvento();
        mostrarLotes(evento);
        Lote lote = evento.buscarLote(lerInteiro("Número do lote: "));
        lote.setEstrategiaPreco(escolherEstrategia());
        System.out.println("Novo preço do lote " + lote.getNome() + ": " + moeda(lote.calcularPreco()));
    }

    private static CalculoPrecoStrategy escolherEstrategia() {
        System.out.println("Regra de preço: 1 - Normal | 2 - Promocional | 3 - Dinâmico (sobe conforme vende)");
        int opcao = lerInteiroEntre("Escolha: ", 1, 3);
        if (opcao == 2) {
            return new PrecoPromocionalStrategy(lerInteiroEntre("Desconto (%): ", 1, 99));
        }
        if (opcao == 3) {
            return new PrecoDinamicoStrategy();
        }
        return new PrecoNormalStrategy();
    }

    // ===== Compra e pagamento =====

    private static void comprarIngressos() {
        Evento evento = escolherEvento();
        mostrarEvento(evento);
        mostrarLotes(evento);

        Pedido pedido = compraService.criarPedido(usuarioAtual);
        do {
            int numeroLote = lerInteiro("Número do lote: ");
            Set<Adicional> adicionais = escolherAdicionais(evento);
            try {
                IIngresso ingresso = compraService.adicionarIngresso(pedido, evento, numeroLote, adicionais);
                System.out.println("Adicionado: " + ingresso.getDescricao() + " - " + moeda(ingresso.getPreco()));
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Não foi possível adicionar: " + e.getMessage());
            }
        } while (lerSimNao("Adicionar outro ingresso a este pedido? (s/n): "));

        if (pedido.getItens().isEmpty()) {
            System.out.println("Nenhum ingresso foi adicionado. Compra encerrada.");
            return;
        }

        System.out.println("Total do pedido #" + pedido.getId() + ": " + moeda(pedido.getValorTotal()));
        if (lerSimNao("Pagar agora? (s/n): ")) {
            pagar(pedido);
        } else {
            System.out.println("Pedido salvo. Você pode pagar depois pela opção 4.");
        }
    }

    private static Set<Adicional> escolherAdicionais(Evento evento) {
        System.out.println("Adicionais: 1 - Área VIP (+R$ 150) | 2 - Estacionamento (+R$ 40)"
                + (evento.isOpenBar() ? " | 3 - Kit Open Bar (+R$ 120)" : "")
                + " | 4 - Seguro (+10%)");
        System.out.print("Digite os números separados por vírgula (ou Enter para nenhum): ");

        Set<Adicional> adicionais = EnumSet.noneOf(Adicional.class);
        for (String parte : lerLinha().split(",")) {
            switch (parte.trim()) {
                case "1":
                    adicionais.add(Adicional.AREA_VIP);
                    break;
                case "2":
                    adicionais.add(Adicional.ESTACIONAMENTO);
                    break;
                case "3":
                    adicionais.add(Adicional.KIT_OPEN_BAR);
                    break;
                case "4":
                    adicionais.add(Adicional.SEGURO);
                    break;
                case "":
                    break;
                default:
                    System.out.println("Adicional \"" + parte.trim() + "\" ignorado.");
            }
        }
        return adicionais;
    }

    private static void pagar(Pedido pedido) {
        Pagamento pagamento = compraService.pagar(pedido, escolherFormaPagamento());

        if (pagamento instanceof PagamentoPix) {
            System.out.println("Pagamento aprovado! Código Pix: " + ((PagamentoPix) pagamento).getCodigoCopiaECola());
        } else if (pagamento instanceof PagamentoBoleto) {
            PagamentoBoleto boleto = (PagamentoBoleto) pagamento;
            System.out.println("Boleto gerado: " + boleto.getCodigoBarras() + " | vence em "
                    + boleto.getVencimento().format(FORMATO_DATA) + ". Confirme o pagamento pela opção 5.");
        } else if (pagamento.isAprovado()) {
            PagamentoCartao cartao = (PagamentoCartao) pagamento;
            System.out.println("Pagamento aprovado em " + cartao.getParcelas() + "x de " + moeda(cartao.getValorParcela()) + ".");
        } else {
            System.out.println("Pagamento recusado pelo gateway. O pedido continua pendente (opção 4 para tentar de novo).");
        }
    }

    private static PagamentoFactory escolherFormaPagamento() {
        System.out.println("Forma de pagamento: 1 - Pix | 2 - Cartão de crédito | 3 - Boleto");
        int opcao = lerInteiroEntre("Escolha: ", 1, 3);
        if (opcao == 1) {
            return new PagamentoPixFactory();
        }
        if (opcao == 3) {
            return new PagamentoBoletoFactory();
        }

        int parcelas = lerInteiroEntre("Número de parcelas (1 a 12): ", 1, 12);
        System.out.println("Gateway: 1 - PagFácil (limite de R$ 5.000) | 2 - CashNow (até 6 parcelas)");
        GatewayPagamento gateway = lerInteiroEntre("Escolha: ", 1, 2) == 1 ? new PagFacilAdapter() : new CashNowAdapter();
        return new PagamentoCartaoFactory(parcelas, gateway);
    }

    private static void pagarPedidoPendente() {
        Pedido pedido = escolherPedidoAberto();
        if (pedido != null) {
            pagar(pedido);
        }
    }

    private static void confirmarBoleto() {
        Pedido pedido = escolherPedidoAberto();
        if (pedido != null) {
            compraService.confirmarBoleto(pedido);
            System.out.println("Boleto compensado! Pedido #" + pedido.getId() + " pago.");
        }
    }

    // ===== Pedidos, cancelamento e check-in =====

    private static void listarMeusPedidos() {
        List<Pedido> pedidos = compraService.listarPedidos(usuarioAtual);
        if (pedidos.isEmpty()) {
            System.out.println("Você ainda não tem pedidos.");
            return;
        }

        for (Pedido pedido : pedidos) {
            System.out.println();
            System.out.println("Pedido #" + pedido.getId() + " | " + statusDoPedido(pedido)
                    + " | Total " + moeda(pedido.getValorTotal()));
            for (IIngresso item : pedido.getItens()) {
                System.out.println("   [" + item.getId() + "] " + item.getDescricao() + " | "
                        + moeda(item.getPreco()) + " | " + item.getEstado().getNomeEstado());
            }
        }
    }

    private static String statusDoPedido(Pedido pedido) {
        if (pedido.isCancelado()) {
            return "Cancelado";
        }
        if (pedido.isPago()) {
            return "Pago via " + pedido.getPagamento().getMetodo();
        }
        if (pedido.getPagamento() instanceof PagamentoBoleto) {
            return "Aguardando compensação do boleto";
        }
        return "Aguardando pagamento";
    }

    private static Pedido escolherPedidoAberto() {
        List<Pedido> abertos = new ArrayList<>();
        for (Pedido pedido : compraService.listarPedidos(usuarioAtual)) {
            if (pedido.isAberto()) {
                abertos.add(pedido);
                System.out.println("Pedido #" + pedido.getId() + " | " + statusDoPedido(pedido)
                        + " | Total " + moeda(pedido.getValorTotal()));
            }
        }
        if (abertos.isEmpty()) {
            System.out.println("Você não tem pedidos pendentes.");
            return null;
        }

        int id = lerInteiro("Número do pedido: ");
        for (Pedido pedido : abertos) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }
        System.out.println("Pedido não encontrado entre os pendentes.");
        return null;
    }

    private static void cancelar() {
        System.out.println("1 - Cancelar pedido ainda não pago | 2 - Cancelar ingresso pago (com reembolso)");
        if (lerInteiroEntre("Escolha: ", 1, 2) == 1) {
            Pedido pedido = escolherPedidoAberto();
            if (pedido != null) {
                compraService.cancelarPedido(pedido);
                System.out.println("Pedido #" + pedido.getId() + " cancelado. Os ingressos voltaram ao estoque.");
            }
        } else {
            double reembolso = compraService.cancelarIngresso(lerTexto("Código do ingresso: "));
            System.out.println("Ingresso cancelado. Reembolso de " + moeda(reembolso) + " solicitado.");
        }
    }

    private static void fazerCheckIn() {
        IIngresso ingresso = checkInService.fazerCheckIn(lerTexto("Código do ingresso: "));
        System.out.println("Entrada liberada: " + ingresso.getDescricao());
    }

    // ===== Usuários =====

    private static void trocarUsuario() {
        for (Usuario usuario : usuarioService.listar()) {
            System.out.println("#" + usuario.getId() + " " + usuario.getNome() + " | " + usuario.getIdade() + " anos"
                    + (usuario.isOrganizador() ? " | organizador" : ""));
        }
        usuarioAtual = usuarioService.buscarPorId(lerInteiro("Número do usuário: "));
        System.out.println("Agora você está usando o sistema como " + usuarioAtual.getNome() + ".");
    }

    private static void cadastrarUsuario() {
        Usuario usuario = usuarioService.cadastrar(
                lerTexto("Nome: "),
                lerTexto("CPF: "),
                lerTexto("E-mail: "),
                lerData("Data de nascimento (dd/MM/aaaa): "),
                lerSimNao("É organizador de eventos? (s/n): "));
        System.out.println("Usuário #" + usuario.getId() + " cadastrado. Use a opção 10 para entrar com ele.");
    }

    // ===== Dados de exemplo =====

    private static void carregarDadosIniciais() {
        Usuario organizadora = usuarioService.cadastrar("Carla Mendes", "111.111.111-11", "carla@ticketflow.com",
                LocalDate.of(1985, 3, 12), true);
        usuarioAtual = usuarioService.cadastrar("Ana Souza", "222.222.222-22", "ana@email.com",
                LocalDate.of(2000, 7, 25), false);
        usuarioService.cadastrar("Pedro Lima", "333.333.333-33", "pedro@email.com",
                LocalDate.now().minusYears(16), false);

        eventoService.criarEvento(organizadora, new EventoBuilder()
                .comNome("Rock in Floripa")
                .comLocal("Arena Floripa")
                .comDataHora(LocalDateTime.now().plusDays(30).withHour(20).withMinute(0))
                .comIdadeMinima(18)
                .comOpenBar()
                .adicionarLote(new Lote(1, "Pré-venda", 200, 50, new PrecoPromocionalStrategy(20)))
                .adicionarLote(new Lote(2, "Pista", 200, 100, new PrecoNormalStrategy()))
                .adicionarLote(new Lote(3, "Camarote", 400, 8, new PrecoDinamicoStrategy())));

        eventoService.criarEvento(organizadora, new EventoBuilder()
                .comNome("Tech Summit")
                .comLocal("Centro de Eventos")
                .comDataHora(LocalDateTime.now().plusDays(10).withHour(9).withMinute(0))
                .adicionarLote(new Lote(1, "Inteira", 120, 200, new PrecoNormalStrategy()))
                .adicionarLote(new Lote(2, "Estudante", 120, 50, new PrecoPromocionalStrategy(50))));
    }

    // ===== Leitura do teclado =====

    private static String lerLinha() {
        if (!scanner.hasNextLine()) {
            System.out.println();
            System.out.println("Entrada encerrada. Até logo!");
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = lerLinha();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Este campo não pode ficar vazio.");
        }
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(mensagem));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro.");
            }
        }
    }

    private static int lerInteiroEntre(String mensagem, int minimo, int maximo) {
        while (true) {
            int valor = lerInteiro(mensagem);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("Digite um número entre " + minimo + " e " + maximo + ".");
        }
    }

    private static double lerDecimal(String mensagem) {
        while (true) {
            try {
                double valor = Double.parseDouble(lerTexto(mensagem).replace(",", "."));
                if (Double.isFinite(valor) && valor >= 0) {
                    return valor;
                }
                System.out.println("Digite um valor positivo (ex.: 150,00).");
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor numérico (ex.: 150,00).");
            }
        }
    }

    private static boolean lerSimNao(String mensagem) {
        while (true) {
            String resposta = lerTexto(mensagem).toLowerCase();
            if (resposta.equals("s")) {
                return true;
            }
            if (resposta.equals("n")) {
                return false;
            }
            System.out.println("Responda com s ou n.");
        }
    }

    private static LocalDate lerData(String mensagem) {
        while (true) {
            try {
                return LocalDate.parse(lerTexto(mensagem), FORMATO_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd/MM/aaaa.");
            }
        }
    }

    private static LocalDateTime lerDataHoraFutura(String mensagem) {
        while (true) {
            try {
                LocalDateTime dataHora = LocalDateTime.parse(lerTexto(mensagem), FORMATO_DATA_HORA);
                if (dataHora.isAfter(LocalDateTime.now())) {
                    return dataHora;
                }
                System.out.println("A data do evento deve ser futura.");
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd/MM/aaaa HH:mm.");
            }
        }
    }

    private static String moeda(double valor) {
        return String.format("R$ %.2f", valor);
    }
}
