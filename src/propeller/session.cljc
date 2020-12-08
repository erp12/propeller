(ns propeller.session
  (:require [propeller.genome :as genome]
            [propeller.gp :as gp]
            [propeller.selection :as selection]
            [propeller.variation :as variation]
            [propeller.problems.simple-regression :as regression]
            [propeller.problems.string-classification :as string-classif]
            propeller.problems.valiant
            [propeller.push.core :as push]
            [propeller.push.interpreter :as interpreter]
            [propeller.push.state :as state]
            [propeller.push.utils.helpers :refer [get-stack-instructions]]))

;#_(interpreter/interpret-program
;    '(1 2 :integer_add) state/empty-state 1000)
;
;#_(interpreter/interpret-program
;    '(3 3 :integer_eq :exec_if (1 "yes") (2 "no"))
;    state/empty-state
;    1000)
;
;#_(interpreter/interpret-program
;    '(:in1 :string_reverse 1 :string_take "?" :string_eq :exec_if
;       (:in1 " I am asking." :string_concat)
;       (:in1 " I am saying." :string_concat))
;    (assoc state/empty-state :input {:in1 "Can you hear me?"})
;    1000)
;
;#_(interpreter/interpret-program
;    '(:in1 :string_reverse 1 :string_take "?" :string_eq :exec_if
;       (:in1 " I am asking." :string_concat)
;       (:in1 " I am saying." :string_concat))
;    (assoc state/empty-state :input {:in1 "I can hear you."})
;    1000)
;
;#_(genome/plushy->push
;    (genome/make-random-plushy (get-stack-instructions #{:float :integer :exec :boolean}) 20))
;
;#_(gp/gp {:instructions            propeller.problems.software.number-io/instructions
;          :error-function          propeller.problems.software.number-io/error-function
;          :max-generations         500
;          :population-size         500
;          :max-initial-plushy-size 100
;          :step-limit              200
;          :parent-selection        :lexicase
;          :tournament-size         5
;          :umad-rate               0.1
;          :variation               {:umad 0.5 :crossover 0.5}
;          :elitism                 false})
;
;#_(gp/gp {:instructions            propeller.problems.simple-regression/instructions
;          :error-function          propeller.problems.simple-regression/error-function
;          :max-generations         500
;          :population-size         500
;          :max-initial-plushy-size 100
;          :step-limit              200
;          :parent-selection        :tournament
;          :tournament-size         5
;          :umad-rate               0.01
;          :variation               {:umad      1.0
;                                    :crossover 0.0}
;          :elitism                 false})
;
;#_(gp/gp {:instructions            propeller.problems.simple-regression/instructions
;          :error-function          propeller.problems.simple-regression/error-function
;          :max-generations         500
;          :population-size         500
;          :max-initial-plushy-size 100
;          :step-limit              200
;          :parent-selection        :tournament
;          :tournament-size         5
;          :umad-rate               0.1
;          :variation               {:umad      1.0
;                                    :crossover 0.0}
;          :elitism                 false})
;
;
;#_(gp/gp {:instructions            propeller.problems.simple-regression/instructions
;          :error-function          propeller.problems.simple-regression/error-function
;          :max-generations         500
;          :population-size         500
;          :max-initial-plushy-size 100
;          :step-limit              200
;          :parent-selection        :lexicase
;          :tournament-size         5
;          :umad-rate               0.1
;          :variation               {:umad      1.0
;                                    :crossover 0.0}
;          :elitism                 false})
;
;#_(gp/gp {:instructions            propeller.problems.simple-regression/instructions
;          :error-function          propeller.problems.simple-regression/error-function
;          :max-generations         500
;          :population-size         500
;          :max-initial-plushy-size 100
;          :step-limit              200
;          :parent-selection        :lexicase
;          :tournament-size         5
;          :umad-rate               0.1
;          :diploid-flip-rate       0.1
;          :variation               {:umad         0.8
;                                    :diploid-flip 0.2}
;          :elitism                 false
;          :diploid                 true})
;
;
;#_(gp/gp {:instructions            propeller.problems.software.smallest/instructions
;          :error-function          propeller.problems.software.smallest/error-function
;          :max-generations         500
;          :population-size         500
;          :max-initial-plushy-size 100
;          :step-limit              200
;          :parent-selection        :lexicase
;          :tournament-size         5
;          :umad-rate               0.1
;          :diploid-flip-rate       0.1
;          :variation               {;:umad 0.8
;                                    ;:diploid-flip  0.2
;                                    :umad 1
;                                    }
;          :elitism                 false
;          :diploid                 false})
;
;#_(gp/gp {:instructions            propeller.problems.software.smallest/instructions
;          :error-function          propeller.problems.software.smallest/error-function
;          :max-generations         500
;          :population-size         500
;          :max-initial-plushy-size 200                      ;100
;          :step-limit              200
;          :parent-selection        :lexicase
;          :tournament-size         5
;          :umad-rate               0.1
;          :diploid-flip-rate       0.1
;          :variation               {:umad         0.8
;                                    :diploid-flip 0.2
;                                    ;:umad 1
;                                    }
;          :elitism                 false
;          :diploid                 true})
;
;
;(gp/gp {:instructions            propeller.problems.string-classification/instructions
;          :error-function          propeller.problems.string-classification/error-function
;          :max-generations         500
;          :population-size         500
;          :max-initial-plushy-size 100
;          :step-limit              200
;          :parent-selection        :lexicase
;          :tournament-size         5
;          :umad-rate               0.1
;          :diploid-flip-rate       0.1
;          :variation               {:umad         0.8
;                                    :diploid-flip 0.2
;                                    }
;          :elitism                 false
;          :diploid                 true})


(gp/gp {:instructions            propeller.problems.valiant/instructions
        :error-function          propeller.problems.valiant/error-function
        :max-generations         500
        :population-size         500
        :max-initial-plushy-size 1000
        :step-limit              2000
        :parent-selection        :lexicase
        :tournament-size         5
        :umad-rate               0.001
        :diploid-flip-rate       0.001
        :variation               {:umad         0.5
                                  :diploid-flip 0.5
                                  }
        :elitism                 false
        :diploid                 true})

