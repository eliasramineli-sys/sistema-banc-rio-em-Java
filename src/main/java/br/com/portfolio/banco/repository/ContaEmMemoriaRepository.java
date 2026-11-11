package br.com.portfolio.banco.repository;

import br.com.portfolio.banco.domain.Conta;
import br.com.portfolio.banco.exception.RegraNegocioException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ContaEmMemoriaRepository implements ContaRepository {
    private final ConcurrentMap<Long, Conta> contas = new ConcurrentHashMap<>();

    @Override public void salvar(Conta conta) {
        if (contas.putIfAbsent(conta.getNumero(), conta) != null)
            throw new RegraNegocioException("Número de conta já cadastrado.");
    }
    @Override public Optional<Conta> buscar(long numero) { return Optional.ofNullable(contas.get(numero)); }
    @Override public Collection<Conta> listar() { return List.copyOf(contas.values()); }
}
