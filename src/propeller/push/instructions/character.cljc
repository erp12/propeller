(ns propeller.push.instructions.character
  (:require [propeller.push.state :as state]
            [propeller.push.utils.helpers :refer [make-instruction]]
            [propeller.push.utils.macros :refer [def-instruction
                                                 generate-instructions]]
            [propeller.tools.character :as char]))

;; =============================================================================
;; CHAR Instructions
;; =============================================================================

;; Pushes TRUE onto the BOOLEAN stack if the popped character is a letter
(def-instruction
  :char_isletter
  ^{:stacks #{:boolean :char}}
  (fn [state]
    (make-instruction state char/is-letter [:char] :boolean)))

;; Pushes TRUE onto the BOOLEAN stack if the popped character is a digit
(def-instruction
  :char_isdigit
  ^{:stacks #{:boolean :char}}
  (fn [state]
    (make-instruction state char/is-digit [:char] :boolean)))

;; Pushes TRUE onto the BOOLEAN stack if the popped character is whitespace
;; (newline, space, or tab)
(def-instruction
  :char_iswhitespace
  ^{:stacks #{:boolean :char}}
  (fn [state]
    (make-instruction state char/is-whitespace [:char] :boolean)))

;; Pops the FLOAT stack, converts the top item to a whole number, and pushes
;; its corresponding ASCII value onto the CHAR stack. Whole numbers larger than
;; 128 will be reduced modulo 128. For instance, 248.45 will result in x being
;; pushed.
(def-instruction
  :char_fromfloat
  ^{:stacks #{:char :float}}
  (fn [state]
    (make-instruction state #(char (mod (long %) 128)) [:float] :char)))

;; Pops the INTEGER stack and pushes the top element's corresponding ASCII
;; value onto the CHAR stack. Integers larger than 128 will be reduced modulo
;; 128. For instance, 248 will result in x being pushed
(def-instruction
  :char_frominteger
  ^{:stacks #{:char :integer}}
  (fn [state]
    (make-instruction state #(char (mod % 128)) [:integer] :char)))

;; Pops the STRING stack and pushes the top element's constituent characters
;; onto the CHAR stack, in order. For instance, "hello" will result in the
;; top of the CHAR stack being \h \e \l \l \o
(def-instruction
  :char_allfromstring
  ^{:stacks #{:char :string}}
  (fn [state]
    (if (state/empty-stack? state :string)
      state
      (let [top-string (state/peek-stack state :string)
            popped-state (state/pop-stack state :string)]
        (state/push-to-stack-many popped-state :char (map char top-string))))))