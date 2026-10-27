# TicketFlow Console

O TicketFlow é um sistema de venda de ingressos para eventos que roda direto no terminal. Ele foi desenvolvido como trabalho final da disciplina de Padrões de Projeto, em Java puro, e a ideia é mostrar seis padrões do catálogo GoF (dois criacionais, dois estruturais e dois comportamentais) resolvendo problemas que aparecem de verdade nesse tipo de negócio.

O funcionamento é parecido com o de uma bilheteria online. Um organizador cadastra o evento e divide os ingressos em lotes, cada um com a sua regra de preço. O comprador escolhe o lote, pode incluir adicionais como área VIP ou estacionamento e paga com Pix, cartão de crédito ou boleto. No dia do evento, a portaria valida o ingresso pelo código. No meio do caminho o sistema cuida das regras que se espera de uma bilheteria: não vende para menores em eventos com classificação etária, não deixa vender além do estoque do lote, devolve o ingresso ao estoque quando há cancelamento e não aceita o mesmo ingresso duas vezes na entrada.

## Padrões utilizados

| Padrão | Categoria | Problema de negócio que ele resolve |
|---|---|---|
| Builder | Criacional | Um evento reúne muitas informações, algumas opcionais (idade mínima, open bar) e uma lista de lotes. O `EventoBuilder` monta o evento aos poucos e só o entrega depois de conferir que ele tem nome, local, data futura e pelo menos um lote. Assim nenhum evento incompleto chega ao sistema. |
| Factory Method | Criacional | Cada meio de pagamento nasce de um jeito: o cartão precisa das parcelas e de um gateway, o boleto calcula o vencimento, o Pix gera o código copia e cola. A `PagamentoFactory` concentra o que é comum a todos (validar o valor, gerar o identificador da transação) e deixa para cada subclasse a decisão de qual pagamento criar. Um novo meio de pagamento entra no sistema com uma classe nova, sem mexer nas existentes. |
| Decorator | Estrutural | O comprador pode somar ao ingresso quantos adicionais quiser: área VIP, estacionamento, kit open bar e seguro. Cada adicional envolve o ingresso e acrescenta seu valor e sua descrição, então qualquer combinação funciona sem precisar de uma classe para cada caso. O seguro mostra bem a ideia, porque cobra 10% sobre o valor que o ingresso já tem com os outros adicionais. |
| Adapter | Estrutural | O pagamento com cartão passa por gateways externos (simulados no pacote `externo`), e cada um tem uma interface diferente: o PagFácil trabalha com valores em centavos e responde com um código de status, enquanto o CashNow recebe tudo como texto e responde "APPROVED" ou "DECLINED". Os adapters traduzem essas APIs para a interface `GatewayPagamento`, que é a única que o sistema conhece. Trocar de gateway não muda nada no pagamento. |
| Strategy | Comportamental | Cada lote tem a sua regra de preço: normal, promocional com desconto percentual ou dinâmica, em que o preço sobe 10% a cada 25% do lote vendido. O lote só pergunta o preço para a estratégia que recebeu, e o organizador pode trocar essa regra a qualquer momento pelo menu. |
| State | Comportamental | Um ingresso passa por pendente, pago, utilizado ou cancelado, e o que pode ser feito com ele depende da fase em que está. Cada estado é uma classe que sabe quais ações aceita e para onde o ingresso vai depois. Isso evita uma sequência de `if` espalhada pelo código e impede, por exemplo, check-in de um ingresso que ainda não foi pago ou cancelamento de um que já foi usado. |

Todas as classes que participam de um padrão indicam seu papel no Javadoc, com a anotação `@note Papel no Padrão`.

## Organização do código

O código fica em `src/main/java`, separado em camadas. O pacote `model` guarda as classes de domínio (evento, lote, ingresso, pedido, pagamentos e usuário). O pacote `pattern` tem uma pasta para cada padrão. O pacote `service` concentra as regras de negócio que juntam tudo, como o fluxo de compra e o check-in. O `Main.java` cuida apenas do menu, ou seja, da leitura do teclado e da exibição dos resultados. Os dados ficam em memória enquanto o programa está aberto.

```
src/main/java
├── Main.java
├── externo/            gateways de pagamento simulados (o lado "de fora" do Adapter)
├── model/              domínio
├── pattern/
│   ├── adapter/
│   ├── builder/
│   ├── decorator/
│   ├── factory/
│   ├── state/
│   └── strategy/
└── service/            usuários, eventos, compra e check-in
```

## Como executar

É preciso ter o JDK 11 ou mais recente instalado (o projeto foi desenvolvido e testado com o JDK 26). Não há dependências externas.

No Windows, pelo PowerShell, a partir da pasta do projeto:

```
.\run.ps1
```

Se o PowerShell bloquear a execução de scripts, use `powershell -ExecutionPolicy Bypass -File run.ps1`.

No Linux, no macOS ou no Git Bash:

```
sh run.sh
```

Os dois scripts compilam o projeto em `target/classes` e iniciam o menu. Se preferir fazer à mão, o equivalente é compilar todos os arquivos de `src/main/java` com `javac -encoding UTF-8 -d target/classes` e depois rodar `java -cp target/classes Main`.

## Testando os padrões pelo menu

O sistema já abre com alguns dados de exemplo para que dê para testar sem cadastrar nada. São três usuários: a Carla, que é organizadora; a Ana, que é a usuária inicial; e o Pedro, de 16 anos. Também há dois eventos: o Rock in Floripa, para maiores de 18, com open bar e três lotes, um com cada regra de preço; e o Tech Summit, com classificação livre e sem open bar. Sempre que um padrão entra em ação, o terminal mostra uma linha começando com o nome dele entre colchetes, como `[STRATEGY]` ou `[DECORATOR]`.

Um bom caminho para ver tudo funcionando é começar pela opção 1, que lista os eventos e mostra o Strategy calculando o preço de cada lote. Em seguida, pela opção 2, compre um ingresso do Camarote do Rock in Floripa com área VIP e seguro e veja os decorators sendo aplicados. Na hora de pagar, escolha cartão em 10 parcelas pelo CashNow. O Factory Method cria o pagamento, o Adapter traduz a chamada e o gateway recusa, porque esse gateway só aceita até 6 parcelas. O pedido continua pendente, então pela opção 4 dá para pagar de novo, agora com boleto, e pela opção 5 confirmar a compensação. Nesse momento os ingressos passam de pendente para pago, que é o State em ação.

A opção 3 mostra os pedidos com o código de cada ingresso. Com esse código, a opção 7 faz o check-in na portaria. Tente fazer o check-in duas vezes para ver o segundo ser recusado. A opção 6 cancela um ingresso pago, devolve o valor e o lugar volta ao estoque do lote.

Para ver as outras regras, use a opção 10 para trocar de usuário. Como Pedro, tente comprar para o Rock in Floripa e o sistema recusa pela idade. Como Carla, cadastre um evento novo pela opção 8, que é onde o Builder trabalha, e use a opção 9 para trocar a regra de preço de um lote e ver o preço mudar na hora.
