#lang scheme

; Ethan Petuchowski

; Structure and Interpretation of Computer Programs (SICP)

; March 9, 2014

; 1.11

; straight from the definition
(define (fR n)
  (if (< n 3) n
      (+ (* 3 (fR (- n 3)))
         (* 2 (fR (- n 2)))
         (* 1 (fR (- n 1))))))

(fR 3)
(fR 4)
(fR 5)
(fR 6)
(fR 7)

; iterative version
(define (fI n)

  ; goofy for-loop
  (define (iter-fI a b c n i)
    (if (> i n) c
        (iter-fI b
                 c
                 (+ (* 3 a) (* 2 b) (* 1 c))
                 n
                 (+ i 1))))

  (if (< n 3) n
      (iter-fI 1 2 4 n 4)))

(fI 3)
(fI 4)
(fI 5)
(fI 6)
(fI 7)


; 1.16

(define (expI b n)
  (cond ((= n 0) 1)
        (iter-exp 1 b n)))

(define (iter-exp a b n)
  (cond ((= n 1) (* a b))
        ((even? n) (iter-exp a (square b) (/ n 2)))
        (else (iter-exp (* a b) b (- n 1)))))

(define (even? n)
  (= (remainder n 2) 0))

(define (square x) (* x x))

(expI 1 0)
(expI 2 4)
(expI 2 10)
(expI 5 3)
