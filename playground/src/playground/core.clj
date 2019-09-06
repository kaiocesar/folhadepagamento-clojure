(ns playground.core)

(defn valor-descontado [valor-bruto]
  "Valor com 10% de desconto"
  (let [desconto (/ 10 100)]
    (* valor-bruto (- 1 0.10))))


(defn -main
  "Estoque de produtos"
  []
  (def estoque ["Notebook", "Teclado", "Mouse", "Monitor"])
  (def valor-bruto-estoque 5300)
  (def estoque (conj estoque "Mousepad"))
  (println "Total de produtos:" (count estoque))
  (println "Estoque:" estoque)
  (println "Valor descontado" (valor-descontado valor-bruto-estoque)))
