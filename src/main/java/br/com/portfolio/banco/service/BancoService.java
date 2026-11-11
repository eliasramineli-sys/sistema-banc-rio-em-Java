package br.com.portfolio.banco.service;

import br.com.portfolio.banco.domain.*;
import br.com.portfolio.banco.exception.RegraNegocioException;
import br.com.portfolio.banco.repository.ContaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class BancoService {
    private final ContaRepository repository;
    private final Object lockOperacoes = new Object();

    public BancoService(ContaRepository repository) { this.repository = java.util.Objects.requireNonNull(repository); }

    public Conta abrirConta(long numero, String titular) {
        Conta conta = new Conta(numero, titular);
        repository.salvar(conta);
        return conta;
    }

    public Conta consultar(long numero) {
        return repository.buscar(numero).orElseThrow(() -> new RegraNegocioException("Conta não encontrada: " + numero));
    }

    public Collection<Conta> listarContas() { return repository.listar(); }
    public List<Transacao> extrato(long numero) { return consultar(numero).getExtrato(); }

    public void depositar(long numero, BigDecimal valor) {
        BigDecimal quantia = validarValor(valor);
        synchronized (lockOperacoes) {
            Conta conta = consultar(numero);
            UUID operacao = UUID.randomUUID();
            conta.creditar(quantia, transacao(operacao, TipoTransacao.DEPOSITO, quantia, "Depósito"));
        }
    }

    public void sacar(long numero, BigDecimal valor) {
        BigDecimal quantia = validarValor(valor);
        synchronized (lockOperacoes) {
            Conta conta = consultar(numero);
            UUID operacao = UUID.randomUUID();
            conta.debitar(quantia, transacao(operacao, TipoTransacao.SAQUE, quantia, "Saque"));
        }
    }

    public void transferir(long origemNumero, long destinoNumero, BigDecimal valor) {
        BigDecimal quantia = validarValor(valor);
        if (origemNumero == destinoNumero) throw new RegraNegocioException("Transferência para a mesma conta não permitida.");
        synchronized (lockOperacoes) {
            Conta origem = consultar(origemNumero);
            Conta destino = consultar(destinoNumero);
            UUID operacao = UUID.randomUUID();
            origem.debitar(quantia, transacao(operacao, TipoTransacao.TRANSFERENCIA_ENVIADA, quantia, "Para conta " + destinoNumero));
            destino.creditar(quantia, transacao(operacao, TipoTransacao.TRANSFERENCIA_RECEBIDA, quantia, "Da conta " + origemNumero));
        }
    }

    private static BigDecimal validarValor(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0 || valor.scale() > 2)
            throw new RegraNegocioException("Informe valor positivo com no máximo duas casas decimais.");
        return valor.setScale(2);
    }

    private static Transacao transacao(UUID operacao, TipoTransacao tipo, BigDecimal valor, String descricao) {
        return new Transacao(UUID.randomUUID(), operacao, tipo, valor, LocalDateTime.now(), descricao);
    }
}
