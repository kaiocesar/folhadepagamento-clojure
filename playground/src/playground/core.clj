(ns playground.core)


(defn aplicar-desconto [valor-desconto-porc valor]
  "aplicar o desconto proporcinal"
  (* valor (/ valor-desconto-porc 100)))

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
    :dependentes 10
    }
    :2 {

    }})
    
    ; calcular inss
    ;(println (calcular-inss (-> funcionarios :1 :salario_base)))

    ; calcular o fgts
    ; (println (calcular-fgts (-> funcionarios :1 :salario_base)))

    ; calcular dependentes
    (println (calcular-dependentes (-> funcionarios :1 :dependentes)))
    ; calcular o IRPF

    ; calcular as horas extras
    ; calcular DSR (descanso semanal remunerado sobre horas extras)
    ; calcular vale transporte
    ; calcular o vale alimentação
    ; adicional noturno
    ; adicional de insalubridade

    ; calcular salario liquido
    
  
  (println "Folha de pagamento"))

