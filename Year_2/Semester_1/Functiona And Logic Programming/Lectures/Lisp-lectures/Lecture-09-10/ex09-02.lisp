(defun p ()
  a
)
(defun q ()
  (setq a 7)
  (p)
)

(setq a 5)
(setq y (list (p) (q) (p)))

(print y)

; the result is   (5 7 7)
; the first (p)   produces 5
; (q)             produces 7
; the second (p)  produces 7