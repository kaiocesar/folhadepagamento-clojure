(ns playground.core)

(defn -main
  "Estoque de produtos"
  ; calculo baseando em https://www.jornalcontabil.com.br/folha-de-pagamento-entenda-como-fazer-o-calculo/
  []
  (def funcionarios {:1 {
    :salario_bruto 3000 
    :base_horas 220
    :jornada {:inicio 8 :refeicao 60 :termino 17 }
    :insalubridade "média"
    :periculosidade 0.30
    }
    :2 {

    }})

    ; calcular salario liquido
    ; calcular inss
    ; calcular o fgts
    ; calcular o IRPF
    ; calcular as horas extras
    ; calcular DSR (descanso semanal remunerado sobre horas extras)
    ; calcular vale transporte
    ; calcular o vale alimentação
    ; adicional noturno
    ; adicional de insalubridade
  (-> funcionarios :1 :salario_bruto println)
  (println "Folha de pagamento"))

