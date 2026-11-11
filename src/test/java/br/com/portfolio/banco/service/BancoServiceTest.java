package br.com.portfolio.banco.service;

import br.com.portfolio.banco.domain.TipoTransacao;
import br.com.portfolio.banco.exception.RegraNegocioException;
import br.com.portfolio.banco.repository.ContaEmMemoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class BancoServiceTest {
    private BancoService banco;
    @BeforeEach void iniciar() {
        banco = new BancoService(new ContaEmMemoriaRepository());
        banco.abrirConta(1001, "Ana");
        banco.abrirConta(1002, "Carlos");
    }
    @Test void depositoESaqueAtualizamSaldo() {
        banco.depositar(1001, new BigDecimal("100.00"));
        banco.sacar(1001, new BigDecimal("40.50"));
        assertEquals(new BigDecimal("59.50"), banco.consultar(1001).getSaldo());
        assertEquals(2, banco.extrato(1001).size());
    }
    @Test void transferenciaRegistraAmbosOsLados() {
        banco.depositar(1001, new BigDecimal("200.00"));
        banco.transferir(1001, 1002, new BigDecimal("75.25"));
        assertEquals(new BigDecimal("124.75"), banco.consultar(1001).getSaldo());
        assertEquals(new BigDecimal("75.25"), banco.consultar(1002).getSaldo());
        var enviada = banco.extrato(1001).get(1);
        var recebida = banco.extrato(1002).get(0);
        assertEquals(TipoTransacao.TRANSFERENCIA_ENVIADA, enviada.tipo());
        assertEquals(TipoTransacao.TRANSFERENCIA_RECEBIDA, recebida.tipo());
        assertEquals(enviada.operacaoId(), recebida.operacaoId());
    }
    @Test void transferenciaSemSaldoNaoAlteraContas() {
        assertThrows(RegraNegocioException.class, () -> banco.transferir(1001, 1002, new BigDecimal("10")));
        assertEquals(new BigDecimal("0.00"), banco.consultar(1001).getSaldo());
        assertEquals(new BigDecimal("0.00"), banco.consultar(1002).getSaldo());
    }
    @Test void validaValoresEContasDuplicadas() {
        assertThrows(RegraNegocioException.class, () -> banco.depositar(1001, new BigDecimal("0")));
        assertThrows(RegraNegocioException.class, () -> banco.depositar(1001, new BigDecimal("1.001")));
        assertThrows(RegraNegocioException.class, () -> banco.abrirConta(1001, "Outra"));
        assertThrows(RegraNegocioException.class, () -> banco.transferir(1001, 1001, BigDecimal.ONE));
    }
}
