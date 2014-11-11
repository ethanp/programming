latex input:    mmd-article-header
Title:          Computer Vision Notes
Author:         Ethan C. Petuchowski
Base Header Level:  1
latex mode:     memoir
Keywords:       Computer Vision, Algorithms
CSS:            http://fletcherpenney.net/css/document.css
xhtml header:   <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
</script>
copyright:      2014 Ethan C. Petuchowski
latex input:    mmd-natbib-plain
latex input:    mmd-article-begin-doc
latex footer:   mmd-memoir-footer

# Computer Vision

## Other

### Find occurrences of a template in an image

[Mathematica docs](https://reference.wolfram.com/mathematica/ref/ImageCorrelate.html) -
 Use the **cosine distance**

	ImageCorrelate[ImgToSearchIn, ImgToSearchFor, CosineDistance]

### Correlation vs Convolution

**Convolution is just like correlation, except that we flip over
the filter before correlating.**

##### 4/28/14

[Nice paper on it](http://www.cs.umd.edu/~djacobs/CMSC426/Convolution.pdf)

> For example, convolution of a 1D image with the filter (3,7,5)
> is exactly the same as correlation with the filter (5,7,3).

## Wikipedia

### Cascading classifier

##### 4/28/14

[Wikipedia](http://en.wikipedia.org/wiki/Cascading_classifiers)

* An **ensemble** learning method
* First use as a face detector, in 2001
* Fast on low CPU systems, e.g. cameras and phones
* Trained with ADAboost
* Available in OpenCV, already trained for frontal faces
	* You can train your own, but it can take a few days

[OpenCV "Cascade" Image Detection Classifier](http://docs.opencv.org/modules/objdetect/doc/cascade_classification.html?highlight=detectmultiscale#cascadeclassifier-detectmultiscale)

* Detects objects of different sizes in the input image.
* The detected objects are returned as a list of rectangles.


### Hough Transform
##### 4/27/14

* [OpenCV Docs](http://docs.opencv.org/doc/tutorials/imgproc/imgtrans/hough_lines/hough_lines.html)
* [Wikipedia](http://en.wikipedia.org/wiki/Hough_transform)
* [OpenCV Python Tutorials](http://opencv-python-tutroals.readthedocs.org/en/latest/py_tutorials/py_imgproc/py_houghlines/py_houghlines.html)

* **Detects a given shape** even if it's broken or distorted a bit
* One should do edge detection as a preprocessing step
* There is a *probabilistic* version that is more efficient, it only analyzes
  a random subset of the image's points
* One could use it to find the lines in a picture of a Sudoku puzzle
* **Hough Lines** - find *lines* in (thresholded) image
* **Hough Circles** - find *circles* in (thresholded) image

#### Rough estimate of the algorithm

1. For each point on a feature (e.g. edge detected area), increment the set of
   lines in polar coordinates it belongs to (r, \theta).
2. The top however-many incremented line-coordinates are your Hough-lines.


### [SIFT](http://en.wikipedia.org/wiki/Scale-invariant_feature_transform)

**Scale-invariant feature transform**

##### 4/27/14

* First published/patented by David Lowe/Univ. British Columbia in **1999**
* Used for
  * **object recognition**
  * **robotic mapping and navigation** - in which a robot builds a map of an
    environment
  * **image stitching** - stitching images together into a panorama
  * **3D modeling** - generating a 3D representation of a 3D thing
  * **gesture recognition** - recognition/interpretation of humanoid gestures
  * **video tracking** - locating moving object(s) over time with a camera
  * **match moving** - inserting computer graphics into live-action footage
    "correctly"

SIFT can robustly identify objects even among clutter and under partial
occlusion, because **the SIFT feature descriptor is invariant to uniform
scaling, orientation, and partially invariant to affine distortion and
illumination changes**.

* SIFT keypoints of objects are first extracted from a set of reference images
  and stored in a database.
* An object is recognized in a new image by individually comparing each
  feature from the new image to this database and finding candidate matching
  features based on Euclidean distance of their feature vectors.
* Then more transformations and algorithms are applied to find the probability
  that a particular set of features indicates the presence of an object.

#### Steps, as far as I can tell

1. The initial feature extractor transforms an image into a large collection
   of feature vectors, each of which is invariant to image translation,
   scaling, and rotation, partially invariant to illumination changes and
   robust to local geometric distortion. These features share similar
   properties with neurons in inferior temporal cortex that are used for
   object recognition in primate vision.

2. Next a tree is built to match extracted features from one image with those
   of a new image. Lowe developed a particularly fast algorithm for
   accomplishing this.

3. Next *linear least squares* and *outlier detection* are performed
   (repeatedly) to remove unlikely hypotheses.



## AI Shack

### [Image moments](http://www.aishack.in/2012/02/scanning-qr-codes/)

##### 4/27/14

* Zero-eth moment is just the sum of the values
* The two first moments are the Sum of (each value times its location on one
  axis)
* From these we can find the centroid of a blob
* We can use higher order moments to capture translation, scale, and rotation
  invarients

### Edge Detection

#### [Canny Edge Detector](http://www.aishack.in/2011/06/the-canny-edge-detector/)

##### 4/27/14

##### How it works, with usage notes

* Preprocess by smoothing image a bit first (e.g.by adding 5x5 Gaussian filter
  with std-dev 1.4) because edge-detectors are prone to noise.
* The implementation may *not* do this by default, so you must do it yourself.
* If you can, make the image greyscale too.
* Uses the **Sobel Edge Detector** to find the magnitudes and directions of
  its gradients.
* Then edges are perpendicular to the gradient direction.
* We only label the ones above a certain upper-threshold.
* Now we go to the labeled edges from the previous step, and look on either
  side of it according to its orientation, and if it is above a certain lower-
  threshold, we declare it to be a continuation of the edge.
* Now we repeat the above until there are no changes to the image.


#### [Sobel Edge Detector](http://www.aishack.in/2011/04/the-sobel-and-laplacian-edge-detectors/)

##### 4/27/14

* Uses a particular **convolution kernel** that includes a horizontal
  component, vertical component, and smoothing to be less affected by noise
  than a more naive detector

#### [Laplacian edge detector](http://www.aishack.in/2011/04/the-sobel-and-laplacian-edge-detectors/)

##### 4/27/14

If you think of the (greyscale) image as a surface of light-intensity over the
x-y plane, then whenever the 2nd derivative of that function is zero, you
(may) have an edge. We approximate these derivatives with the help of
convolutions.

### Convolutions

##### 4/25/14

* [AI Shack Explanation](http://www.aishack.in/2010/08/convolutions/)
* [AI Shack Examples](http://www.aishack.in/2010/08/image-convolution-examples/)


AI Shack's explanation is far better than any I could come up with:

> The convolution kernel is a small matrix. This matrix has numbers in each
> cell and has an anchor point. This kernel slides over an image and does its
> thing. The “anchor” point is used to determine the position of the kernel
> with respect to the image. The anchor point starts at the top-left corner of
> the image and moves over each pixel sequentially. At each position, the
> kernel overlaps a few pixels on the image. Each overlapping pair of numbers
> is multiplied and added. Finally, the value at the current position is set
> to this sum.


## Udacity Intro AI with Sebastian Thrun

##### 4/27/14

#### Image formation Equation

x = X*(f/z)

* x := image size
* X := object size
* z := dist of obj to pinhole camera
* f := dist of img to pinhole camera

#### Invariances

"You'd like to be *invariant* to these natural variations":

* scale - distance
* illumination - the shadows etc.
* rotation - spinning
* deformation - like a shirt in the wind
* occlusion - it gets obscurred behind something else
* view point - seeing it from different vantage points

In computer vision, we don't use color-images very much because greyscale is
more robust to lighting variation.

#### Greyscale

Greyscale images can be represented by a matrix of values from 0 (black) to
255 (white).

#### Linear Filter

* I := original greyscale image
* (x) := the (convolution?) operator
* g := kernel / mask
* I' := resulting (filtered) image

"run g over I"

* I (x) g ~~> I'

"pseudocode"

* I'(x,y) = Sum_{U,V}I(x-u,y-v)*g(u,v)

#### Harris corner detector

A formula involving eigenvalues. It's also kind of rotation invariant, but the
SIFT algorithm is better.

#### Correspondence

Figuring out where/how a point from image A appears in image B.

One can use *Sum Squared Difference (SSD) Minimization*
