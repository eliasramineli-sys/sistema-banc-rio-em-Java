# Banco Avançado — Java ☕

Projeto de portfólio de um sistema bancário em Java, organizado em camadas e com testes automatizados. **Simulador educacional, não destinado ao uso com dinheiro real.**

## Funcionalidades
- Abertura de contas e validação de dados
- Depósitos, saques e transferências
- Valores monetários precisos com `BigDecimal`
- Saldo, listagem de contas e extrato de transações
- Identificador compartilhado entre os dois lançamentos de uma transferência
- Regras de negócio e tratamento de erros
- Operações serializadas por bloqueio no serviço (adequado ao repositório em memória deste exemplo)
- Testes com JUnit 5

## Tecnologias
Java 21+ (compatível com JDK 25), Maven 3.9+, JUnit 5. Sem banco de dados externo.

## Executar
```bash
mvn clean test
mvn exec:java
```

No Eclipse: **File > Import > Maven > Existing Maven Projects**, selecione a pasta e execute `Main.java`.

## Estrutura
```text
src/main/java/br/com/portfolio/banco/
  app/          # Menu interativo
  domain/       # Conta, Transacao, TipoTransacao
  exception/    # Exceções de negócio
  repository/   # Interface e armazenamento em memória
  service/      # Regras de negócio
src/test/java/  # Testes unitários
```

## Exemplo
1. Abrir conta `1001` para Ana e `1002` para Carlos.
2. Depositar `500.00` na conta `1001`.
3. Transferir `200.00` para a conta `1002`.
4. Consultar saldos: `300.00` e `200.00`.
5. Visualizar extratos.

## Limitações e próximos passos
**Os dados são perdidos ao encerrar o programa.** Não há autenticação, API HTTP, banco de dados persistente, transações ACID nem infraestrutura para produção. O próximo estágio é uma API REST com Spring Boot, PostgreSQL, Flyway, autenticação e testes de integração. Não utilize dados pessoais ou credenciais reais.

## Publicar no GitHub
Este projeto está publicado em https://github.com/eliasramineli-sys/sistema-banc-rio-em-Java .

## Licença
MIT — veja `LICENSE`.
