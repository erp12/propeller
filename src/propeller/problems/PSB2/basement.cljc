(ns propeller.problems.PSB2.basement
  (:require [psb2.core :as psb2]
            [propeller.genome :as genome]
            [propeller.push.interpreter :as interpreter]
            [propeller.utils :as utils]
            [propeller.push.utils.helpers :refer [get-stack-instructions]]
            [propeller.push.state :as state]
            [clojure.pprint :as pprint]
            [propeller.tools.math :as math]
            [propeller.gp :as gp]))

; ===========  PROBLEM DESCRIPTION  ============================
; BASEMENT from PSB2
; Given a vector of integers, return the first
; index such that the sum of all integers from the start of the
; vector to that index (inclusive) is negative.
;
; Source: https://arxiv.org/pdf/2106.06086.pdf
; ===============================================================

(def train-and-test-data (psb2/fetch-examples "data" "basement" 200 2000))

; Random integer between -100 and 100 (from smallest)
(defn random-int [] (- (rand-int 201) 100))

(def instructions
  (utils/not-lazy
    (concat
      ;;; stack-specific instructions
      (get-stack-instructions #{:exec :integer :boolean :vector_integer :print})
      ;;; input instructions
      (list :in1)
      ;;; close
      (list 'close)
      ;;; ERCs (constants)
      (list random-int -1 0 1 []))))

(defn error-function
  [argmap data individual]
  (let [program (genome/plushy->push (:plushy individual) argmap)
        inputs (map (fn [i] (get i :input1)) data)
        correct-outputs (map (fn [i] (get i :output1)) data)
        outputs (map (fn [input]
                       (state/peek-stack
                         (interpreter/interpret-program
                           program
                           (assoc state/empty-state :input {:in1 input})
                           (:step-limit argmap))
                         :integer))
                     inputs)
        errors (map (fn [correct-output output]
                      (if (= output :no-stack-item)
                        1000000
                        (min 1000.0 (math/abs (- correct-output output)))))
                    correct-outputs
                    outputs)]
    (assoc individual
      :behaviors outputs
      :errors errors
      :total-error #?(:clj  (apply +' errors)
                      :cljs (apply + errors)))))

(defn -main
  "Runs propel-gp, giving it a map of arguments."
  [& args]
  (gp/gp
    (merge
      {:instructions            instructions
       :error-function          error-function
       :training-data           (:train train-and-test-data)
       :testing-data            (:test train-and-test-data)
       :max-generations         500
       :population-size         500
       :max-initial-plushy-size 100
       :step-limit              200
       :parent-selection        :lexicase
       :tournament-size         5
       :umad-rate               0.1
       :variation               {:umad 0.5 :crossover 0.5}
       :elitism                 false}
      (apply hash-map (map #(if (string? %) (read-string %) %) args)))))

