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

# Statistics #

## Similarity Measures ##

#### 5/8/14

Sources:

* [Random blog on Collaborative Filtering with Mahout](http://blog.comsysto.com/2013/04/03/background-of-collaborative-filtering-with-mahout/)

### Real Valued Attributes

#### Pearson Similarity ####

* [Wikipedia](http://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient)

\\[\rho_{X,Y}=\frac{\mathrm{cov}(X,Y)}{\sigma_X \sigma_Y}=\frac{E[(X-\mu_X)(Y-\mu_Y)]}{\sigma_X \sigma_Y}\\]

#### Euclidean Distance ####

* [Wikipedia: Norm](http://en.wikipedia.org/wiki/Norm_(mathematics)#Euclidean_norm)

\\[ ||\textbf{x}|| := \sqrt{x^2_1 + \cdots + x^2_n} \\]

Or more generally, the *p*-norm is

\\[ ||\textbf{x}|| := (\sum_{i=1}^{n}|x_i|^p)^{1/p} \\]

Which means the Euclidean norm \\(\equiv\\) the \\(l_2\\) norm.

### Binary Attributes ###

* [Wikipedia](http://en.wikipedia.org/wiki/Tanimoto_coefficient#Tanimoto_coefficient_.28extended_Jaccard_coefficient.29)

#### Jaccard index/similarity coefficient ####

\\[J(A,B)=\frac{|A\cap{B}|}{|A\cup{B}|}\\]


Now, letting \\(M_{AB}\\) be the number of data points where binary attribute a=A and b=B, we
compute the **Jaccard similarity coefficient** as

\\[J=\frac{M_{11}}{M_{01}+M_{10}+M_{11}}\\]

And the **Jaccard distance** as

\\[d_J=\frac{M_{01}+M_{10}}{M_{01}+M_{10}+M_{11}}\\]


# Digital Signal Processing

## Total Harmonic Distortion ##

#### 4/17/14

[Wikipedia](http://en.wikipedia.org/wiki/Total_harmonic_distortion)

The total harmonic distortion, or THD, of a signal is a measurement of the
harmonic distortion present and is defined as **the ratio of the sum of the
powers of all harmonic components to the power of the fundamental frequency**.
In audio systems, lower THD means the components in a loudspeaker, amplifier or
microphone or other equipment produce a more accurate reproduction by reducing
harmonics added by electronics and audio media.

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
