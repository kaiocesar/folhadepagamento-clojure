(ns playground.core)


(defn -main
  "Estoque de produtos"
  []
  (defn valor-descontado
    "Valor com 10% de desconto"
    [valor-bruto]    
      (if (< valor-bruto 500)
        (let [taxa-de-desconto (/ 10 100)]
          (- valor-bruto (* valor-bruto taxa-de-desconto)))
        valor-bruto)
  )

  (def estoque ["Notebook", "Teclado", "Mouse", "Monitor", "Impressora"])
  (def valor-bruto-estoque 53)
  (def estoque (conj estoque "Mousepad"))
  (println "Total de produtos:" (count estoque))
  (println "Estoque:" estoque)
  (println "Valor final:" (valor-descontado valor-bruto-estoque))  
  )

