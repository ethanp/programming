# -*- coding: utf-8 -*-
# 4/24, 5/1, 5/2 -- 2014
# Ethan Petuchowski
# OpenCV with Python, Initial Experiments
import cv2
import os
import numpy as np
from matplotlib import pyplot as plt

os.chdir('/Users/ethan')

video = cv2.VideoCapture('Movies/mov.mov')

img = cv2.imread('Pictures/IMG_0004 2.jpg')
first, second = cv2.imread('Movies/first.jpg'), cv2.imread('Movies/second.jpg')

def exper1():
    """
    desc: find image gradients using Sobel-X/Y and Laplacian
    date: 4/24/14
    source: opencv-python-tutroals.readthedocs.org/en/latest/py_tutorials/py_imgproc/py_gradients/py_gradients.html (top of page)
    result: didn't do anything useful
    """
    laplacian = cv2.Laplacian(img,cv2.CV_64F)
    sobelx = cv2.Sobel(img,cv2.CV_64F,1,0,ksize=5)
    sobely = cv2.Sobel(img,cv2.CV_64F,0,1,ksize=5)
    plt.subplot(2,2,1),plt.imshow(img,cmap = 'gray')
    plt.title('Original'), plt.xticks([]), plt.yticks([])
    plt.subplot(2,2,2),plt.imshow(laplacian,cmap = 'gray')
    plt.title('Laplacian'), plt.xticks([]), plt.yticks([])
    plt.subplot(2,2,3),plt.imshow(sobelx,cmap = 'gray')
    plt.title('Sobel X'), plt.xticks([]), plt.yticks([])
    plt.subplot(2,2,4),plt.imshow(sobely,cmap = 'gray')
    plt.title('Sobel Y'), plt.xticks([]), plt.yticks([])
    plt.show()


def exper2():
    """
    desc: find image gradients using Sobel-X/Y and Laplacian (like exper1), but with smarter data-type usage
    date: 4/24/14
    source: opencv-python-tutroals.readthedocs.org/en/latest/py_tutorials/py_imgproc/py_gradients/py_gradients.html (bottom of page)
    result: didn't do anything useful
    """
    # Output dtype = cv2.CV_8U
    sobelx8u = cv2.Sobel(img,cv2.CV_8U,1,0,ksize=5)
    # Output dtype = cv2.CV_64F. Then take its absolute and convert to cv2.CV_8U
    sobelx64f = cv2.Sobel(img,cv2.CV_64F,1,0,ksize=5)
    abs_sobel64f = np.absolute(sobelx64f)
    sobel_8u = np.uint8(abs_sobel64f)
    plt.subplot(1,3,1),plt.imshow(img,cmap = 'gray')
    plt.title('Original'), plt.xticks([]), plt.yticks([])
    plt.subplot(1,3,2),plt.imshow(sobelx8u,cmap = 'gray')
    plt.title('Sobel CV_8U'), plt.xticks([]), plt.yticks([])
    plt.subplot(1,3,3),plt.imshow(sobel_8u,cmap = 'gray')
    plt.title('Sobel abs(CV_64F)'), plt.xticks([]), plt.yticks([])
    plt.show()


def exper3():
    """
    desc: Canny Edge Detector
    date: 4/24/14
    source: opencv-python-tutroals.readthedocs.org/en/latest/py_tutorials/py_imgproc/py_canny/py_canny.html
    result: LOOKS LIKE IT FOUND IT!
    """
    edges = cv2.Canny(img,100,200)
    plt.subplot(121),plt.imshow(img,cmap = 'gray')
    plt.title('Original Image'), plt.xticks([]), plt.yticks([])
    plt.subplot(122),plt.imshow(edges,cmap = 'gray')
    plt.title('Edge Image'), plt.xticks([]), plt.yticks([])
    plt.show()


def exper4():
    """
    desc: Simple Image Thresholding
    date: 4/24/14
    source: opencv-python-tutroals.readthedocs.org/en/latest/py_tutorials/py_imgproc/py_thresholding/py_thresholding.html
    result: LOOKS LIKE IT FOUND IT!
    """
    ret,thresh1 = cv2.threshold(img,127,255,cv2.THRESH_BINARY)
    ret,thresh2 = cv2.threshold(img,127,255,cv2.THRESH_BINARY_INV)
    ret,thresh3 = cv2.threshold(img,127,255,cv2.THRESH_TRUNC)
    ret,thresh4 = cv2.threshold(img,127,255,cv2.THRESH_TOZERO)
    ret,thresh5 = cv2.threshold(img,127,255,cv2.THRESH_TOZERO_INV)

    titles = ['Original Image','BINARY','BINARY_INV','TRUNC','TOZERO','TOZERO_INV']
    images = [img, thresh1, thresh2, thresh3, thresh4, thresh5]

    for i in xrange(6):
        plt.subplot(2,3,i+1),plt.imshow(images[i],'gray')
        plt.title(titles[i])
        plt.xticks([]),plt.yticks([])

    plt.show()


def exper5():
    """
    desc: Adaptive Image Thresholding
    date: 4/24/14
    source: opencv-python-tutroals.readthedocs.org/en/latest/py_tutorials/py_imgproc/py_thresholding/py_thresholding.html
    result: TODO: DOESN"T WORK, I think the image datatype needs to be changed, BUT THIS *IS* A GOOD IDEA
    """
    ret,th1 = cv2.threshold(img,127,255,cv2.THRESH_BINARY)
    th2 = cv2.adaptiveThreshold(img,255,cv2.ADAPTIVE_THRESH_MEAN_C,cv2.THRESH_BINARY,11,2)
    th3 = cv2.adaptiveThreshold(img,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C,cv2.THRESH_BINARY,11,2)

    titles = ['Original Image', 'Global Thresholding (v = 127)',
                'Adaptive Mean Thresholding', 'Adaptive Gaussian Thresholding']
    images = [img, th1, th2, th3]

    for i in xrange(4):
        plt.subplot(2,2,i+1),plt.imshow(images[i],'gray')
        plt.title(titles[i])
        plt.xticks([]),plt.yticks([])
    plt.show()

def plot_four_imgs(images, titles=None):
    """
    desc: use matplotlib.pyplot to show four images at once
    date: 5/1/14
    param `images`: a list of four references to actual images
    source: exper5() above
    """
    for i in xrange(4):
        plt.subplot(2,2,i+1), plt.imshow(images[i], 'gray')
        if titles: plt.title(titles[i])
        plt.xticks([]),plt.yticks([])
    plt.show()

def plot_two_imgs(images):
    """
    desc: use matplotlib.pyplot to show 2 images at once side by side
    date: 5/1/14
    param `images`: a list of four references to actual images
    source: exper5() above
    result: it worked, though you have to close the window every time you want to re-run it
    """
    plt.subplot(1,2,1), plt.imshow(images[0], 'gray')
    plt.xticks([]),plt.yticks([])
    plt.subplot(1,2,2), plt.imshow(images[1], 'gray')
    plt.xticks([]),plt.yticks([])
    plt.show()

def two_imgs_frm_video():
    """
    desc: 'plot' two different images from the global video
    date: 5/1/14
    source: opencv-python-tutroals.readthedocs.org/en/latest/py_tutorials/py_gui/py_video_display/py_video_display.html
    result: it worked
    """
    while video.isOpened():
        frames = [video.read()[1] for i in xrange(50)][::25]
        break
    plot_two_imgs(frames)

def save_two_imgs_frm_video():
    """
    desc: save two images from global video to disk so they can be opened quickly later
    date: 5/1/14
    source: docs.opencv.org/modules/highgui/doc/reading_and_writing_images_and_video.html
    result: it worked
    """
    while video.isOpened():
        frames = [video.read()[1] for i in xrange(50)][::25]
        #gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        break
    cv2.imwrite('Movies/first.jpg',frames[0])
    cv2.imwrite('Movies/second.jpg',frames[1])

def plot_the_img_seq():
    """
    desc: plot the global picture files 'first' and 'second'
    date: 5/1/14
    source: EP
    result: it worked
    """
    plot_two_imgs([first, second])

def subtract_img_seq():
    """
    desc: subtract "first" from "second" and πlø† the resulting image
    date: 5/1/14
    result: the whole picture is either black or nearly black,
            clearly not enough differentiation to do any decent thresholding
    """
    s = cv2.subtract(first, second)
    plt.subplot(1,1,1), plt.imshow(s, 'gray')
    plt.xticks([]),plt.yticks([])
    plt.show()

def bg_subtractor_sum1():
    """
    desc: use opencv's built-in bg subtractor to perform our bg subtracting, then add those images together
    date: 5/1/14
    source: opencv-python-tutroals.readthedocs.org/en/latest/py_tutorials/py_video/py_bg_subtraction/py_bg_subtraction.html
    result: it *almost* works, but in addition to it not *quite* working, it is WAY TOO SLOW
    disc: it could be that my video isn't very good for this purpose and it would work with a different video
    """
    fgbg = cv2.BackgroundSubtractorMOG(50, 6, .99999, 1000)

    frames = []
    for i in xrange(600):
        ret, frame = video.read()
        if i % 4 == 0:
            print i,
            fg_mask = fgbg.apply(frame)
            frames.append(fg_mask)
    f = frames[0]
    for i in xrange(1, len(frames)):
        f = cv2.add(f, frames[i])
        cv2.imshow('frame', frames[i])
        c = cv2.waitKey(1)
        if c == 27:
            break
    cv2.destroyAllWindows()
    plt.subplot(1,1,1), plt.imshow(f, 'gray')
    plt.xticks([]),plt.yticks([])
    plt.show()

def crop_known_spot():
    """
    desc: crop the given image to only contain the place we want
    date: 5/2/14
    source: stackoverflow.com/questions/15589517/how-to-crop-an-image-in-opencv-using-python
    result: I successively found the coordinates I need
    """
    cropped = first[450:600, 300:400]  # note it's why *then* ex
    plt.subplot(1,1,1), plt.imshow(cropped, 'Set1')
    plt.xticks([]),plt.yticks([])
    plt.show()

def crop_spot(pic):
    """
    desc: take given pic and crop out the location we want
    date: 5/2/14
    """
    return pic[450:600, 300:400]

def cropped_bg_sum1():
    """
    desc: pass the cropped image through the background summation algorithm
          "bg_subber_sum1" from yesterday, though blur it first.
    date: 5/2/14
    source: Ian Burnett
    result: It worked. The algorithm barely squeaks by, but the cropping the image was super helpful.
            And when I crop a different spot, the whole thing becomes why pretty quickly, which is definitely a good sign.
    """
    fgbg = cv2.BackgroundSubtractorMOG(500, 6, 0.9, 1)
    _, f = video.read()
    f = crop_spot(f)
    f = cv2.blur(f,(18,18)) # this was super effective too
    f = cv2.blur(f,(16,16))
    f = fgbg.apply(f)

    for i in xrange(200):
        _, frame = video.read()
        cropped = crop_spot(frame)
        fg_mask = fgbg.apply(cropped)
        if i % 5 == 0:
            print i,
            f = cv2.add(f, fg_mask)
            cv2.imshow('frame', f)
            c = cv2.waitKey(1)
            if c == 27:
                break

    cv2.destroyAllWindows()
    plt.subplot(1,1,1), plt.imshow(f, 'gray')
    plt.xticks([]), plt.yticks([])
    plt.show()

def img_grid_sample1(image=first, height=3, width=3):
    """
    desc: turn a (specified) image into a (specified) grid of images
    date: 5/2/14
    result: It works. Rather ironically the spot-to-find was split across multiple grid-slots.
            I guess whatever resulting algorithm is going to have to be robust to that.
    """
    iH, iW, iDepth = image.shape
    secW, secH = iW/width, iH/height
    for h in range(height):
        for w in range(width):
            plt.subplot(height,width, h*width+w+1)
            plt.imshow(image[h*secH:(h+1)*secH,w*secW:(w+1)*secW], 'gray')
            plt.xticks([]), plt.yticks([])
    plt.show()

def img_to_grid(image=first, height=3, width=3):
    """
    desc: turn a specified image into an array of its grid elements
    date: 5/2/14
    result: works
    """
    if len(image.shape) == 3:
        iH, iW, iDepth = image.shape
    else:
        iH, iW = image.shape
    secW, secH = iW/width, iH/height
    return [image[h*secH:(h+1)*secH,w*secW:(w+1)*secW]
            for h in range(height) for w in range(width)]

def img_grid_sample2(image=first, height=3, width=3):
    """
    desc: rewrite img_grid_sample1() using img_to_grid()
            "turn a (specified) image into a (specified) grid of images"
    date: 5/2/14
    result: works
    """
    grid = img_to_grid(image, height, width)
    for h in range(height):
        for w in range(width):
            p_num = (h*width + w) + 1
            plt.subplot(height, width, p_num)
            plt.imshow(grid[p_num-1], 'gray')
            plt.xticks([]), plt.yticks([])
    plt.show()

def plot_img_grid(grid=None, height=3, width=3):
    for h in range(height):
        for w in range(width):
            p_num = (h*width + w) + 1
            plt.subplot(height, width, p_num)
            plt.imshow(grid[p_num-1], 'gray')
            plt.xticks([]), plt.yticks([])
    plt.show()

def plot_img(image=first):
    plt.subplot(1,1,1)
    plt.imshow(image, 'gray')
    plt.xticks([]), plt.yticks([])
    plt.show()


def avg_imgs_from_vid():
    """
    desc: avg a few greyed, gridded images from the video
    date: 5/2/14
    result: works, doesn't work as well as I'd hoped
    """
    grey_grids = []
    for i in xrange(200):
        _, frame = video.read()
        if i % 5 == 0:
            print i,
            grey_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            grey_frame = cv2.blur(grey_frame, (10,10)) # blurring optional
            grey_grid = img_to_grid(grey_frame)
            grey_grids.append(grey_grid)
            cv2.imshow('frame', grey_frame)

    flt_grid = [np.float32(i) for i in grey_grids[0]]
    for grey_grid in grey_grids:
        for loc in range(len(grey_grid)):
            cv2.accumulateWeighted(grey_grid[loc], flt_grid[loc], 0.3)
    plot_img_grid(flt_grid)
    cv2.destroyAllWindows()

def grid_blur(grid, dimensions=10): # works
    return grid_apply(grid, cv2.blur, ((dimensions, dimensions)))

def try_avg():
    """
    desc: use opencv's `accumulateWeighted` on two images from a video
    date: 5/2/14
    source: opencvpython.blogspot.com/2012/07/background-extraction-using-running.html
    result: works, took forever, but figured out the problem was I was trying to capture
            a return value from accumulateWeighted, but there shouldn't be one
    """
    _, a = video.read()
    for i in xrange(100): video.read()
    _, b = video.read()
    gA = cv2.cvtColor(a, cv2.COLOR_BGR2GRAY)
    gB = cv2.cvtColor(b, cv2.COLOR_BGR2GRAY)
    fB = np.float32(gB)
    cv2.accumulateWeighted(gA, fB, 0.3)
    cv2.accumulateWeighted(gB, fB, 0.3)
    plot_img(fB)

# it's getting better all the time...
def grid_apply(grid, f, *args): return [f(i, *args) for i in grid]

if __name__ == '__main__':
    avg_imgs_from_vid()
