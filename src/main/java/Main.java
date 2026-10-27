import java.util.Scanner;

/** Ponto de entrada: menu interativo do TicketFlow Console. */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            System.out.println();
            System.out.println("===== TicketFlow Console =====");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            if (!scanner.hasNextLine()) {
                break;
            }
            String opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "0":
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

        System.out.println("Até logo!");
        scanner.close();
    }
}
