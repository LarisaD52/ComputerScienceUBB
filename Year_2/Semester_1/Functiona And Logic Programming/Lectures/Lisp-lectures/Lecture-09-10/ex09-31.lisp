(defun first1 (l)
  (cond
    ((null l) nil)
    ((null (car l)) (first1 (cdr l)))
    ((atom (car l)) (car l))
    (t (first1 (cdr l)))
  )
)

(defun first2 (l)
  (cond
    ((null l) nil)
    ((null (car l)) (first2 (cdr l)))
    ((atom (car l)) (car l))
    (t (first2 (car l)))
  )
)
