(ns folhadepagamento.base)

(defn aplicar-desconto [valor_desconto_porc valor]
    "aplicar o desconto proporcinal"
    (* valor (/ valor_desconto_porc 100)))

(defn calcular-inss [salario_base]
    {:pre [(number? salario_base)]}
    (cond 
        (< salario_base 1751.82) (aplicar-desconto 8 salario_base)
        (and (>= salario_base 1751.82) (<= salario_base 2919.72)) (aplicar-desconto 9 salario_base)
        (and (>= salario_base 2919.73) (<= salario_base 5839.45)) (aplicar-desconto 11 salario_base)
        :else 642.34))