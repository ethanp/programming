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
