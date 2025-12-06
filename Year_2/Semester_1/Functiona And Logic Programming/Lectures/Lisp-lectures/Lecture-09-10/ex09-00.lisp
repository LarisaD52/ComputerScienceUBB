; Try all following functions with numerical and list arguments

; x is number or string
; = its value is copied
; = change of contents is not exported
; = change of object is not exported

; x is list etc
; = its reference is copied
; = change of contents is exported
; = change of object is not exported

; x is number
; the inner x is changed
; the outer x is not changed
(defun f1 (x) 
  (setq x (+ 1 x))
)

; x is number
; the inner x is changed
; the outer x is not changed
(defun f2 (x) 
  (setf x (+ 1 x))
)

; x is list
; the inner x is changed
; the outer x is not changed
(defun f3 (x) 
  (setq x '(100 0 0))
)

; x is list
; the inner x is changed
; the outer x is not changed
(defun f4 (x) 
  (setf x '(100 0 0))
)

; x is list
; the inner x is changed
; the outer x is changed
(defun f5 (x) 
  (setf (car x) 100)
)

; x is list
; the inner x is changed
; the outer x is changed
(defun f6 (x)
  (rplaca x 100)
)

; x is list
; the inner x is changed
; the outer x is changed
(defun f7 (x)
  ((lambda (y) (rplaca y 100)) x)
)
