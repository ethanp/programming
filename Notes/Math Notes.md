latex input:		mmd-article-header
Title:		Math Notes
Author:		Ethan C. Petuchowski
Base Header Level:		1
latex mode:		memoir
Keywords:		Math, DSP, Digital Signal Processing, Fourier Transform
CSS:		http://fletcherpenney.net/css/document.css
xhtml header:		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
</script>
copyright:			2014 Ethan C. Petuchowski
latex input:		mmd-natbib-plain
latex input:		mmd-article-begin-doc
latex footer:		mmd-memoir-footer

This document requires MathJax (and possibly MultiMarkdown) to be viewed properly.

# Digital Signal Processing

## Fourier Transform ##

Notes from Brian Douglas's Khan Academy-style  YouTube Videos:

* [Part 1](https://www.youtube.com/watch?v=1JnayXHhjlg)
* [Part 2](https://www.youtube.com/watch?v=kKu6JDqNma8)

#### Turns a function of *time* into a function of *frequency.* ####

Any function in the time-domain, can be represented as a sum of sinusoids, 
where each has a different amplitude, frequency, and phase. This is that thing from Diff. EQ class. 
So that's all we're doing.

 \\[\mathrm{Frequency}\;\; \nu_{Hz} := \frac{\omega}{2\pi}[Hz]  \\]

So a Fourier Transform maps you from the Time domain \\(T\\), to the Frequency domain \\(N\\):

\\[FT:\; f(t) \rightarrow f(\nu) \\]

Here it is:

\\[FT\{f(x)\}:=\frac{1}{\sqrt{2\pi}}\int_{-\infty}^{\infty}\!f(x)e^{-iwx}dx\\]

And the inverse:

\\[FT^{-1}\{FT\{f(x)\}\} := \frac{1}{\sqrt{2\pi}}\int_{-\infty}^{\infty}\!FT\{f(x)\}e^{iwx}dx\\]


![](Fourier%20Transform%20Summary.png)

### Useful Tidbits

#### A bit of Nomenclature

* A **signal** and a **function** are the *same thing*
* **Analysis** -- break a signal into simpler component parts
* **Synthesis** -- reassemble a signal from its constituent parts
* **Complex Sinusoids** -- Phase and amplitude can be described by a single complex number. 
Plotting that point on a real-imaginary plane, the amplitude is the distance of the point from the origin. 
The phase is the angle of that line off the positive real line, so a frequency with no phase shift is
on the real line, where the value *is* the amplitude. Otherwise we have to do some trigonometry to
go convert between the number and the phase & amplitude.
* **Frequency spectrum** -- representation of a signal in the *frequency domain*

#### Euler's Formula(s) ####


\\[e^{ix} = \cos x + i \sin x\\]
\\[e^{-ix} = \cos x - i \sin x\\]


### [Discrete Fourier Transform](http://en.wikipedia.org/wiki/Discrete_Fourier_transform) ##

The following summary is brilliant:

> Converts a finite list of equally spaced samples of a function into the list of coefficients of a finite combination of *complex sinusoids* [see definition above], ordered by their frequencies, that has those same sample values. It can be said to convert the sampled function from its original domain (often time or position along a line) to the frequency domain.

### [Fast Fourier Transform](Http://en.wikipedia.org/Wiki/Fast_Fourier_Transform) ###

Using the definition of a DFT, the computation takes \\(O(n^2)\\) operations. An FFT can compute the same DFT in only \\(O(n\log n)\\) operations. It is an approximation.