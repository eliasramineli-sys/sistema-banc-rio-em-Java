package br.com.portfolio.banco.repository;

import br.com.portfolio.banco.domain.Conta;
import java.util.Collection;
import java.util.Optional;

public interface ContaRepository {
    void salvar(Conta conta);
    Optional<Conta> buscar(long numero);
    Collection<Conta> listar();
}
