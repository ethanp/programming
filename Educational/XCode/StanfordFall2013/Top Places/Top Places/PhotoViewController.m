//
//  PhotoViewController.m
//  Top Places
//
//  Created by Ethan Petuchowski on 1/1/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "PhotoViewController.h"

@interface PhotoViewController () <UIScrollViewDelegate>

@property (nonatomic, strong) UIImageView *imageView;
@property (nonatomic, strong) UIImage *image;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;

@end


@implementation PhotoViewController

/* copy-pasted much of this from the Professor's Imaginarium code */

#pragma mark - View Controller Lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.scrollView addSubview:self.imageView];
}

#pragma mark - Properties

- (UIImageView *)imageView
{
    if (!_imageView) _imageView = [[UIImageView alloc] init];
    return _imageView;
}

/* don't need to `@synthesize image` because we never use it as its own
   instance variable, it just gets/sets the values of the imageView propery */

- (UIImage *)image
{
    return self.imageView.image;
}

- (void)setImage:(UIImage *)image
{
    self.imageView.image = image; /* does not change the frame of the UIImageView */
    [self.imageView sizeToFit];   /* update the frame of the UIImageView */
    
    /* self.scrollView is nil if outlet-setting hasn't happened */
    self.scrollView.contentSize = self.image ? self.image.size : CGSizeZero;
    
    [self.spinner stopAnimating];
}

- (void)setScrollView:(UIScrollView *)scrollView
{
    _scrollView = scrollView;
    
    /* necessary for zooming */
    _scrollView.minimumZoomScale = 0.2;
    _scrollView.maximumZoomScale = 2.0;
    _scrollView.delegate = self;
    
    /* in case self.image gets set before self.scrollView does
     * for example, prepareForSegue:sender: is called before outlet-setting phase */
    self.scrollView.contentSize = self.image ? self.image.size : CGSizeZero;
}

#pragma mark - UIScrollViewDelegate

/* mandatory zooming method in UIScrollViewDelegate protocol */
- (UIView *)viewForZoomingInScrollView:(UIScrollView *)scrollView
{
    return self.imageView;
}

#pragma mark - Setting the Image from the Image's URL

- (void)setImageURL:(NSURL *)imageURL
{
    _imageURL = imageURL;
    [self startDownloadingImage];
}

- (void)startDownloadingImage
{
    self.image = nil;
    if (self.imageURL)
    {
        [self.spinner startAnimating];
        
        NSURLRequest *request = [NSURLRequest requestWithURL:self.imageURL];
        NSURLSession *session = [NSURLSession sessionWithConfiguration:
                   [NSURLSessionConfiguration ephemeralSessionConfiguration]];
        
        NSURLSessionDownloadTask *task =
            [session downloadTaskWithRequest:request completionHandler:
                ^(NSURL *localfile, NSURLResponse *response, NSError *error) {
                    if (!error && [request.URL isEqual:self.imageURL]) {
                        /* UIImage is an exception to the "can't do UI here" */
                        UIImage *image = [UIImage imageWithData:
                                          [NSData dataWithContentsOfURL:localfile]];
                        dispatch_async(dispatch_get_main_queue(),
                                       ^{ self.image = image; });
                    }
                }
            ];
        [task resume]; /* all NSURLSession tasks start out suspended */
    }
}


@end
