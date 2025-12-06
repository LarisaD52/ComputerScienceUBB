; Functions with function parameters

(defun a () 10)
(defun b () 20)
(defun c () 30)

(defun f1 (a) (a))
(defun f2 (a) (funcall #'a))
(defun f3 (a) (funcall a))

; try the following

; (a)
; (b)
; (c)

; (f1 #'b)
; (f1 #'c)

; (f2 #'b)
; (f2 #'c)

; (f3 #'b)
; (f3 #'c)
