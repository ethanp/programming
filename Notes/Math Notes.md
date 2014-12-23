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

This document requires MathJax (and possibly [`MultiMarkdown`](http://fletcherpenney.net)) to be viewed properly.

# Probability & Statistics #

## Probability ##

### Combinations (*n* choose *k*) ##

\\[{n\choose k}=\frac{n!}{k!(n-k)!}\\]

####Explanation ###

1. We know that \\(n!\\) is the number of *permutations* of *n* items
	1. I.e. the total number of unique *orderings*
	2. Because there are \\(n\\) options for the first slot, \\(n-1\\) for the second, and so on.
2. But if we are only "choosing" \\(k\\) items, so we can stop the factorial after
   computing the permutations of the first \\(k\\) items. This leaves us with
   \\(n\cdot(n-1)\cdot(n-2)\cdots (n-k+1)=\frac{n!}{(n-k)!}\\).
3. Now among these \\(k\\) items, we don't actually care about all the
   different permutations, so we can cancel them out by dividing by
   \\(k!\\), leaving us with the final formula.

## Statistics #

### Bayesian Inference ###

Use **Bayes' Rule** to update the probability for a hypothesis as evidence is acquired.

\\[\mathbb{P}[H|E]=\frac{\mathbb{P}[E|H]\cdot\mathbb{P}[H]}{\mathbb{P}[E]}\\]

In my own words:
> The *posterior probability* that the *hypothesis* \\(H\\) is *true* *given* the *evidence* \\(E\\), is *equal* to the *probability* of the *evidence* *given* the *hypothesis* is *true* *w.r.t.* seeing that *evidence* under *all* circumstances, multiplied by the *prior* ("overall") probability of the *hypothesis* being *true* in general.

Particularly important in the *dynamic analysis* of a *sequence* of data.

### Markov Chain Monte Carlo ###

A *class* of *algorithms* for *sampling* from a *probability distribution* based on constructing a *Markov chain* that has the desired distribution as its *equilibrium distribution*. We can then use the *state* of the chain after a number of *steps* as a *sample* from the desired distribution. The point is generally to calculate a *numerical approximation* of a *multi-dimensional integral*.

Examples include *Gibbs sampling*, which requires all the *conditional distributions* of the target distribution to be sampled exactly.

### Covariance Matrix ##
#### How to Compute It ###

\\[X^TX\\]

#### Why It Matters ###

Used in **Principal Component Analysis** (PCA), a technique described in Andrew Ng's *Machine Learning* course on Coursera, for reducing the *dimensionality* of a *dataset*. One might want to do this for 2 reasons:

1. As a form of *lossy compression*
2. To produce *visualizations* of the data in 1, 2, or 3D
3. To *reduce computation time*

#### Covariance ###

"A measure of how much two random variables change together."
[[Wikipedia][WCov]]

\\[\sigma_{XY}=cov(X,Y)=E[(X-\mu_X)(Y-\mu_Y)]\\]
\\[=E[XY]-\mu_X\mu_Y\\]
\\[=E[XY]-E[X]E[Y]\\]
\\[=\sum_{i=1}^{N}{\frac{(x_i-\bar{x})(y_i-\bar{y})}{N}}\\]

Note
\\[cov(X,X)=E[X^2]-E[X]^2=\sigma_x^2\\]
At first glance, it behaves like the slope line of a linear regression: two positively correlated variables have a positive covariance, and same for negative. However, the *magnitude* of the covariance is different, "Notably, correlation is dimensionless while covariance is in units obtained by multiplying the units of the two variables" (Wiki).

**TODO...heh**

[WCov]: http://en.wikipedia.org/wiki/Covariance

### Ordinary Least Squares ##

#### Finding the Intercept and the Slope ###

[On Wikipedia](http://en.wikipedia.org/wiki/Simple_linear_regression)

We're trying to find the equation of the straight line

\\[y = \alpha + \beta x \\]

which would provide a line that minimizes the sum of squared residuals of the linear regression model.

We can use

\\[\hat{\beta} = \frac{\mathrm{Cov}[x, y]}{\mathrm{Var}[x]} = r_{xy} \frac{s_y}{s_x}\\]

\\[\hat{\alpha} = \bar{y} - \hat{\beta} \bar{x}\\]

* The line always passes through \\((\bar{x},\bar{y})\\)
* If you normalize the data, the slope is \\(Cor(Y,X)\\)

##### Without the Intercept Term ####

\\[y = \beta x \\]
\\[\hat{\beta} = \frac{\bar{xy}}{x^2}\\]

### Similarity Measures ##
Sources:

* [Random blog on Collaborative Filtering with Mahout](http://blog.comsysto.com/2013/04/03/background-of-collaborative-filtering-with-mahout/)

#### Real Valued Attributes

##### Pearson Similarity ####

* [Wikipedia](http://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient)

\\[\rho_{X,Y}=\frac{\mathrm{cov}(X,Y)}{\sigma_X \sigma_Y}=\frac{E[(X-\mu_X)(Y-\mu_Y)]}{\sigma_X \sigma_Y}\\]

##### Euclidean Distance ####

* [Wikipedia: Norm](http://en.wikipedia.org/wiki/Norm_(mathematics)#Euclidean_norm)

\\[ ||\textbf{x}|| := \sqrt{x^2_1 + \cdots + x^2_n} \\]

Or more generally, the *p*-norm is

\\[ ||\textbf{x}|| := (\sum_{i=1}^{n}|x_i|^p)^{1/p} \\]

Which means the Euclidean norm \\(\equiv\\) the \\(l_2\\) norm.

#### Binary Attributes ###

* [Wikipedia](http://en.wikipedia.org/wiki/Tanimoto_coefficient#Tanimoto_coefficient_.28extended_Jaccard_coefficient.29)

##### Jaccard index/similarity coefficient ####

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
