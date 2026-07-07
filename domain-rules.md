# domain-rules.md — Regras de Negócio da Folha de Pagamento

> **Propósito deste arquivo:** ground truth para qualquer IA (Claude Code, Kiro, etc.) que for gerar ou revisar
> código de cálculo neste projeto. Nenhuma fórmula de INSS/IRRF/FGTS deve ser calculada "de memória" pela IA —
> sempre consultar este arquivo. Se a competência (mês/ano) do cálculo não for a vigente abaixo, a IA deve
> **parar e perguntar** qual tabela usar, em vez de assumir.
>
> **Vigência dos valores abaixo: 01/01/2026 a 31/12/2026.**
> Ao virar o exercício, este arquivo precisa ser revisado e a data de vigência atualizada.

---

## 1. Parâmetros gerais 2026

| Parâmetro                    | Valor         |
| ----------------------------- | ------------- |
| Salário mínimo nacional       | R$ 1.621,00   |
| Teto do INSS                  | R$ 8.475,55   |
| Contribuição máxima do INSS   | R$ 988,09     |
| Desconto simplificado IRRF    | R$ 607,20/mês |
| Dedução por dependente (IRRF) | R$ 189,59/mês |
| Isenção total de IRRF até     | R$ 5.000,00   |

---

## 2. Tabela INSS 2026 (empregado, doméstico e avulso)

Contribuição **progressiva por faixas sucessivas** desde 03/2020 (EC 103/2019). Fórmula simplificada:

```
INSS = (salário_de_contribuição × alíquota_da_faixa) − parcela_a_deduzir
```

| Faixa | Salário de contribuição      | Alíquota | Parcela a deduzir |
| ----- | ----------------------------- | -------- | ------------------ |
| 1ª    | Até R$ 1.621,00               | 7,5%     | R$ 0,00            |
| 2ª    | De R$ 1.621,01 a R$ 2.902,84  | 9,0%     | R$ 24,32           |
| 3ª    | De R$ 2.902,85 a R$ 4.354,27  | 12,0%    | R$ 111,40          |
| 4ª    | De R$ 4.354,28 a R$ 8.475,55  | 14,0%    | R$ 198,49          |

- Salário acima do teto (R$ 8.475,55): desconto fixo em R$ 988,09 — **nunca ultrapassar esse valor**.
- **13º salário tem apuração de INSS separada** — não somar ao salário do mês para aplicar a tabela.
- Múltiplos vínculos: as remunerações se somam para fins de teto, mas cada empregador desconta apenas sua parcela.

**Exemplo de referência (para teste unitário):**
Salário R$ 3.000,00 → 3ª faixa → (3.000,00 × 12%) − 111,40 = **R$ 248,60**

**Fonte oficial:** Portaria Interministerial MPS/MF nº 13/2026 · https://www.gov.br/inss/pt-br/direitos-e-deveres/inscricao-e-contribuicao/tabela-de-contribuicao-mensal

---

## 3. Tabela IRRF 2026

### 3.1 Tabela progressiva mensal

```
IRRF_bruto = (base_de_cálculo × alíquota_da_faixa) − parcela_a_deduzir
```

| Faixa | Base de cálculo mensal        | Alíquota | Parcela a deduzir |
| ----- | ------------------------------ | -------- | ------------------ |
| 1ª    | Até R$ 2.428,80                | Isento   | R$ 0,00            |
| 2ª    | De R$ 2.428,81 a R$ 2.826,65   | 7,5%     | R$ 182,16          |
| 3ª    | De R$ 2.826,66 a R$ 3.751,05   | 15,0%    | R$ 394,16          |
| 4ª    | De R$ 3.751,06 a R$ 4.664,68   | 22,5%    | R$ 675,49          |
| 5ª    | Acima de R$ 4.664,68           | 27,5%    | R$ 908,73          |

### 3.2 Base de cálculo

```
base_de_cálculo = salário_bruto − INSS − (nº_dependentes × 189,59) − pensão_alimentícia − outras_deduções
```

Alternativa (usar a que resultar em menor imposto — a IA deve implementar **as duas** e escolher a mais vantajosa):

```
base_de_cálculo = salário_bruto − 607,20   (desconto simplificado mensal)
```

### 3.3 Redução mensal — Lei 15.270/2025 (NOVIDADE 2026, aplicar **depois** do cálculo acima)

Calculada sobre o **rendimento tributável bruto** (não sobre a base de cálculo):

| Rendimento tributável mensal | Redução                                          |
| ------------------------------ | ------------------------------------------------- |
| Até R$ 5.000,00                | Até R$ 312,89 (zera o IRRF)                       |
| De R$ 5.000,01 a R$ 7.350,00   | R$ 978,62 − (0,133145 × rendimento_tributável)    |
| A partir de R$ 7.350,01        | Sem redução                                       |

**Fórmula final:**

```
IRRF = MAX(0, IRRF_bruto − redução_mensal_aplicável)
```

- O IRRF **nunca é negativo**.
- 13º salário: tributado separadamente na 2ª parcela (dezembro), com base própria, sem somar ao salário do mês.
- Pensão alimentícia só é dedutível se houver decisão judicial, acordo homologado ou escritura pública.

**Fonte oficial:** Receita Federal do Brasil — Lei 15.191/2025 e Lei 15.270/2025 · https://www.gov.br/receitafederal/pt-br

---

## 4. FGTS

- Alíquota: **8%** sobre a remuneração bruta (empregado comum), depositado pelo empregador — **não é desconto do empregado**.
- Contrato de trabalho intermitente ou aprendiz pode ter regras específicas — validar caso a caso, não assumir 8% universal sem checar o tipo de contrato.

---

## 5. Regras de arredondamento

- Todos os valores monetários intermediários e finais: arredondar para **2 casas decimais**, padrão bancário (half-up), a cada etapa do cálculo (INSS, base do IRRF, IRRF).
- Nunca acumular arredondamento apenas no resultado final — isso pode gerar divergência de centavos em relação aos exemplos oficiais.

---

## 6. Como a IA deve usar este arquivo

1. Antes de implementar ou alterar qualquer função de cálculo (`inss`, `irrf`, `fgts`, `salario-liquido` etc.), ler este arquivo.
2. Toda função de cálculo deve ter pelo menos um teste `deftest` batendo com o "Exemplo de referência" desta tabela.
3. Se a tarefa envolver competência anterior a 2026, **parar e pedir a tabela daquele ano** — não extrapolar os valores atuais para trás.
4. Qualquer PR/commit que alterar uma fórmula de imposto deve citar a fonte oficial (Portaria/Lei) no corpo do commit.
5. Este arquivo não substitui consulta a fonte oficial em caso de dúvida — é um resumo operacional para grounding da IA, não parecer jurídico.

---

*Última atualização: 07/07/2026 · Fontes: Receita Federal, Portaria Interministerial MPS/MF nº 13/2026, Lei 15.191/2025, Lei 15.270/2025.*
