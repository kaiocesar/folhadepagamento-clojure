# CLAUDE.md

Projeto: **folhadepagamento-clojure** — cálculo de folha de pagamento CLT (Brasil), em Clojure.

## Contexto obrigatório

Antes de qualquer tarefa que toque cálculo de INSS, IRRF ou FGTS, leia:

- @domain-rules.md — regras de negócio, tabelas vigentes e fórmulas (fonte da verdade dos valores)
- @steering.md — convenções de código deste projeto (namespaces, estilo, testes)

Se a tarefa não envolver cálculo (ex.: ajuste de build, documentação), esses dois arquivos podem ser
ignorados.

## Stack

- Clojure + Leiningen (`project.clj` na raiz)
- Testes: `clojure.test`

## Comandos

```
lein test      # roda a suíte inteira — obrigatório passar 100% antes de considerar qualquer tarefa concluída
lein repl       # exploração manual
```

## Regras inegociáveis

- Nenhuma fórmula de INSS/IRRF/FGTS é calculada "de cabeça" — sempre consultar `domain-rules.md`.
  Se a competência (mês/ano) não for a vigente ali, **parar e perguntar** qual tabela usar.
- Valores monetários: nunca `double`/`float`. Usar `BigDecimal` (literais `M`).
- Toda mudança em fórmula de imposto cita a fonte legal (Portaria/Lei) no commit.
- Refactor de estilo é tarefa separada — não misturar com correção de bug ou nova feature na mesma
  alteração.
- Antes de dar uma tarefa por concluída: `lein test` passando, incluindo testes pré-existentes.

## O que não fazer

- Não editar `domain-rules.md` a partir do código — é o código que se ajusta ao arquivo, nunca o inverso.
- Não adicionar dependência nova sem checar se `project.clj` já cobre a necessidade.
- Não "corrigir" arredondamento ad-hoc fora da regra descrita em `domain-rules.md` §5.

## Specs de feature

Tarefas maiores (nova regra, alteração de cálculo, novo relatório) devem ter spec própria em
`specs/<nome-da-feature>/` antes da implementação, seguindo o padrão requirements → design → tasks.
Não implementar direto uma feature grande sem isso.
