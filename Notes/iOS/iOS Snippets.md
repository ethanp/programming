### Turn view into image to be saved to Camera Roll

    UIGraphicsBeginImageContext(view.bounds.size);
    [view.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();

### Save image code to Camera Roll

    UIImage *i = ...;
    ALAssetsLibrary *library = [[ALAssetsLibrary alloc] init];
    CGImageRef r = i.CGImage;
    [library writeImageToSavedPhotosAlbum:r metadata:nil completionBlock:nil];

### Make method abstract

##### 4/28/14

Just raise a suitable Exception if the base class's implementation is called.

	#define mustOverride() @throw [NSException exceptionWithName:NSInvalidArgumentException\
	reason:[NSString stringWithFormat:@"%s must be overridden in a subclass/category", __PRETTY_FUNCTION__] userInfo:nil]
	
### Execute block after delay

##### 5/1/14

	- (void)afterDelay:(float)time performBlock:(void(^)(void))block
	{
	    dispatch_time_t inABit = dispatch_time(DISPATCH_TIME_NOW, (int64_t) time * NSEC_PER_SEC);
	    dispatch_after(inABit, dispatch_get_main_queue(), block);
	}

	[self afterDelay:0.4 performBlock:^{
		self.okToSnap=YES;
	}];

### Synchronize with semaphore

##### 5/1/14

    instance.sema = dispatch_semaphore_create(0);
    [instance run];
    dispatch_semaphore_wait(instance.sema, DISPATCH_TIME_FOREVER);
    
    - (void)run {
        dispatch_semaphore_signal(self.sema);
    }