# steering.md — Convenções de Código do Projeto

> **Propósito:** orientar qualquer IA (Claude Code, Kiro, etc.) sobre *como* escrever/revisar código neste
> repositório, sem repetir a cada prompt. Este arquivo é um ponto de partida — os blocos marcados com
> `<!-- VERIFICAR -->` precisam ser confirmados contra o código real em `src/folhadepagamento` antes de
> considerar este documento definitivo.

---

## 1. Stack e build

- Build tool: **Leiningen** (`project.clj` na raiz).
- Comandos padrão que a IA deve rodar após qualquer mudança:
  ```
  lein test      # roda toda a suíte antes de considerar uma tarefa concluída
  lein repl      # para exploração manual, se necessário
  ```
- <!-- VERIFICAR --> Versão do Clojure e dependências externas (ex.: `clojure.data.json`, alguma lib de datas) —
  confirmar em `project.clj` e listar aqui, pois a IA não deve adicionar uma dependência nova sem checar se
  já existe equivalente no projeto.

---

## 2. Organização de namespaces

- Um namespace por responsabilidade, seguindo o padrão `folhadepagamento.<dominio>`, ex.:
  `folhadepagamento.inss`, `folhadepagamento.irrf`, `folhadepagamento.fgts`, `folhadepagamento.core`.
- Funções de **cálculo puro** (sem I/O) ficam separadas de funções de **apresentação/relatório** (ex.: gerar
  holerite, imprimir, formatar). A IA não deve misturar `println`/I/O dentro de funções de cálculo.
- Cada namespace de domínio (`inss`, `irrf`, `fgts`) deve ter seu par de teste em
  `test/folhadepagamento/<dominio>_test.clj`.
- <!-- VERIFICAR --> Confirmar se o projeto já segue esse padrão de nomes de namespace ou se usa outra
  convenção (ex.: tudo em `core.clj`). Se estiver tudo em um único namespace, a primeira tarefa de melhoria
  deve ser a extração em namespaces por domínio — não fazer isso "de brinde" dentro de outra tarefa.

---

## 3. Estilo de código Clojure

- **Funções puras sempre que possível**: cálculo de folha é um domínio ideal para funções sem efeito colateral
  — entrada (salário, dependentes, etc.) → saída (valor calculado). A IA deve preferir isso a qualquer
  abordagem com estado mutável (`atom`, `ref`) a menos que exista uma razão explícita (ex.: acumulando log).
- **Threading macros** (`->`, `->>`, `as->`) para pipelines de cálculo em vez de `let` aninhados profundos.
- **Destructuring** em argumentos de mapas em vez de múltiplos `get`/`:chave` repetidos:
  ```clojure
  ;; preferir
  (defn calcular-inss [{:keys [salario-bruto]}] ...)
  ;; evitar
  (defn calcular-inss [dados] (:salario-bruto dados) ...)
  ```
- **Nomes em `kebab-case`**, em português, alinhados ao domínio (ex.: `salario-bruto`, `calcular-irrf`,
  `parcela-a-deduzir`) — o projeto já é em português, manter consistência (não misturar `grossSalary` com
  `salario-bruto`).
- Constantes de domínio (alíquotas, tetos, faixas) devem viver em `def`s nomeados no topo do namespace
  correspondente, nunca como números "mágicos" espalhados no corpo das funções — e devem referenciar
  `domain-rules.md` em comentário, ex.:
  ```clojure
  ;; Fonte: domain-rules.md §2 — Portaria Interministerial MPS/MF nº 13/2026
  (def teto-inss-2026 8475.55M)
  ```
- **Usar `BigDecimal`/literais `M`** para valores monetários, nunca `double`/`float` — evita erro de
  arredondamento em centavos. Se o projeto atual usa `double`, isso é uma dívida técnica a registrar como
  tarefa própria, não corrigir de passagem dentro de outra feature.
- Evitar `def` de funções fora dos namespaces de domínio; nada de "namespace utilitário genérico" tipo
  `utils.clj` fofo de tudo — se uma função é sobre INSS, mora no namespace do INSS.

---

## 4. Testes

- Framework: `clojure.test` (já usado no projeto).
- Todo `deftest` de cálculo deve:
  1. Usar os "Exemplos de referência" do `domain-rules.md` como caso feliz.
  2. Cobrir pelo menos um caso de borda (salário no limite exato de uma faixa, salário no teto, zero
     dependentes vs. N dependentes).
  3. Comparar com `==`/`=` usando `BigDecimal`, nunca comparação direta de `double` por igualdade.
- Nome de teste descreve o comportamento, não a implementação:
  ```clojure
  (deftest calcula-inss-terceira-faixa ...)   ; bom
  (deftest test-inss-2 ...)                    ; evitar
  ```
- A IA **não marca uma tarefa como concluída** sem `lein test` passando 100% — inclusive testes pré-existentes
  que não têm relação direta com a mudança (evita regressão silenciosa).

---

## 5. Documentação inline

- `docstring` em toda função pública de domínio, curta, explicando a regra de negócio (não o código):
  ```clojure
  (defn calcular-inss
    "Calcula o desconto de INSS pelo método de parcela a deduzir (ver domain-rules.md §2)."
    [salario-bruto]
    ...)
  ```
- Comentários de código só quando a *regra de negócio* não é óbvia pelo nome da função/variável — não
  comentar o que o Clojure já deixa claro (`;; soma os valores` acima de um `+` é ruído).

---

## 6. O que a IA NÃO deve fazer neste projeto

- Não introduzir uma nova lib de terceiros para resolver algo que `clojure.core` já resolve.
- Não reescrever um namespace inteiro para "melhorar estilo" numa tarefa que pediu uma correção pontual —
  refactors de estilo são tarefa própria, com spec própria em `specs/`.
- Não modificar valores de `domain-rules.md` a partir do código — é o inverso: o código se ajusta ao arquivo,
  nunca o contrário.
- Não usar `double` para dinheiro, nem "corrigir arredondamento" ad-hoc sem seguir a regra da seção 5 do
  `domain-rules.md`.

---

*Este arquivo deve ser revisado e ajustado após a primeira leitura real do `src/folhadepagamento` — os itens
marcados `<!-- VERIFICAR -->` são suposições razoáveis, não fatos confirmados sobre o código atual.*
