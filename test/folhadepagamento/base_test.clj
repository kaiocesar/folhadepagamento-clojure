(ns folhadepagamento.base-test
    (:require [clojure.test :refer :all]
              [folhadepagamento.base :refer :all]))

(deftest test-calcular-inss-3k
    (is (= (calcular-inss 3000) 330)))

(deftest test-calcular-inss-175181
    (is (= (calcular-inss 1751.81) (* 1751.81 0.08))))
    
(deftest test-calcular-inss-zero
    (is (= (calcular-inss 0) 0)))

; (deftest test-calcular-inss-nil
;     (is (thrown? Exception (calcular-inss nil))))
