package br.com.portfolio.banco.app;

import br.com.portfolio.banco.domain.Conta;
import br.com.portfolio.banco.domain.Transacao;
import br.com.portfolio.banco.exception.RegraNegocioException;
import br.com.portfolio.banco.repository.ContaEmMemoriaRepository;
import br.com.portfolio.banco.service.BancoService;
import java.math.BigDecimal;
import java.util.Scanner;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        BancoService banco = new BancoService(new ContaEmMemoriaRepository());
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n=== BANCO AVANÇADO ===");
                System.out.println("1. Abrir conta | 2. Depositar | 3. Sacar | 4. Transferir");
                System.out.println("5. Consultar saldo | 6. Extrato | 7. Listar contas | 0. Sair");
                System.out.print("Opção: ");
                String opcao = scanner.nextLine().trim();
                if (opcao.equals("0")) break;
                try {
                    switch (opcao) {
                        case "1" -> {
                            long numero = numero(scanner, "Número: ");
                            System.out.print("Titular: ");
                            Conta conta = banco.abrirConta(numero, scanner.nextLine());
                            System.out.println("Conta aberta: " + conta.getNumero());
                        }
                        case "2" -> { banco.depositar(numero(scanner, "Conta: "), valor(scanner)); System.out.println("Depósito realizado."); }
                        case "3" -> { banco.sacar(numero(scanner, "Conta: "), valor(scanner)); System.out.println("Saque realizado."); }
                        case "4" -> {
                            long origem = numero(scanner, "Origem: ");
                            long destino = numero(scanner, "Destino: ");
                            banco.transferir(origem, destino, valor(scanner));
                            System.out.println("Transferência realizada.");
                        }
                        case "5" -> { Conta conta = banco.consultar(numero(scanner, "Conta: ")); System.out.println(conta.getTitular() + " | Saldo: R$ " + conta.getSaldo()); }
                        case "6" -> {
                            for (Transacao t : banco.extrato(numero(scanner, "Conta: ")))
                                System.out.printf("%s | %s | R$ %s | %s | Operação: %s%n", t.dataHora(), t.tipo(), t.valor(), t.descricao(), t.operacaoId());
                        }
                        case "7" -> banco.listarContas().stream().sorted(java.util.Comparator.comparingLong(Conta::getNumero))
                                .forEach(c -> System.out.println(c.getNumero() + " | " + c.getTitular() + " | R$ " + c.getSaldo()));
                        default -> System.out.println("Opção inválida.");
                    }
                } catch (RegraNegocioException | NumberFormatException ex) {
                    System.out.println("Erro: " + ex.getMessage());
                }
            }
        }
    }
    private static long numero(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine().trim());
    }
    private static BigDecimal valor(Scanner scanner) {
        System.out.print("Valor (ex.: 100.50): R$ ");
        return new BigDecimal(scanner.nextLine().trim().replace(',', '.'));
    }
}
