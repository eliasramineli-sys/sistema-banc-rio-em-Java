package br.com.portfolio.banco.domain;

import br.com.portfolio.banco.exception.RegraNegocioException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Conta {
    private final long numero;
    private final String titular;
    private final LocalDate abertura;
    private BigDecimal saldo = new BigDecimal("0.00");
    private final List<Transacao> extrato = new ArrayList<>();

    public Conta(long numero, String titular) {
        if (numero <= 0) throw new RegraNegocioException("Número da conta inválido.");
        if (titular == null || titular.isBlank()) throw new RegraNegocioException("Titular obrigatório.");
        this.numero = numero;
        this.titular = titular.trim();
        this.abertura = LocalDate.now();
    }

    public long getNumero() { return numero; }
    public String getTitular() { return titular; }
    public LocalDate getAbertura() { return abertura; }
    public synchronized BigDecimal getSaldo() { return saldo; }
    public synchronized List<Transacao> getExtrato() { return List.copyOf(extrato); }

    public synchronized void creditar(BigDecimal valor, Transacao transacao) {
        saldo = saldo.add(valor);
        extrato.add(transacao);
    }

    public synchronized void debitar(BigDecimal valor, Transacao transacao) {
        if (saldo.compareTo(valor) < 0) throw new RegraNegocioException("Saldo insuficiente.");
        saldo = saldo.subtract(valor);
        extrato.add(transacao);
    }
}
