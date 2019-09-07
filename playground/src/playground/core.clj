(ns playground.core)

(defn valor-descontado
  "Valor com 10% de desconto"
  [valor-bruto]
  (if (> valor-bruto 5000)
    (let [taxa-de-desconto (/ 10 100)
          desconto         (* valor-bruto taxa-de-desconto)]
      (desconto))
    (- valor-bruto desconto)))

(defn -main
  "Estoque de produtos"
  []
  (def estoque ["Notebook", "Teclado", "Mouse", "Monitor", "Impressora"])
  (def valor-bruto-estoque 5300)
  (def estoque (conj estoque "Mousepad"))
  (println "Total de produtos:" (count estoque))
  (println "Estoque:" estoque)
  (println "Valor final:" (valor-descontado valor-bruto-estoque)))

