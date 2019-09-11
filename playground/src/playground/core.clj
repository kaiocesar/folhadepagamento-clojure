(ns playground.core)


(defn aplicar-desconto [valor_desconto_porc valor]
  "aplicar o desconto proporcinal"
  (* valor (/ valor_desconto_porc 100)))

(defn aplicar-irrf [salario_base valor_dependentes valor_inss porc_irrf valor_deducao]
  "aplicar o calculo de provendo - descontos - dedução do irrf"
  (if (> valor_dependentes (- salario_base valor_inss))
    0
    (let [valor_aplicado (- (* (- salario_base valor_inss valor_dependentes) (/ porc_irrf 100)) valor_deducao)]
      (if (< valor_aplicado 0)
        (* -1 valor_aplicado)
        valor_aplicado
      ))
  ))

(defn calcular-inss [salario_base]
   "calculo inss"
    (cond 
      (< salario_base 1751.81) (aplicar-desconto 8 salario_base)
      (and (>= salario_base 1751.82) (<= salario_base 2919.72)) (aplicar-desconto 9 salario_base)
      (and (>= salario_base 2919.73) (<= salario_base 5839.45)) (aplicar-desconto 11 salario_base)
      :else 642.34))

(defn calcular-fgts [salario_base]
  (* salario_base (/ 8 100)))

(defn calcular-dependentes [dependentes]
  (* dependentes 189.59))

(defn calcular-irrf [salario_base valor_dependentes valor_inss]
  (let [base_irrf (- salario_base valor_inss valor_dependentes)]
    (cond
      (< base_irrf 1903.98) 0
      (and (>= base_irrf 1903.99) (<= base_irrf 2826.65)) (aplicar-irrf salario_base valor_dependentes valor_inss 7.5 142.80)
      (and (>= base_irrf 2826.66) (<= base_irrf 3751.05)) (aplicar-irrf salario_base valor_dependentes valor_inss 15 354.80)
      (and (>= base_irrf 3751.06) (<= base_irrf 4664.68)) (aplicar-irrf salario_base valor_dependentes valor_inss 22.5 636.13)
      :else (aplicar-irrf salario_base valor_dependentes valor_inss 27.5 869.36))))

(defn -main
  "Estoque de produtos"
  ; calculo baseando em https://www.jornalcontabil.com.br/folha-de-pagamento-entenda-como-fazer-o-calculo/
  []
  (def funcionarios {:1 {
    :salario_base 3000
    :base_horas 220
    :jornada {:inicio 8 :refeicao 60 :termino 17 }
    :insalubridade "média"
    :periculosidade 0.30
    :dependentes 2
    }
    :2 {

    }})
    
    ; calcular inss
    ;(println (calcular-inss (-> funcionarios :1 :salario_base)))

    ; calcular o fgts
    ; (println (calcular-fgts (-> funcionarios :1 :salario_base)))

    ; calcular dependentes
    ; (println (calcular-dependentes (-> funcionarios :1 :dependentes)))

    ; calcular o IRPF
    (println (calcular-irrf 
      (-> funcionarios :1 :salario_base) 
        (calcular-dependentes (-> funcionarios :1 :dependentes)) 
          (calcular-inss (-> funcionarios :1 :salario_base))))

    ; calcular as horas extras
    ; calcular DSR (descanso semanal remunerado sobre horas extras)
    ; calcular vale transporte
    ; calcular o vale alimentação
    ; adicional noturno
    ; adicional de insalubridade

    ; calcular salario liquido
    
  
  (println "Folha de pagamento"))

