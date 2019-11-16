(ns folhadepagamento.core)


(def porcentagem-vale-refeicao 0.2)

(def salario-minimo-nacional 998)

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

(defn calcular-salario-hora [salario_base base_horas]
  (/ salario_base base_horas))

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

(defn calcular-horas-extras [salario_hora dias_uteis domingos_feriados]
  (println domingos_feriados dias_uteis)
  )

(defn calcular-vale-transporte [salario_base vale_transporte_necessario]
  (let [vale_transporte_base (* salario_base (/ 6 100))]
    (cond
      (> vale_transporte_base vale_transporte_necessario) (- vale_transporte_base vale_transporte_necessario)
      (<= vale_transporte_base vale_transporte_necessario) vale_transporte_base ; a diferença será adicionada para a empresa
    )))


  (defn calcular-vale-alimentacao [salario_base]
    (* salario_base porcentagem-vale-refeicao))

(defn calcular-adicional-noturno [salario_base jornada valor_hora]
  (cond
    (and (> (:inicio jornada) 21) (< (:termino jornada) 6)) (* valor_hora 1.20)
  ))

(defn calcular-insalubridade [insalubridade]
   (cond
     (== insalubridade "mínima") (* salario-minimo-nacional 0.10)
     (== insalubridade "média") (* salario-minimo-nacional 0.20)
     (== insalubridade "máxima") (* salario-minimo-nacional 0.40)
))
     

(defn -main
  "Estoque de produtos"
  ;; calculo baseando em https://www.jornalcontabil.com.br/folha-de-pagamento-entenda-como-fazer-o-calculo/
  []
  (def funcionarios  [
    {
      :name "Ana Julia"
      :salario_base 3000.00
      :base_horas 220
      :jornada {:inicio 22 :refeicao 60 :termino 5 }
      :insalubridade "média"
      :periculosidade 0.30
      :dependentes 2
      :horas_extras {
        :domingos_feriados 1
        :dias_uteis 2
      }
      :outras_dependencias {
        :vale_transporte_necessario 228.80 ;; 44 passagens de R$5,20
      }
    }
    {
      :name "Manuella"
      :salario_base 1500.00
      :base_horas 220
      :jornada {:inicio 8 :refeicao 60 :termino 17 }
      :insalubridade "baixa"
      :periculosidade 0.10
      :dependentes 1
      :horas_extras {
        :domingos_feriados 0
        :dias_uteis 25
      }
    :outras_dependencias {
        :vale_transporte_necessario 196.68 ;; 44 passagens de R$4,75
      }
    }])
    
    ; calcular inss
    ;(println (calcular-inss (-> funcionarios :1 :salario_base)))

    ; calcular o fgts
    ; (println (calcular-fgts (-> funcionarios :1 :salario_base)))

    ; calcular dependentes
    ; (println (calcular-dependentes (-> funcionarios :1 :dependentes)))

    ; calcular o IRPF
    ; (println (calcular-irrf 
    ;   (-> funcionarios :1 :salario_base) 
    ;     (calcular-dependentes (-> funcionarios :1 :dependentes)) 
    ;       (calcular-inss (-> funcionarios :1 :salario_base))))

    ; calcular as horas extras
    ; (calcular-horas-extras  (/ (-> funcionarios :1 :salario_base) (-> funcionarios :1 :base_horas))
    ;                         (-> funcionarios :1 :horas_extras :dias_uteis) (-> funcionarios :1 :horas_extras :domingos_feriados))

    ; calcular DSR (descanso semanal remunerado sobre horas extras)
    ; calcular vale transporte
    ; (-> (calcular-vale-transporte 
    ;   (-> funcionarios :1 :salario_base) 
    ;     (-> funcionarios :1 :outras_dependencias :vale_transporte_necessario)) println)

    ; calcular o vale alimentação
    ; (-> (calcular-vale-alimentacao (-> funcionarios :1 :salario_base)) println)

    ; adicional noturno
    ; (let [salario_base (-> funcionarios :1 :salario_base)]
    ;   (-> (calcular-adicional-noturno 
    ;         (-> funcionarios :1 :salario_base) 
    ;         (-> funcionarios :1 :jornada) 
    ;         (calcular-salario-hora salario_base (-> funcionario :1 :base_horas)))))
    

    ;; Exibir funcionários
    ; (-> (map #(str "\n Funcionário " (:name %) " com salário R$ " (:salario_base %)) funcionarios) println )


    ; adicional de insalubridade
    ; (-> calcular-insalubridade (-> funcionarios :1 :insalubridade) println)

    (println  (-> funcionarios))
    ; calcular salario liquido
    
  
  (println "Folha de pagamento"))

