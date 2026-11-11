package br.com.portfolio.banco.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Transacao(UUID id, UUID operacaoId, TipoTransacao tipo, BigDecimal valor,
                        LocalDateTime dataHora, String descricao) { }
