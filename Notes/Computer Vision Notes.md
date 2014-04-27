# Computer Vision

## AI Shack

### Edge Detection

#### Canny Edge Detector

##### 4/25/14

[Great AI Shack Tutorial](http://www.aishack.in/2011/06/the-canny-edge-detector/)

##### Usage notes

* Preprocess by smoothing image a bit first (e.g.by adding 5x5 Gaussian filter with std-dev 1.4) because edge-detectors are prone to noise. The implementation may *not* do this by default, so you must do it yourself.

##### Method of action

* Uses the **Sobel Edge Detector**


#### Sobel Edge Detector

##### 4/25/14

[AI Shack](http://www.aishack.in/2011/04/the-sobel-and-laplacian-edge-detectors/)

* Uses a particular **convolution kernel** that includes a horizontal component, 
  vertical component, and smoothing to be less affected by noise than a more 
  naive detector
  
#### Laplacian edge detector

[AI Shack](http://www.aishack.in/2011/04/the-sobel-and-laplacian-edge-detectors/)

If you think of the (greyscale) image as a surface of light-intensity over the
x-y plane, then whenever the 2nd derivative of that function is zero, you (may) 
have an edge.

### Convolutions

##### 4/25/14

[AI Shack Explanation](http://www.aishack.in/2010/08/convolutions/)
[AI Shack Examples](http://www.aishack.in/2010/08/image-convolution-examples/)


AI Shack's explanation is far better than any I could come up with:

> The convolution kernel is a small matrix. This matrix has numbers in each cell 
> and has an anchor point.
> This kernel slides over an image and does its thing. The “anchor” point is used 
> to determine the position of the kernel with respect to the image.
> The anchor point starts at the top-left corner of the image and moves over each 
> pixel sequentially. At each position, the kernel overlaps a few pixels on the 
> image. Each overlapping pair of numbers is multiplied and added. Finally, the 
> value at the current position is set to this sum.


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

In computer vision, we don't use color-images very much because greyscale
is more robust to lighting variation.

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

A formula involving eigenvalues. It's also kind of rotation invariant, but
the SIFT algorithm is better.

#### Correspondence

Figuring out where/how a point from image A appears in image B.

One can use **Sum Squared Difference (SSD) Minimization**
